<template>
  <div class="express-in-list">
    <!-- 查询 -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="快递单号">
          <el-input v-model="queryForm.expressNo" clearable />
        </el-form-item>

        <el-form-item label="快递柜名">
          <el-input v-model="queryForm.cabinetName" clearable />
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="queryForm.status" clearable placeholder="全部">
            <el-option label="未取件" :value="0" />
            <el-option label="已出库" :value="1" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="fetchList">查询</el-button>
          <el-button type="success" @click="openAdd">入库</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table :data="list" stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="expressNo" label="快递单号" min-width="220" />

        <el-table-column
          prop="cabinetName"
          label="快递柜名"
          width="200"
          align="center"
        />

        <el-table-column prop="boxNo" label="柜口" width="100" align="center" />

        <el-table-column
          prop="verifyCode"
          label="取件码"
          min-width="200"
          align="center"
        />

        <el-table-column label="入库类型" width="150" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.inType === 1" type="info">收件入库</el-tag>
            <el-tag v-else type="success">寄件投柜</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="150" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'warning' : 'success'">
              {{ row.status === 0 ? "未取件" : "已出库" }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="入库时间" width="180" align="center">
          <template #default="{ row }">
            {{ formatDateTime(row.inTime) }}
          </template>
        </el-table-column>

        <el-table-column
          prop="inEmail"
          label="入库人"
          min-width="150"
          align="center"
        />
        <el-table-column
          prop="outEmail"
          label="待取件人"
          min-width="150"
          align="center"
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

    <!-- 入库弹窗 -->
    <el-dialog title="快递入库" :visible.sync="addVisible" width="500px">
      <el-alert
        v-if="addFormError"
        :title="addFormError"
        type="warning"
        show-icon
        :closable="false"
        style="margin-bottom: 10px"
      />
      <el-form :model="addForm" label-width="100px" ref="addFormRef">
        <el-form-item label="快递单号" prop="expressId">
          <el-select v-model="addForm.expressId" placeholder="选择快递">
            <el-option
              v-for="item in expressOptions"
              :key="item.id"
              :label="item.expressNo"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="addForm.inType === 2 ? '选择工作人员' : '出库用户'"
          prop="outUserId"
        >
          <el-select
            v-model="addForm.outUserId"
            :placeholder="addForm.inType === 2 ? '选择工作人员' : '选择出库用户'"
          >
            <el-option
              v-for="u in addForm.inType === 2 ? courierOptions : userOptions"
              :key="u.id"
              :label="u.email"
              :value="u.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="快递柜">
          <el-tree
            :data="cabinetTree"
            node-key="label"
            :expand-on-click-node="false"
            @node-click="handleBoxSelect"
          />
        </el-form-item>

        <el-form-item label="入库类型">
          <el-tag type="info">
            {{ addForm.inType === 1 ? "收件入库" : "寄件投柜" }}
          </el-tag>
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
import {
  fetchExpressInList,
  saveExpressIn,
  deleteExpressIn,
} from "@/api/expressInApi";

import { findAllCanUseList } from "@/api/expressApi";
import { findAllUser, findAllCourier } from "@/api/admin";
import { findCanUseBoxList } from "@/api/cabinetBox";

export default {
  name: "ExpressInList",
  data() {
    return {
      addFormError: "",
      expressOptions: [], // 未入库快递
      userOptions: [], // 用户列表
      cabinetTree: [],
      loading: false,
      list: [],
      courierOptions: [],
      deleteVisible: false,
      deleteRow: null,
      addVisible: false,

      queryForm: {
        expressNo: "",
        cabinetName: "",
        status: "",
      },

      addForm: {
        expressId: "",
        cabinetId: "",
        outUserId: "",
        boxNo: "",
        inType: 1,
      },

      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0,
      },
    };
  },
  created() {
    this.fetchList();
  },
  watch: {
    "addForm.expressId"(val) {
      if (!val) return;

      const express = this.expressOptions.find((e) => e.id === val);
      if (!express) return;

      // 用 bizType 决定入库类型
      this.addForm.inType = express.bizType;

      // 切换快递后，清空依赖字段，防脏数据
      this.addForm.outUserId = "";
      this.addForm.cabinetId = "";
      this.addForm.boxNo = "";
    },

    // 你原来这个 watch 可以保留
    "addForm.inType"() {
      this.addForm.outUserId = "";
    },
  },
  methods: {
    async fetchList() {
      this.loading = true;
      const params = {
        ...this.queryForm,
        pageNum: this.pagination.currentPage,
        pageSize: this.pagination.pageSize,
      };
      const res = await fetchExpressInList(params);
      this.list = res.data.data || [];
      this.pagination.total = res.data.total || 0;
      this.loading = false;
    },
    resetQuery() {
      this.queryForm = { expressNo: "", cabinetName: "", status: "" };
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
    openAdd() {
      this.addForm = {
        expressId: "",
        outUserId: "",
        cabinetId: "",
        boxNo: "",
        inType: 1,
      };
      this.addVisible = true;
      this.loadAddData();
    },
    async loadAddData() {
      // 1️⃣ 未入库快递（status = 0）
      const expressRes = await findAllCanUseList();
      this.expressOptions = expressRes.data || [];

      // 2️⃣ 用户（比如 ROLE_USER / ROLE_ADMIN）
      const userRes = await findAllUser({ role: "ROLE_USER" });
      this.userOptions = userRes.data || [];

      // 3️⃣ 可用柜 & 柜口（Map 转 Tree）
      const cabinetRes = await findCanUseBoxList();
      this.cabinetTree = this.buildCabinetTree(cabinetRes.data || []);

      // 3️⃣ 工作人员
      const courierRes = await findAllCourier();
      this.courierOptions = courierRes.data || [];
    },

    buildCabinetTree(list) {
      return list.map((cabinet) => ({
        label: `${cabinet.cabinetName}（${cabinet.location}）`,
        cabinetId: cabinet.cabinetId,
        children: cabinet.boxes.map((box) => ({
          label: `柜口 ${box.boxNo}`,
          cabinetId: cabinet.cabinetId,
          boxNo: box.boxNo,
          isLeaf: true,
        })),
      }));
    },
    handleBoxSelect(node) {
      // 只允许点柜口
      if (!node.boxNo) return;

      this.addForm.cabinetId = node.cabinetId;
      this.addForm.boxNo = node.boxNo;
    },
    formatDateTime(t) {
      if (!t) return "";
      const d = new Date(t);
      const pad = (n) => n.toString().padStart(2, "0");
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(
        d.getDate()
      )} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    },

    async submitAdd() {
      if (!this.addForm.expressId) {
        this.addFormError = "请选择快递单号";
        return;
      }
      if (!this.addForm.outUserId) {
        this.addFormError =
          this.addForm.inType === 2 ? "请选择出库工作人员" : "请选择出库用户";
        return;
      }
      if (!this.addForm.cabinetId || !this.addForm.boxNo) {
        this.addFormError = "请选择快递柜及柜口";
        return;
      }
      try {
        const res = await saveExpressIn(this.addForm);

        // 兼容后端统一返回结构
        console.log("***************************code:" + res.code);
        if (res.code === 200) {
          this.$message.success("入库成功");
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
    openDelete(row) {
      this.deleteRow = row;
      this.deleteVisible = true;
    },
    async confirmDelete() {
      await deleteExpressIn(this.deleteRow.id);
      this.$message.success("删除成功");
      this.deleteVisible = false;
      this.fetchList();
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
