<template>
  <div class="express-list">
    <!-- 查询条件 -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="queryForm" size="small">
        <!-- 收件：手动输入 -->
        <el-form-item label="快递单号">
          <el-input
            v-model="queryForm.expressNo"
            placeholder="请输入快递单号"
            clearable
          />
        </el-form-item>

        <el-form-item label="收件人">
          <el-input
            v-model="queryForm.receiverName"
            placeholder="请输入收件人"
            clearable
          />
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable>
            <el-option label="待入库" :value="0" />
            <el-option label="已入库" :value="1" />
            <el-option label="用户已取件" :value="2" />
            <el-option label="快递已出库" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="fetchList"
            >查询</el-button
          >
          <el-button type="success" icon="el-icon-plus" @click="openAdd"
            >新增快递</el-button
          >
          <el-button type="warning" icon="el-icon-refresh" @click="resetQuery"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never" class="table-card">
      <el-table :data="list" stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="expressNo" label="快递单号" width="160" />
        <el-table-column prop="companyName" label="快递公司" width="120" />
        <el-table-column prop="senderName" label="寄件人" width="100" />
        <el-table-column prop="senderPhone" label="寄件电话" width="120" />
        <el-table-column prop="receiverName" label="收件人" width="100" />
        <el-table-column prop="receiverPhone" label="收件电话" width="120" />
        <el-table-column prop="weight" label="重量(kg)" width="100" />

        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)">{{
              statusText(row.status)
            }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="创建时间" width="170">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>

        <el-table-column
          prop="receiverAddress"
          label="收件地址"
          min-width="200"
        />

        <el-table-column prop="bizType" label="业务类型" width="120">
          <template slot-scope="scope">
            <a-tag v-if="scope.row.bizType === 1" color="blue">
              收件
            </a-tag>
            <a-tag v-else-if="scope.row.bizType === 2" color="green">
              寄件
            </a-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="创建人" width="100" />
        <el-table-column prop="remark" label="备注" width="170" />

        <el-table-column
          label="操作"
          width="150"
          fixed="right"
          align="center"
          class-name="action-column"
        >
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button type="danger" size="mini" @click="openDelete(row)"
                >删除</el-button
              >
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        ></el-pagination>
      </div>
    </el-card>
    <el-dialog title="删除确认" :visible.sync="deleteVisible" width="400px">
      <span>确定要删除这条快递记录吗？</span>

      <template #footer>
        <el-button @click="deleteVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确定</el-button>
      </template>
    </el-dialog>

    <!-- 新增弹窗 -->
    <el-dialog title="新增快递" :visible.sync="addVisible" width="600px">
      <el-form
        :model="addForm"
        label-width="100px"
        :rules="addFormRules"
        ref="addFormRef"
      >
        <el-form-item label="业务类型" prop="bizType">
          <el-radio-group
            v-model="addForm.bizType"
            :disabled="!userRoles.includes('ROLE_SUPER_ADMIN')"
          >
            <el-radio :label="1">收件</el-radio>
            <el-radio :label="2">寄件</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
          label="快递单号"
          prop="expressNo"
          v-if="addForm.bizType === 1"
        >
          <el-input v-model="addForm.expressNo" placeholder="请输入快递单号" />
        </el-form-item>

        <el-form-item label="快递单号" v-else>
          <el-input v-model="addForm.expressNo" readonly />
        </el-form-item>
        <el-form-item label="快递公司" prop="companyCode">
          <el-select
            v-model="addForm.companyCode"
            placeholder="请选择快递公司"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in companyOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="寄件人" prop="senderName">
          <el-input
            v-model="addForm.senderName"
            placeholder="请输入寄件人姓名"
          />
        </el-form-item>
        <el-form-item label="寄件电话" prop="senderPhone">
          <el-input
            v-model="addForm.senderPhone"
            placeholder="请输入寄件人电话"
          />
        </el-form-item>
        <el-form-item label="收件人" prop="receiverName">
          <el-input
            v-model="addForm.receiverName"
            placeholder="请输入收件人姓名"
          />
        </el-form-item>
        <el-form-item label="收件电话" prop="receiverPhone">
          <el-input
            v-model="addForm.receiverPhone"
            placeholder="请输入收件人电话"
          />
        </el-form-item>
        <el-form-item label="收件地址" prop="receiverAddress">
          <el-input
            v-model="addForm.receiverAddress"
            placeholder="请输入收件地址"
          />
        </el-form-item>
        <el-form-item label="重量(kg)" prop="weight">
          <el-input-number
            v-model="addForm.weight"
            :min="0"
            :step="0.1"
            placeholder="请输入重量"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            type="textarea"
            v-model="addForm.remark"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="addVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { fetchExpressList, saveExpress, deleteExpress } from "@/api/expressApi";
import { getRoles } from "@/api/admin";

export default {
  name: "ExpressList",
  data() {
    return {
      userRoles: [],
      companyOptions: [
        { label: "顺丰", value: "SF" },
        { label: "中通", value: "ZT" },
        { label: "圆通", value: "YT" },
        { label: "申通", value: "ST" },
        { label: "韵达", value: "YD" },
        { label: "京东", value: "JD" },
      ],
      deleteVisible: false,
      deleteRow: null,
      loading: false,
      list: [],
      queryForm: {
        expressNo: "",
        receiverName: "",
        status: "",
      },
      addVisible: false,
      addForm: {
        bizType: null,
        expressNo: "",
        companyCode: "",
        senderName: "",
        senderPhone: "",
        receiverName: "",
        receiverPhone: "",
        receiverAddress: "",
        weight: null,
        remark: "",
        createUserId: "",
      },
      addFormRules: {
        bizType: [
          { required: true, message: "请选择业务类型", trigger: "change" },
        ],

        companyCode: [
          { required: true, message: "请选择快递公司", trigger: "change" },
        ],

        expressNo: [
          {
            validator: (rule, value, callback) => {
              if (!value) {
                callback(new Error("快递单号不能为空"));
              } else {
                callback();
              }
            },
            trigger: "blur",
          },
        ],

        // 寄件人必填
        senderName: [
          { required: true, message: "请输入寄件人姓名", trigger: "blur" },
        ],

        // 寄件电话必填
        senderPhone: [
          { required: true, message: "请输入寄件人电话", trigger: "blur" },
          {
            pattern: /^1[3-9]\d{9}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],

        // 收件人必填
        receiverName: [
          { required: true, message: "请输入收件人姓名", trigger: "blur" },
        ],

        // 收件电话必填
        receiverPhone: [
          { required: true, message: "请输入收件人电话", trigger: "blur" },
          {
            pattern: /^1[3-9]\d{9}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],

        // 收件地址必填
        receiverAddress: [
          { required: true, message: "请输入收件地址", trigger: "blur" },
        ],

        // 重量必填
        weight: [
          { required: true, message: "请输入重量", trigger: "blur" },
          {
            validator: (rule, value, callback) => {
              if (value <= 0) {
                callback(new Error("重量必须大于0"));
              } else {
                callback();
              }
            },
            trigger: "blur",
          },
        ],
      },
      // 分页相关数据
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
      },
    };
  },
  created() {
    this.fetchList();
    this.initRoles();
  },
  watch: {
    // 业务类型变化
    "addForm.bizType"(val) {
      this.addForm.expressNo = "";

      if (val === 2 && this.addForm.companyCode) {
        this.addForm.expressNo = this.generateExpressNo(
          this.addForm.companyCode
        );
      }
    },

    // 快递公司变化
    "addForm.companyCode"(val) {
      if (this.addForm.bizType === 2 && val) {
        this.addForm.expressNo = this.generateExpressNo(val);
      }
    },
  },
  methods: {
    async initRoles() {
      try {
        const res = await getRoles();
        console.log('res', res);
        if (res && res.data.token) {
          // 如果后端返回单角色
          this.userRoles = [res.data.token];
        } else {
          this.userRoles = [];
        }
      } catch (e) {
        console.error("获取角色失败", e);
        this.userRoles = [];
      }
    },
    generateExpressNo(companyCode) {
      if (!companyCode) return "";
      return `${companyCode}${Date.now()}`;
    },
    async fetchList() {
      const user = JSON.parse(localStorage.getItem("user") || "{}");
      const params = {
        ...this.queryForm,
        createUserId: user.id,
        pageNum: this.pagination.currentPage,
        pageSize: this.pagination.pageSize,
      };

      this.loading = true;
      try {
        const res = await fetchExpressList(params);

        if (res.data && res.data.data) {
          this.list = res.data.data || []; // 当前页数据列表
          this.pagination.total = res.data.total || 0;
          this.pagination.currentPage = res.data.pageNum || 1;
          this.pagination.pageSize = res.data.pageSize || 10;
        } else {
          this.list = [];
          this.pagination.total = 0;
        }
      } catch (error) {
        console.error("获取快递列表失败:", error);
        this.list = [];
        this.pagination.total = 0;
      } finally {
        this.loading = false;
      }
    },

    // 重置查询条件
    resetQuery() {
      this.queryForm = {
        expressNo: "",
        receiverName: "",
        status: "",
      };
      this.pagination.currentPage = 1;
      this.fetchList();
    },

    // 处理每页显示条数变化
    handleSizeChange(val) {
      this.pagination.pageSize = val;
      this.pagination.currentPage = 1; // 每页条数改变回到第一页
      this.fetchList();
    },
    // 处理当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val;
      this.fetchList();
    },

    statusText(status) {
      return (
        { 0: "待入库", 1: "已入库", 2: "用户已取件", 3: "快递已出库" }[
          status
        ] || "未知"
      );
    },
    statusTag(status) {
      return (
        { 0: "info", 1: "success", 2: "warning", 3: "warning" }[status] || ""
      );
    },
    openAdd() {
      const user = JSON.parse(localStorage.getItem("user") || "{}");
      // 默认业务类型
      let defaultBizType = null;
      if (this.userRoles.includes("ROLE_USER")) defaultBizType = 2;
      // 寄件
      else if (this.userRoles.includes("ROLE_COURIER")) defaultBizType = 1;
      // 收件
      else defaultBizType = null; // 管理员可选
      this.addForm = {
        bizType: defaultBizType,
        expressNo: "",
        companyCode: "",
        companyName: "",
        senderName: "",
        senderPhone: "",
        receiverName: "",
        receiverPhone: "",
        receiverAddress: "",
        weight: null,
        remark: "",
        createUserId: user.id || "",
      };

      if (this.$refs.addFormRef) {
        this.$refs.addFormRef.clearValidate();
      }

      this.addVisible = true;
    },
    async submitAdd() {
      // 表单验证
      try {
        const company = this.companyOptions.find(
          (c) => c.value === this.addForm.companyCode
        );

        if (company) {
          this.addForm.companyName = company.label;
        }
        await this.$refs.addFormRef.validate();

        this.loading = true;
        await saveExpress(this.addForm);
        this.$message.success("新增成功");
        this.addVisible = false;

        // 新增后重置到第一页
        this.pagination.currentPage = 1;
        this.fetchList();
      } catch (error) {
        if (error && error.fields) {
          this.$message.error("请填写完整信息");
        } else {
          this.$message.error("新增失败");
        }
      } finally {
        this.loading = false;
      }
    },
    openDelete(row) {
      console.log("1", row);
      this.deleteRow = row;
      this.deleteVisible = true;
    },

    async confirmDelete() {
      console.log("2", this.deleteRow.id);
      try {
        await deleteExpress(this.deleteRow.id);

        this.deleteVisible = false;

        if (this.list.length === 1 && this.pagination.currentPage > 1) {
          this.pagination.currentPage--;
        }

        this.fetchList();
      } catch (e) {
        console.log(e);
        alert("删除失败");
      }
    },
    formatDateTime(datetime) {
      if (!datetime) return "";
      const d = new Date(datetime);
      const pad = (n) => n.toString().padStart(2, "0");
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(
        d.getDate()
      )} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    },
  },
};
</script>

<style scoped>
.filter-card {
  margin-bottom: 15px;
  text-align: center;
}

.table-card {
  margin-top: 15px;
}

/* 操作列样式优化 */
.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 移除操作列所有留白 */
:deep(.action-column) {
  padding: 0 !important;
}

:deep(.action-column .cell) {
  padding: 0 !important;
  margin: 0 !important;
}

:deep(.action-column .action-buttons) {
  padding: 0;
  margin: 0;
}

:deep(.action-column .el-button) {
  margin: 0;
}

/* 确保表格边框正常显示 */
:deep(.el-table) {
  border: 1px solid #ebeef5;
  border-right: none;
  border-bottom: none;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #fafafa;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  border-right: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-table td) {
  border-right: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-table__fixed-right::before) {
  background-color: transparent;
}

/* 操作列与前一列之间的间距调整 */
:deep(.el-table__fixed-right) {
  left: auto !important;
  right: 0;
}

:deep(.el-table__fixed-right-patch) {
  background-color: #f5f7fa;
}

/* 分页样式 */
.pagination-container {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
  text-align: right;
}

:deep(.el-pagination) {
  padding: 2px 0;
}

:deep(.el-pagination .el-select .el-input) {
  width: 100px;
}

/* 添加重置按钮样式 */
:deep(.el-button--warning) {
  background-color: #e6a23c;
  border-color: #e6a23c;
  color: white;
}

:deep(.el-button--warning:hover) {
  background-color: #ebb563;
  border-color: #ebb563;
}
</style>
