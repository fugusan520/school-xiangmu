<template>
  <div class="container">
    <div style="display: flex; width: 400px; height: 350px; margin: auto;
                position: absolute;
	              top: 0;
	              left: 0;
	              right: 0;
	              bottom: 0;">
      <div style="flex: 1; background-color: #ffffff;">
        <div style="padding: 20px;">
          <a-tabs @change="tabClick" default-active-key="1" :tabBarStyle="{ textAlign: 'center' }">
            <a-tab-pane key="1" tab="密码登陆">
              <a-input
                  v-model="form.email"
                  size="large"
                  style="margin-top: 10px"
                  class="input"
                  placeholder="用户名">
                <a-icon slot="prefix" type="mail"/>
              </a-input>
              <a-input-password
                  v-model="form.password"
                  size="large"
                  class="input"
                  placeholder="密码">
                <a-icon slot="prefix" type="lock"/>
              </a-input-password>
            </a-tab-pane>
<!--            <a-tab-pane key="2" tab="验证码登陆" force-render>-->
<!--              <a-input-->
<!--                  v-model="form.email"-->
<!--                  size="large"-->
<!--                  style="margin-top: 10px"-->
<!--                  class="input"-->
<!--                  placeholder="邮箱">-->
<!--                <a-icon slot="prefix" type="mail"/>-->
<!--              </a-input>-->
<!--              <div style="display: flex">-->
<!--                <a-input-->
<!--                    v-model="form.code"-->
<!--                    size="large"-->
<!--                    class="input"-->
<!--                    placeholder="验证码">-->
<!--                  <a-icon slot="prefix" type="safety-certificate"/>-->
<!--                </a-input>-->
<!--                <a-button class="code-btn" :loading="sendLoading" :disabled="isButtonDisabled" @click="sendEmail">-->
<!--                  {{ buttonText }}-->
<!--                </a-button>-->
<!--&lt;!&ndash;                <a-button class="code-btn" :loading="sendLoading" @click="sendEmail">&ndash;&gt;-->
<!--&lt;!&ndash;                  获取验证码&ndash;&gt;-->
<!--&lt;!&ndash;                </a-button>&ndash;&gt;-->
<!--              </div>-->
<!--            </a-tab-pane>-->
          </a-tabs>
          <div style="margin-bottom: 20px;display: flex;justify-content: space-around;">
            <!-- <a-checkbox v-model="form.remember" style="display: inline;">自动登录</a-checkbox> -->
            <!-- <a-button type="link" to="/init">没有账号？点我注册 </a-button> -->
            <router-link to="/init">没有账号？点我注册</router-link>
          </div>
          <a-button :loading="submitLoading" class="submit-btn" type="primary" @click="submitLogin">
            确认登陆
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {AdminLogin, AdminSendEmail} from "@/api/admin";
import {IsInit} from "../api/admin";
import service from "../utils/request"
export default {
  data() {
    return {
      isButtonDisabled: false, // 控制按钮的禁用状态
      buttonText: '获取验证码', // 按钮的文本内容
      countdown: 60, // 倒计时的秒数
      timer: null, // 保存定时器的引用

      sendLoading: false,
      submitType: '1', //1账号登录 2邮箱登录
      submitLoading: false,
      form: {
        password: '',
        email: '',
        code: '',
        remember: false,
      },
    }
  },

  mounted() {
    IsInit().then((res) => {
      if (!res.data) this.$router.push('/init')
    })

    // 页面加载时检查 localStorage 中是否有倒计时的剩余时间
    const endTime = localStorage.getItem('countdownEndTime');
    if (endTime) {
      const remainingTime = Math.floor((new Date(endTime) - new Date()) / 1000);
      if (remainingTime > 0) {
        this.startCountdown(remainingTime); // 如果剩余时间大于0，继续倒计时
      } else {
        localStorage.removeItem('countdownEndTime'); // 如果时间过了，清除存储
      }
    }

  },

  methods: {
    sendEmail() {

      if (this.checkEmail()) {
        this.sendLoading = true
        var that = this;
        AdminSendEmail(this.form.email).then((res) => {
          console.log("数据1", res)
          if (res.data.status) {
            this.$message.success(res.data.msg)

            this.isButtonDisabled = true;
            // console.log("数据", res.data)
            this.buttonText = `${this.countdown} 秒后重新获取`;

            const now = new Date();
            const endTime = new Date(now.getTime() + this.countdown * 1000); // 计算结束时间
            localStorage.setItem('countdownEndTime', endTime); // 将结束时间保存到 localStorage

            this.startCountdown(this.countdown);


            // 模拟异步请求结束
            setTimeout(() => {
              this.sendLoading = false;
            }, 2000);




          } else {
            this.$message.error(res.data.msg)
          }
          that.sendLoading = false
        })

      }


    },
    resetButton() {
      clearInterval(this.timer); // 清除定时器
      localStorage.removeItem('countdownEndTime'); // 倒计时结束，清除存储
      this.countdown = 60; // 重置倒计时
      this.buttonText = '获取验证码'; // 恢复按钮文本
      this.isButtonDisabled = false; // 重新启用按钮
    },
    startCountdown(time) {

      if(time > 0){
        this.isButtonDisabled = true;
      }
      // console.log("数据", time)

      this.countdown = time;
      this.buttonText = `${this.countdown} 秒后重新获取`;

      this.timer = setInterval(() => {
        if (this.countdown > 1) {
          this.countdown--;
          this.buttonText = `${this.countdown} 秒后重新获取`;
        } else {
          this.resetButton();
        }
      }, 1000);
    },

    submitLogin() {
      if (this.checkEmail()) {
        let type = this.submitType === '1' ? "passwrod" : "email"
        AdminLogin(type, this.form).then((res) => {
          if (res.status) {
            //存入往LocalStore存入Token
            this.$store.commit('user/saveToken', res.data.token)
            this.$store.commit('user/saveLoginUser', res.data.admin)
            setTimeout(() => {
              this.$router.push("/express/list")
              this.submitLoading = false
            }, 1000)
            this.$message.success("登录成功")
          }
        })
      }
    },

    tabClick(key) {
      this.submitType = key
    },
    checkEmail() {
      return true
    },

  },
  beforeDestroy() {
    // 组件销毁前清除定时器
    if (this.timer) {
      clearInterval(this.timer);
    }
  }

}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  /*background-color: #3C537D;*/
  background-image: url("../assets/仓库1.jpg");
  background-size: 100% 100%;
  color: #222;
}

body {
  background: #000000 !important;
}

 .ant-input-affix-wrapper .ant-input {
  font-size: 12px !important;
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
</style>
