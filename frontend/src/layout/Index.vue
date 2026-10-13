<template>
  <a-layout id="components-layout-demo-fixed-sider">
    <Aside @update:collapsed="handleCollapsedChange"/>
    <a-layout :style="{ marginLeft: collapsed ? '40px' : '220px', transition: 'margin-left 0.3s ease' }">
      <Header/>
      <Main/>
      <Footer/>
    </a-layout>
  </a-layout>
</template>

<script>
import Aside from "@/layout/Aside";
import Header from "@/layout/Header";
import Main from "@/layout/Main";
import Footer from "@/layout/Footer";

export default {
  components: {Aside, Header, Main, Footer},
  data() {
    return {
      collapsed: true
    }
  },
  methods: {
    handleCollapsedChange(value) {
      this.collapsed = value;
    }
  },
  mounted() {
    this.$message.success(
        '欢迎管理员 ' + this.$store.state.user.details.email,6,
    );
  }

}
</script>

<style scoped>
#components-layout-demo-fixed-sider .logo {
  padding: 10px 15px;
  height: 50px;
  font-size: 15px;
  margin: 16px;
  color: #ffffff;
  letter-spacing: 2px;
}

.logo img {
  width: 32px;
  height: 32px;
  margin-right: 5px;
}

.header {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
}

</style>
