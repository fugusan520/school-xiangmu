<template>
  <div class="express-feedback-list">
    <!-- 查询条件 -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="快递单号">
          <el-input
            v-model="queryForm.expressNo"
            clearable
            placeholder="请输入快递单号"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="fetchList">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="success" @click="openAddDialog">
            新增评价
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table :data="list" stripe v-loading="loading" style="width: 100%">
        <el-table-column
          prop="expressNo"
          label="快递单号"
          min-width="220"
          show-overflow-tooltip
        />

        <el-table-column
          prop="name"
          label="登记人"
          min-width="160"
          show-overflow-tooltip
        />

        <el-table-column
          prop="message"
          label="评价内容"
          min-width="280"
          show-overflow-tooltip
        />

        <!-- 评分 -->
        <el-table-column label="评分" width="120" align="center">
          <template slot-scope="scope">
            <el-rate v-model="scope.row.rating" disabled />
          </template>
        </el-table-column>

        <!-- 状态（可修改） -->
        <el-table-column label="状态" width="160" align="center">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.status"
              size="mini"
              @change="updateStatus(scope.row)"
            >
              <el-option label="待处理" :value="0" />
              <el-option label="已处理" :value="1" />
              <el-option label="已忽略" :value="2" />
            </el-select>
          </template>
        </el-table-column>

        <!-- 时间 -->
        <el-table-column label="评价时间" min-width="180" align="center">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdAt) }}
          </template>
        </el-table-column>

        <!-- 操作 -->
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button
              type="danger"
              size="mini"
              @click="handleDelete(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增评价弹窗 -->
    <el-dialog
      title="新增快递评价"
      :visible.sync="dialogVisible"
      width="520px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="addForm"
        :model="addForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="快递单号" prop="expressNo">
          <el-input v-model="addForm.expressNo" />
        </el-form-item>

        <!-- <el-form-item label="登记人" prop="name">
          <el-input v-model="addForm.name" />
        </el-form-item> -->

        <el-form-item label="评价内容" prop="message">
          <el-input type="textarea" :rows="3" v-model="addForm.message" />
        </el-form-item>

        <el-form-item label="评分" prop="rating">
          <el-rate v-model="addForm.rating" />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-select v-model="addForm.status" placeholder="请选择">
            <el-option label="待处理" :value="0" />
            <el-option label="已处理" :value="1" />
            <el-option label="已忽略" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>

      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdd">
          提交
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  FindAllExpressFeedback,
  SaveExpressFeedback,
  DeleteExpressFeedback,
  UpdateExpressFeedback
} from "@/api/expressFeedback";
import { MessageBox, Message } from "element-ui";

export default {
  name: "ExpressFeedbackList",
  data() {
    return {
      loading: false,
      list: [],
      dialogVisible: false,

      queryForm: {
        expressNo: "",
      },

      addForm: {
        expressNo: "",
        message: "",
        rating: 0,
        status: 0,
      },

      rules: {
        expressNo: [
          { required: true, message: "请输入快递单号", trigger: "blur" },
        ],
        message: [
          { required: true, message: "请输入评价内容", trigger: "blur" },
        ],
        rating: [{ required: true, message: "请选择评分", trigger: "change" }],
        status: [{ required: true, message: "请选择状态", trigger: "change" }],
      },
    };
  },

  created() {
    this.fetchList();
  },

  methods: {
    /* ================= 查询 ================= */
    async fetchList() {
      this.loading = true;
      try {
        console.log("111111111111111");
        const res = await FindAllExpressFeedback(this.queryForm);

        console.log("222222222222222");
        if (res.code !== 200) {
          this.$message.error(res.msg || "查询失败");
          return;
        }

        this.list = res.data || [];
      } catch (e) {
        this.$message.error("请求失败，请检查网络或后端");
      } finally {
        this.loading = false;
      }
    },

    resetQuery() {
      this.queryForm.expressNo = "";
      this.fetchList();
    },

    /* ================= 新增 ================= */
    openAddDialog() {
      this.dialogVisible = true;
      this.$nextTick(() => {
        this.$refs.addForm.resetFields();
      });
    },

    submitAdd() {
      this.$refs.addForm.validate(async (valid) => {
        if (!valid) return;

        try {
          const res = await SaveExpressFeedback(this.addForm);

          if (res.code !== 200) {
            // this.$message.error(res.msg || "新增失败");
            this.dialogVisible = false;
            this.fetchList();
            return;
          }

          this.$message.success("新增成功");
          this.dialogVisible = false;
          this.fetchList();
        } catch (e) {
          //   this.$message.error("新增失败");
        }
      });
    },

    /* ================= 状态修改 ================= */
    async updateStatus(row) {
      try {
        const res = await UpdateExpressFeedback(row);

        if (res.code !== 200) {
          //   this.$message.error(res.msg || "状态更新失败");
          return;
        }

        this.$message.success("状态更新成功");
      } catch (e) {
        // this.$message.error("状态更新失败");
      }
    },

    /* ================= 删除 ================= */
    handleDelete(id) {
      MessageBox.confirm("确认删除该评价吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          try {
            const res = await DeleteExpressFeedback(id);

            if (res.code !== 200) {
              //   this.$message.error(res.msg || "删除失败");
              return;
            }

            this.$message.success("删除成功");
            this.fetchList();
          } catch (e) {
            this.$message.error("删除失败");
          }
        })
        .catch(() => {});
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
.el-message-box__message {
  color: #333 !important;
}
</style>
