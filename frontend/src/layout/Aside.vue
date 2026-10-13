<template>
  <a-layout-sider
      :style="{ overflow: 'auto', height: '100vh', position: 'fixed', left: 0, transition: 'width 0.3s ease' }"
      :collapsible="true"
      :collapsed="collapsed"
      :collapsed-width="40"
      @mouseenter="collapsed = false"
      @mouseleave="collapsed = true"
      width="220">
    <div class="logo" :style="{ textAlign: collapsed ? 'center' : 'left' }">
        <a-icon v-if="collapsed" type="menu-fold" style="fontSize: 20px; color: #fff;" />
        <span v-else>快递驿站管理系统</span>
      </div>
    <a-tooltip v-if="showTooltip && !collapsed" placement="right" :content="tooltipContent">
      <div style="position: absolute; left: 220px; top: 0; pointer-events: none;" />
    </a-tooltip>
    <a-menu
        v-if="!collapsed"
        theme="dark"
        mode="inline"
        :openKeys="openKeys"
        @openChange="handleOpenChange">
      <a-sub-menu v-for="(item, index) in filteredMenus" :key="index">
        <span slot="title">
          <a-icon v-if="!collapsed" :type="item.icon"/>
          <span v-if="!collapsed">{{ item.title }}</span>
        </span>
        <a-menu-item v-for="menu in item.children" :key="menu.title" v-if="!collapsed">
          <router-link :to="menu.path">
            {{ menu.title }}
          </router-link>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<script>
import { mapState } from 'vuex'
import { Tooltip } from 'ant-design-vue'

export default {
  components: {
    'a-tooltip': Tooltip
  },
  watch: {
    collapsed(newVal) {
      this.$emit('update:collapsed', newVal);
      if (newVal) {
        this.openKeys = [];
      }
    }
  },
  data() {
    return {
      collapsed: true,
      menus: [
        {
          title: '快递管理',
          icon: 'home',
          children: [
            {title: '信息管理', path: '/express/list', roles: ['ROLE_COURIER', 'ROLE_SUPER_ADMIN','ROLE_USER']},
            {title: '库存管理', path: '/express/stock', roles: [ 'ROLE_SUPER_ADMIN']},
          ]
        },
        {
          title: '出入库管理',
          icon: 'pay-circle',
          children: [
            {title: '出库管理', path: '/express/out', roles: ['ROLE_COURIER', 'ROLE_SUPER_ADMIN']},
            {title: '入库管理', path: '/express/in', roles: ['ROLE_COURIER', 'ROLE_SUPER_ADMIN']},
          ]
        },

        {
          title: '快递柜管理',
          icon: 'rocket',
          children: [
            {title: '快递柜管理', path: '/cabinet/list', roles: ['ROLE_COURIER', 'ROLE_SUPER_ADMIN']},
            {title: '柜口管理', path: '/cabinet/box', roles: ['ROLE_COURIER', 'ROLE_SUPER_ADMIN']}
          ]
        },

        {
          title: '用户中心',
          icon: 'car',
          children: [
            {title: '取件管理', path: '/express/out', roles: ['ROLE_USER', 'ROLE_SUPER_ADMIN']},
            {title: '寄件投放&取件码查询', path: '/express/in', roles: ['ROLE_USER', 'ROLE_SUPER_ADMIN']}
          ]
        },
        // {
        //   title: '销售管理',
        //   icon: 'pay-circle',        //   children: [
        //     {title: '销售开票', path: '/sale/create', roles: ['ROLE_SUPER_ADMIN']},
        //     {title: '销售记录', path: '/sale/record', roles: ['ROLE_SALE', 'ROLE_SUPER_ADMIN']},
        //   ]
        // },
        // {
        //   title: '配送管理',
        //   icon: 'car',
        //   children: [
        //     {title: '申请配送', path: '/delivery/create', roles: ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN']},
        //     {title: '配送列表', path: '/delivery/list', roles: ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN']},
        //   ]
        // },
        // {
        //   title: '运输管理',
        //   icon: 'rocket',
        //   children: [
        //     {title: '车辆资料', path: '/vehicle', roles: ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN']},
        //     {title: '驾驶员资料', path: '/driver', roles: ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN']},
        //   ]
        // },
        // {
        //   title: '图表分析',
        //   icon: 'line-chart',
        //   children: [
        //     {title: '入库分析', path: '/analyze/in', roles: ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN']},
        //     {title: '出库分析', path: '/analyze/out', roles: ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN']},
        //   ]
        // },
        {
          title: '系统管理',
          icon: 'tool',
          children: [
            {title: '个人信息设置', path: '/security', roles: ['ROLE_USER','ROLE_COURIER', 'ROLE_SUPER_ADMIN']},
            {title: '用户管理', path: '/admin', roles: ['ROLE_SUPER_ADMIN']},
            {title: '权限列表', path: '/role', roles: ['ROLE_SUPER_ADMIN']},
            {title: '系统公告', path: '/notice', roles: ['ROLE_USER','ROLE_COURIER', 'ROLE_SUPER_ADMIN']}
          ]
        },

        {
          title: '评价中心',
          icon: 'file',
          children: [
            {title: '评价管理', path: '/express/feedback', roles: ['ROLE_USER', 'ROLE_SUPER_ADMIN']},
          ]
        },
        // {
        //   title: '日志管理',
        //   icon: 'file',
        //   children: [
        //     {title: '登录日志', path: '/loginlog', roles: ['ROLE_SUPER_ADMIN']},
        //     {title: '操作日志', path: '/systemlog', roles: ['ROLE_SUPER_ADMIN']}
        //   ]
        // }
      ],
      openKeys: [], // 当前打开的子菜单
      showTooltip: false,
      tooltipContent: '欢迎使用快递驿站管理系统！'
    }
  },
  computed: {
    ...mapState({
      userDetails: state => state.user.details
    }),
    // 根据用户角色筛选菜单
    filteredMenus() {
      console.log('当前用户详情:', this.userDetails);
      if (!this.userDetails || !this.userDetails.roles) {
        return [];
      }

      const userRoles = this.userDetails.roles.split(';');
      console.log('当前用户角色:', userRoles);
      // 过滤有权限访问的菜单项
      const allowedMenus = this.menus.map(menu => {
        const allowedChildren = menu.children.filter(child => {
          // 检查用户是否有权限访问此菜单项
          return child.roles.some(role => userRoles.includes(role));
        });

        // 只返回有子菜单的主菜单
        if (allowedChildren.length > 0) {
          return {
            ...menu,
            children: allowedChildren
          };
        }
        return null;
      }).filter(menu => menu !== null);

      return allowedMenus;
    }
  },
  methods: {
    handleOpenChange(openKeys) {
      // 只保留最后一个打开的菜单
      this.openKeys = openKeys.length ? [openKeys[openKeys.length - 1]] : [];
    }
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

.ant-menu {
  letter-spacing: 1px;
}

.logo {
  font-size: 16px !important;
  margin: 0;
  padding: 10px 0 !important;
}

.ant-layout-sider-collapsed .ant-menu .ant-menu-submenu-title, 
.ant-layout-sider-collapsed .ant-menu .ant-menu-item {
  display: flex !important;
  justify-content: flex-start !important;
  align-items: center !important;
  padding: 0 8px !important;
  margin: 0 !important;
  width: 100% !important;
}

.ant-layout-sider-collapsed .ant-menu .ant-menu-item .anticon, 
.ant-layout-sider-collapsed .ant-menu .ant-menu-submenu-title .anticon {
  font-size: 18px;
  margin: 0 !important;
  display: block;
  width: 24px;
  height: 24px;
  line-height: 24px;
}
</style>
