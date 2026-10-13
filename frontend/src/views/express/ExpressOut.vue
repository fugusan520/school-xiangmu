<template>
  <div class="express-out-list">
    <!-- 查询 -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="快递单号">
          <el-input v-model="queryForm.expressNo" clearable />
        </el-form-item>

        <el-form-item label="出库类型">
          <el-select v-model="queryForm.outType" clearable placeholder="全部">
            <el-option label="用户取件" :value="1" />
            <el-option label="工作人员发货" :value="2" />
          </el-select>
        </el-form-item>

        <el-form-item label="出库角色">
          <el-select v-model="queryForm.outRole" clearable placeholder="全部">
            <el-option label="用户" :value="1" />
            <el-option label="工作人员" :value="2" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="fetchList">查询</el-button>
          <el-button type="success" @click="openAdd">出库</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table :data="list" stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="expressNo" label="快递单号" min-width="220" />

        <el-table-column label="出库类型" width="150" align="center">
          <template #default="{ row }">
            <el-tag v-if="row && row.outType === 1" type="success"
              >用户取件</el-tag
            >
            <el-tag v-else-if="row" type="info">工作人员发货</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="出库角色" width="120" align="center">
          <template #default="{ row }">
            <el-tag
              v-if="row"
              :type="row.outRole === 1 ? 'warning' : 'primary'"
            >
              {{ row.outRole === 1 ? "用户" : "工作人员" }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          prop="outUserEmail"
          label="操作人"
          min-width="180"
          align="center"
        />

        <el-table-column label="出库时间" width="180" align="center">
          <template #default="{ row }">
            {{ formatDateTime(row.outTime) }}
          </template>
        </el-table-column>

        <el-table-column
          prop="remark"
          label="备注"
          min-width="200"
          show-overflow-tooltip
        />
      </el-table>

      <el-pagination
        class="pagination"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        :current-page="pagination.currentPage"
        :page-size="pagination.pageSize"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next"
      />
    </el-card>

    <!-- 出库弹窗 -->
    <el-dialog title="快递出库" :visible.sync="addVisible" width="500px">
      <el-alert
        v-if="addFormError"
        :title="addFormError"
        type="warning"
        show-icon
        :closable="false"
        style="margin-bottom: 10px"
      />

      <el-form :model="addForm" label-width="100px">
        <el-form-item label="快递单号">
          <el-select v-model="addForm.expressId" placeholder="选择快递">
            <el-option
              v-for="e in expressOptions"
              :key="e.id"
              :label="e.expressNo"
              :value="e.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="出库类型">
          <el-tag v-if="addForm.bizType === 1" type="success">用户取件</el-tag>
          <el-tag v-else-if="addForm.bizType === 2" type="info"
            >工作人员发货</el-tag
          >
        </el-form-item>

        <el-form-item label="操作人">
          <el-select v-model="addForm.outUserId" placeholder="选择操作人">
            <el-option
              v-for="u in addForm.outRole === 2 ? courierOptions : userOptions"
              :key="u.id"
              :label="u.email"
              :value="u.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="验证码">
          <el-input type="text" v-model="addForm.verifyCode" />
        </el-form-item>

        <el-form-item label="备注">
          <el-input
            type="textarea"
            v-model="addForm.remark"
            placeholder="可选填写"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="addVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { fetchExpressOutList, saveExpressOut } from "@/api/expressOutApi";

import { findAllCanOutList } from "@/api/expressApi";
import { findAllUser, findAllCourier } from "@/api/admin";

export default {
  name: "ExpressOutList",
  data() {
    return {
      loading: false,
      list: [],
      addVisible: false,
      addFormError: "",

      expressOptions: [],
      userOptions: [],
      courierOptions: [],

      queryForm: {
        expressNo: "",
        outType: "",
        outRole: "",
      },

      addForm: {
        bizType: 1,
        verifyCode: "",
        expressId: "",
        outType: 1,
        outRole: 1,
        outUserId: "",
        remark: "",
      },

      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
      },
    };
  },
  watch: {
    "addForm.expressId"(val) {
      const selected = this.expressOptions.find((e) => e.id === val);
      if (selected) {
        this.addForm.bizType = selected.bizType; // 自动设置出库类型
        this.addForm.outRole = selected.bizType === 1 ? 1 : 2; // 根据类型设置操作角色
        this.addForm.outType = selected.bizType === 1 ? 1 : 2; 
        this.addForm.outUserId = ""; // 清空操作人
      } else {
        this.addForm.bizType = 1;
        this.addForm.outRole = 1;
        this.addForm.outType = 1;
        this.addForm.outUserId = "";
      }
    },
    "addForm.outType"(val) {
      // outType 决定 outRole
      this.addForm.outRole = val === 1 ? 1 : 2;

      // 类型切换时，清空已选操作人（非常重要）
      this.addForm.outUserId = "";
    },
  },
  created() {
    this.fetchList();
  },
  methods: {
    async fetchList() {
      this.loading = true;
      const params = {
        ...this.queryForm,
        pageNum: this.pagination.currentPage,
        pageSize: this.pagination.pageSize,
      };
      const res = await fetchExpressOutList(params);
      this.list = res.data.data || [];
      this.pagination.total = res.data.total || 0;
      this.loading = false;
    },
    resetQuery() {
      this.queryForm = { expressNo: "", outType: "", outRole: "" };
      this.fetchList();
    },
    handleCurrentChange(p) {
      this.pagination.currentPage = p;
      this.fetchList();
    },
    handleSizeChange(s) {
      this.pagination.pageSize = s;
      this.fetchList();
    },
    async openAdd() {
      this.addForm = {
        expressId: "",
        outType: 1,
        outRole: 1,
        outUserId: "",
        remark: "",
      };
      this.addFormError = "";
      this.addVisible = true;

      const expressRes = await findAllCanOutList();
      this.expressOptions = expressRes.data || [];

      const userRes = await findAllUser({ role: "ROLE_USER" });
      this.userOptions = userRes.data || [];

      const courierRes = await findAllCourier();
      this.courierOptions = courierRes.data || [];
    },
    async submitAdd() {
      if (!this.addForm.expressId) {
        this.addFormError = "请选择快递";
        return;
      }
      if (!this.addForm.outUserId) {
        this.addFormError = "请选择操作人";
        return;
      }
      if (!this.addForm.verifyCode) {
        this.addFormError = "请输入验证码";
        return;
      }

      try {
        const res = await saveExpressOut(this.addForm);

        // 兼容后端统一返回结构

        if (res.code === 200) {
          this.$message.success("出库成功");
          this.addVisible = false;
          this.fetchList();
        } else {
          // 业务失败（比如你这个 400）
          this.addVisible = false;
          this.fetchList();
        }
      } catch (e) {
        console.error(e);
        this.$message.error("接口调用失败，请稍后重试");
      }
    },
    formatDateTime(t) {
      if (!t) return "";
      return new Date(t).toLocaleString();
    },
  },
};
</script>

<style scoped>
.filter-card {
  margin-bottom: 15px;
}
.pagination {
  margin-top: 15px;
  text-align: right;
}
</style>
