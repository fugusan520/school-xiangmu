<template>
  <div class="login-box">
    <div>
      <div class="box-header">
        <div class="box-header-t">账号注册</div>
      </div>
      <div class="title">快递驿站管理系统 - 账号注册</div>

      <a-input
        v-model="form.email"
        size="large"
        style="margin-top: 10px"
        class="input"
        placeholder="* 用户名（必填）"
      >
        <a-icon slot="prefix" type="mail" />
      </a-input>

      <a-input-password
        v-model="form.password"
        size="large"
        class="input"
        placeholder="* 密码（必填）"
      >
        <a-icon slot="prefix" type="lock" />
      </a-input-password>

      <!-- 可选信息区域 -->
      <div class="optional-info">
        <div class="optional-title">完善个人信息（非必填）</div>

        <a-input
          v-model="form.name"
          size="large"
          class="input optional-input"
          placeholder="真实姓名"
        >
        </a-input>

        <a-select
          v-model="form.sex"
          size="large"
          class="input optional-input"
          placeholder="性别"
        >
          <a-select-option value="男">男</a-select-option>
          <a-select-option value="女">女</a-select-option>
        </a-select>

        <a-input-number
          v-model="form.age"
          size="large"
          class="input optional-input"
          style="width: 100%"
          :min="0"
          :max="100"
          placeholder="年龄"
        />

        <a-input
          v-model="form.sign"
          size="large"
          class="input optional-input"
          placeholder="个性签名"
        />
      </div>

      <!-- 新增：角色选择区域 -->
      <div class="role-selection" style="margin-bottom: 25px;">
        <div style="margin-bottom: 8px; color: #333; font-weight: 500;">
          选择角色类型：
        </div>
        <a-radio-group v-model="form.roles" size="large" style="width: 100%">
          <a-radio value="ROLE_USER" style="margin-right: 30px;">
            <span style="font-size: 14px;">普通用户</span>
          </a-radio>
          <a-radio value="ROLE_COURIER">
            <span style="font-size: 14px;">工作人员</span>
          </a-radio>
        </a-radio-group>
      </div>

      <a-button
        :loading="submitLoading"
        class="submit-btn"
        type="primary"
        @click="submitLogin"
      >
        注册提交
      </a-button>

      <div class="des">快递驿站管理系统 - Copyright 2022 Author</div>

      <a-collapse
        v-model:activeKey="activeKey"
        :bordered="false"
        :show-arrow="true"
      >
        <template #expandIcon="{ isActive }">
          <caret-right-outlined :rotate="isActive ? 90 : 0" />
        </template>
        <a-collapse-panel
          key="1"
          header="注册须知（点击查看）"
          :style="customStyle"
        >
          <p>密码长度不小于6位</p>
          <!-- 新增角色选择说明 -->
          <p style="margin-top: 8px;">
            <strong>角色说明：</strong>
            <br />• 普通用户：可以收发快递，查看物流信息 <br />•
            工作人员：可以管理快递收发，处理物流订单
          </p>
        </a-collapse-panel>
      </a-collapse>

      <router-link to="/login">返回登录页</router-link>
    </div>
  </div>
</template>

<script>
import { Init, IsInit } from "../api/admin";

export default {
  data() {
    return {
      spinning: true,
      sendLoading: false,
      submitType: "2", //1账号登录 2邮箱登录
      submitLoading: false,
      form: {
        email: "",
        password: "",
        roles: "ROLE_USER",
        name: "",
        sex: null,
        age: null,
        sign: "", // 默认选中普通用户
      },
      isActive: true,
      customStyle:
        "background: #f7f7f7;border-radius: 4px;margin-bottom: 24px;border: 0;overflow: hidden",
    };
  },

  mounted() {
    // 如果需要检查初始化状态，可以取消注释
    // IsInit().then((res) => {
    //   if (res.data) this.$router.push('/login')
    // })
  },

  methods: {
    submitLogin() {
      // 用户名必填
      if (!this.form.email) {
        this.$message.error("请输入用户名");
        return;
      }

      // 密码必填
      if (!this.form.password) {
        this.$message.error("请输入密码");
        return;
      }

      // 添加角色验证
      if (!this.form.roles) {
        this.$message.error("请选择角色类型");
        return;
      }

      if (this.checkEmail()) {
        // 确保密码长度符合要求
        if (this.form.password.length < 6) {
          this.$message.error("密码长度不能小于6位");
          return;
        }

        // 发送注册请求，form中包含roles字段
        Init(this.form)
          .then((res) => {
            if (res.status) {
              this.$message.success("注册成功");
              this.$router.push("/login");
            } else {
              this.$message.error(res.message || "注册失败");
            }
          })
          .catch((error) => {
            this.$message.error("注册失败：" + (error.message || "未知错误"));
          });
      }
    },

    checkEmail() {
      return true;
    },
  },
};
</script>

<style scoped>
body {
  background: #000000 !important;
}

.ant-tabs-bar {
  border-bottom: none !important;
}

.ant-btn-primary {
  border-color: #5a84fd;
}

.login-box {
  width: 380px; /* 稍微增加宽度以适应新内容 */
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.box-header {
  display: flex;
}

.box-header-t {
  font-weight: bolder;
  font-size: 30px;
  letter-spacing: 1px;
}

.logo {
  width: 44px;
  height: 44px;
  margin-right: 20px;
  margin-left: 43px;
}

.ant-tabs-nav {
  width: 350px;
}

.ant-tabs-ink-bar {
  left: 52px;
}

.ant-input-affix-wrapper .ant-input {
  font-size: 12px !important;
}

.title {
  color: #91949c;
  padding-top: 15px;
  padding-bottom: 35px;
  font-size: 13px;
  text-align: center;
}

.input {
  margin-bottom: 25px;
  font-size: 10px;
}

.code-btn {
  height: 40px;
  margin-left: 30px;
}

.submit-btn {
  letter-spacing: 2px;
  background: #5a84fd;
  width: 100%;
  height: 45px;
}

.des {
  padding-top: 25px;
  font-size: 13px;
  text-align: center;
  color: #91949c;
  letter-spacing: 2px;
  margin-bottom: 20px;
}

/* 新增：角色选择区域的样式优化 */
.role-selection {
  padding: 10px 15px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  background-color: #fafafa;
  transition: all 0.3s;
}

.role-selection:hover {
  border-color: #5a84fd;
  background-color: #fff;
}

.ant-radio-wrapper {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.ant-radio-wrapper:last-child {
  margin-bottom: 0;
}
/* ===== 可选信息整体容器 ===== */
.optional-info {
  padding: 12px 15px 5px;
  margin-bottom: 25px;
  border: 1px dashed #e6e6e6;
  border-radius: 8px;
  background-color: #fafafa;
  transition: all 0.3s ease;
}

.optional-info:hover {
  border-color: #5a84fd;
  background-color: #fff;
}

/* 标题 */
.optional-title {
  font-size: 13px;
  color: #888;
  margin-bottom: 12px;
}

/* 非必填输入统一风格 */
.optional-input {
  margin-bottom: 18px;
}

/* 统一 select 高度 */
.optional-input .ant-select-selector {
  height: 40px !important;
  align-items: center;
}

.optional-input .ant-select-selection-placeholder {
  line-height: 40px !important;
}

/* input-number 高度对齐 */
.optional-input.ant-input-number {
  height: 40px;
}

.optional-input.ant-input-number input {
  height: 38px;
  font-size: 12px;
}

/* 非必填字段整体弱化一点 */
.optional-input input {
  font-size: 12px;
}

/* placeholder 颜色稍浅，强调“可选” */
.optional-input input::placeholder {
  color: #bfbfbf;
}

/* ===== 修复 a-select placeholder 不显示问题 ===== */
.optional-input .ant-select-selection-placeholder {
  color: #bfbfbf !important;
  font-size: 12px;
  line-height: 40px;
}
.optional-input {
  width: 100%;
  margin-bottom: 18px;
}

</style>
