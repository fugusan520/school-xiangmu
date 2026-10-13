<template>
  <div class="main">
    <div class="title">个人中心</div>

    <a-spin :spinning="spinning">
      <!-- 基本资料 -->
      <a-card title="基本资料" bordered>
        <a-form-model
          :model="admin"
          :label-col="{ span: 4 }"
          :wrapper-col="{ span: 10 }"
        >
          <a-form-model-item label="真实姓名">
            <a-input v-model="admin.name" placeholder="请输入真实姓名" />
          </a-form-model-item>

          <a-form-model-item label="性别">
            <a-select v-model="admin.sex" placeholder="请选择性别">
              <a-select-option value="男">男</a-select-option>
              <a-select-option value="女">女</a-select-option>
            </a-select>
          </a-form-model-item>

          <a-form-model-item label="年龄">
            <a-input-number
              v-model="admin.age"
              :min="1"
              :max="120"
              style="width: 100%"
            />
          </a-form-model-item>

          <a-form-model-item label="个性签名">
            <a-textarea
              v-model="admin.sign"
              :rows="3"
              placeholder="写点什么介绍下自己吧"
            />
          </a-form-model-item>

          <a-form-model-item :wrapper-col="{ offset: 4 }">
            <a-button type="primary" @click="submit">
              保存资料
            </a-button>
          </a-form-model-item>
        </a-form-model>
      </a-card>

      <a-divider />

      <!-- 账号信息 -->
      <a-card title="账号信息" bordered>
        <p><b>用户名：</b>{{ admin.email }}</p>
        <p>
          <b>密码：</b>
          {{ show ? admin.password : '********' }}
          <a @click="show = !show" style="margin-left: 10px">
            <a-icon :type="show ? 'eye-invisible' : 'eye'" />
          </a>
        </p>

        <a-button type="link" @click="passwordVisible = true">
          修改密码
        </a-button>
      </a-card>
    </a-spin>

    <!-- 修改密码弹窗 -->
    <a-modal
      title="修改密码"
      :visible="passwordVisible"
      @ok="submit"
      @cancel="passwordVisible = false"
    >
      <a-form-model :model="admin">
        <a-form-model-item label="新密码">
          <a-input-password v-model="admin.password" />
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
import { SaveAdmin } from "@/api/admin";

export default {
  name: "Me",

  data() {
    return {
      admin: { ...this.$store.state.user.details },
      show: false,
      passwordVisible: false,
      spinning: false
    };
  },

  mounted() {
    this.load();
  },

  methods: {
    load() {
      this.spinning = true;
      setTimeout(() => {
        this.spinning = false;
      }, 400);
    },

    submit() {
      SaveAdmin(this.admin).then(res => {
        if (res.status) {
          this.$message.success("个人信息更新成功");
          this.$store.commit("user/saveLoginUser", res.data);
          this.passwordVisible = false;
          this.load();
        }
      });
    }
  }
};
</script>

<style scoped>
.main {
  background: #fff;
  padding: 40px 60px;
}

.title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 24px;
}
</style>
