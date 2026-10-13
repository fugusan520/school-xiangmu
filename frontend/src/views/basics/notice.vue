<template>
    <div class="system-notice">
  
      <!-- 查询栏 -->
      <el-card shadow="never" class="filter-card">
        <el-form :inline="true" size="small">
  
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="loadData">
              查询
            </el-button>
  
            <el-button
              v-if="canOperate"
              type="success"
              icon="el-icon-plus"
              @click="openAddDialog"
            >
              新增公告
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
  
      <!-- 表格 -->
      <el-card shadow="never">
        <el-table
          :data="tableData"
          border
          size="small"
        >
          <el-table-column
            prop="createAt"
            label="创建时间"
            width="180"
            align="center"
          />
  
          <el-table-column
            prop="description"
            label="系统公告"
            show-overflow-tooltip
          />
  
          <el-table-column
            v-if="canOperate"
            label="操作"
            width="180"
            align="center"
          >
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                @click="openEditDialog(scope.row)"
              >
                编辑
              </el-button>
  
              <el-button
                type="text"
                size="small"
                style="color: red"
                @click="handleDelete(scope.row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
  
      <!-- 新增 / 编辑 弹窗 -->
      <el-dialog
        :title="dialogTitle"
        :visible.sync="dialogVisible"
        width="500px"
        destroy-on-close
      >
        <el-form
          ref="noticeForm"
          :model="formData"
          :rules="rules"
          label-width="80px"
        >
          <el-form-item label="内容" prop="description">
            <el-input
              type="textarea"
              v-model="formData.description"
              :rows="4"
              placeholder="请输入系统公告内容"
            />
          </el-form-item>
        </el-form>
  
        <span slot="footer">
          <el-button size="small" @click="dialogVisible = false">取消</el-button>
          <el-button
            size="small"
            type="primary"
            @click="submitForm"
          >
            确定
          </el-button>
        </span>
      </el-dialog>
  
    </div>
  </template>
  
  <script>
  import { getRoles } from "@/api/admin";
  import { MessageBox, Message } from "element-ui";
  import {
    listNotice,
    addNotice,
    updateNotice,
    deleteNotice
  } from "@/api/systemNotice";
  
  export default {
    name: "SystemNotice",
  
    data() {
      return {
        userRoles: [],
        tableData: [],
        queryForm: {
          keyword: ""
        },
        dialogVisible: false,
        dialogTitle: "",
        formData: {
          id: null,
          description: ""
        },
        rules: {
          description: [
            { required: true, message: "请输入公告内容", trigger: "blur" }
          ]
        }
      };
    },
  
    computed: {
      // 是否有操作权限
      canOperate() {
        return (
          this.userRoles.includes("ROLE_COURIER") ||
          this.userRoles.includes("ROLE_SUPER_ADMIN")
        );
      }
    },
  
    created() {
      this.initRoles();
      this.loadData();
    },
  
    methods: {
      // 初始化角色
      async initRoles() {
        try {
          const res = await getRoles();
          if (res && res.data.token) {
            this.userRoles = [res.data.token];
          } else {
            this.userRoles = [];
          }
        } catch (e) {
          console.error("获取角色失败", e);
          this.userRoles = [];
        }
      },
  
      // 查询公告
      async loadData() {
        const res = await listNotice(this.queryForm);
        this.tableData = res.data || [];
      },
  
      // 打开新增
      openAddDialog() {
        this.dialogTitle = "新增系统公告";
        this.formData = {
          id: null,
          description: ""
        };
        this.dialogVisible = true;
      },
  
      // 打开编辑
      openEditDialog(row) {
        this.dialogTitle = "编辑系统公告";
        this.formData = { ...row };
        this.dialogVisible = true;
      },
  
      // 提交新增 / 编辑
      submitForm() {
        this.$refs.noticeForm.validate(async valid => {
          if (!valid) return;
  
          if (this.formData.id) {
            await updateNotice(this.formData);
            this.$message.success("修改成功");
          } else {
            await addNotice(this.formData);
            this.$message.success("新增成功");
          }
  
          this.dialogVisible = false;
          this.loadData();
        });
      },
  
      // 删除
      handleDelete(row) {
        MessageBox.confirm("确认删除该公告吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(async () => {
          await deleteNotice(row.id);
          this.$message.success("删除成功");
          this.loadData();
        });
      }
    }
  };
  </script>
  
  <style scoped>
  .system-notice {
    padding: 16px;
  }
  
  .filter-card {
    margin-bottom: 10px;
  }
  </style>
  