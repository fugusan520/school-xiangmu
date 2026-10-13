<template>
  <a-layout-header class="header" :style="{ background: '#fff', padding: 0 }">
    <a-tooltip placement="bottom">
      <template slot="title">
        <span>刷新页面</span>
      </template>
      <a-button
        @click="clickReload"
        class="reload-btn"
        icon="reload"
        type="link"
        >刷新</a-button
      >
    </a-tooltip>

    <a-tooltip placement="bottom" class="question-btn">
      <template slot="title">
        <span>About</span>
      </template>
      <a-button class="reload-btn" icon="question-circle" type="link" />
    </a-tooltip>

    <div class="header-right">
      <!-- 🔔 通知铃铛 -->
      <a-popover
        placement="bottomRight"
        trigger="click"
        @visibleChange="onNoticeVisible"
      >
        <template slot="content">
          <a-list
            :data-source="noticeList"
            :locale="{ emptyText: '暂无通知' }"
            size="small"
            class="notice-list"
          >
            <a-list-item v-for="item in noticeList" :key="item.id">
              <span :class="item.type === 0 ? 'notice-unread' : 'notice-read'">
                {{ item.description }}
              </span>
            </a-list-item>
          </a-list>
        </template>

        <a-badge :count="unreadCount" offset="[-2, 6]">
          <a-button type="link" class="bell-btn" icon="bell" />
        </a-badge>
      </a-popover>

      <!-- 👤 用户头像下拉 -->
      <a-dropdown>
        <a-button type="link" class="avatar-btn">
          <a-avatar
            :size="26"
            class="avatar"
            src="https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png"
          />
          <span class="avatar-btn-username">{{ details.email }}</span>
        </a-button>

        <a-menu slot="overlay">
          <a-menu-item>
            <router-link to="/security">
              <a-icon type="smile" /> 个人中心
            </router-link>
          </a-menu-item>
          <!-- <a-menu-item>
            <router-link to="/role">
              <a-icon type="setting" /> 权限设置
            </router-link>
          </a-menu-item> -->
          <a-menu-divider />
          <a-menu-item @click="handleLogout">
            <a-icon type="logout" /> 退出登录
          </a-menu-item>
        </a-menu>
      </a-dropdown>
    </div>
  </a-layout-header>
</template>

<script>
import { getNotice } from "@/api/admin";
export default {
  created() {
  },
  computed: {
    unreadCount() {
      return this.noticeList.filter((item) => item.type === 0).length;
    },
  },
  data() {
    return {
      noticeList: [],
      details: this.$store.state.user.details
        ? this.$store.state.user.details
        : {},
    };
  },
  methods: {
    // Popover 打开时加载通知
    async onNoticeVisible(visible) {
    if (!visible) return;

    // 每次点击铃铛，都重新拉通知
    await this.fetchNotices();

    // 打开即视为已读（前端效果）
    this.noticeList.forEach(item => {
      item.type = 1;
    });
    },
    async fetchNotices() {
      const res = await getNotice();
      this.noticeList = res.data || [];
      this.noticeLoaded = true;
    },
    clickReload() {
      this.$router.go(0);
    },
    handleLogout() {
      let that = this;
      this.$confirm({
        title: "你确定要注销你的登录信息吗?",
        content: "点击确定将删除你在网站保留的登录信息！",
        onOk() {
          that.$router.push("/login");
          that.$store.commit("user/userLogout");
        },
      });
    },
  },
};
</script>

<style scoped>
.reload-btn {
  font-size: 15px;
  margin-left: 10px;
  color: rgba(0, 0, 0, 0.65);
}

.question-btn {
  float: right;
  margin: 15px 12px 0 0;
}

.avatar-btn {
  float: right;
  font-size: 15px;
  color: rgba(0, 0, 0, 0.55);
  height: 64px;
}

.avatar-btn-main {
  display: flex;
  align-items: center;
}
.avatar-bell {
  font-size: 16px;
  margin-right: 12px;
  color: rgba(0, 0, 0, 0.65);
  cursor: pointer;
}
.avatar-btn-username {
  line-height: 62px;
  font-size: 14px;
}

.avatar {
  margin-right: 12px;
  margin-top: 19px;
}
.notice-btn {
  float: right;
  margin: 15px 6px 0 0;
  font-size: 16px;
  color: rgba(0, 0, 0, 0.65);
}

.notice-unread {
  font-weight: 600;
  color: #1890ff;
}

.notice-read {
  color: rgba(0, 0, 0, 0.45);
}

.notice-list {
  max-height: 280px;
  overflow-y: auto;
  width: 260px;
}

.header-right {
  float: right;
  display: flex;
  align-items: center;
  height: 64px;
}

.bell-btn {
  font-size: 16px;
  color: rgba(0, 0, 0, 0.65);
  margin-right: 8px;
}

.avatar-btn {
  display: flex;
  align-items: center;
  color: rgba(0, 0, 0, 0.65);
}

.avatar {
  margin-right: 8px;
}

.avatar-btn-username {
  font-size: 14px;
}
</style>
