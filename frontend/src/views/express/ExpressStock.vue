<template>
  <div class="express-stock-list">
    <!-- 查询条件 -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="快递单号">
          <el-input v-model="queryForm.expressNo" clearable />
        </el-form-item>

        <!-- <el-form-item label="快递柜名">
          <el-input v-model="queryForm.cabinetName" clearable />
        </el-form-item> -->

        <el-form-item label="状态">
          <el-select v-model="queryForm.status" clearable placeholder="全部">
            <el-option label="在库" :value="0" />
            <el-option label="已取" :value="1" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="fetchList">查询</el-button>
          <el-button type="success" @click="exportStock">导出库存</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 库存表格 -->
    <el-card shadow="never">
      <el-table :data="list" stripe v-loading="loading" style="width: 100%">
        <el-table-column
          prop="expressNo"
          label="快递单号"
          min-width="220"
          show-overflow-tooltip
        />

        <el-table-column
          prop="cabinetName"
          label="快递柜名"
          min-width="160"
          align="center"
          show-overflow-tooltip
        />

        <el-table-column prop="boxNo" label="柜口" width="100" align="center" />

        <el-table-column label="状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'warning' : 'success'">
              {{ row.status === 0 ? "在库" : "已取" }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          label="入库时间"
          min-width="180"
          align="center"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            {{ formatDateTime(row.inTime) }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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
  </div>
</template>

<script>
import {
  fetchExpressStockList,
  exportExpressStock,
} from "@/api/expressStockApi";

import axios from "axios";

export default {
  name: "ExpressStockList",
  data() {
    return {
      loading: false,
      list: [],
      queryForm: {
        expressNo: "",
        // cabinetName: "",
        status: "",
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
  methods: {
    async fetchList() {
      this.loading = true;
      const params = {
        ...this.queryForm,
        pageNum: this.pagination.currentPage,
        pageSize: this.pagination.pageSize,
      };
      const res = await fetchExpressStockList(params);
      this.list = res.data.data || [];
      this.pagination.total = res.data.total || 0;
      this.loading = false;
    },
    resetQuery() {
      this.queryForm = { expressNo: "", status: "" };
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
    formatDateTime(t) {
      if (!t) return "";
      return new Date(t).toLocaleString();
    },

    async exportStock() {
      try {
        const params = { ...this.queryForm };
        const res = await axios.get(
          "http://localhost:8099/api/express/stock/export",
          {
            params,
            responseType: "blob",
          }
        );
        const blob = new Blob([res.data], { type: "application/vnd.ms-excel" });
        const url = URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.download = "快递库存.xlsx";
        link.click();
        URL.revokeObjectURL(url);
      } catch (err) {
        this.$message.error("导出失败");
      }
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
