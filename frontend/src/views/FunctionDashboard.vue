<template>
  <a-layout-content class="dashboard-container">
    <a-page-header title="系统功能总览" />
    <a-row :gutter="[24, 24]">
      <a-col 
        v-for="(item, index) in flattenedMenus" 
        :key="index"
        :xs="24" :sm="12" :md="8" :lg="6"
      >
        <a-card 
          hoverable
          @click="$router.push(item.path)"
          class="function-card"
        >
          <template #actions>
            <a-icon type="arrow-right" />
          </template>
          <a-card-meta :title="item.title" />
        </a-card>
      </a-col>
    </a-row>
  </a-layout-content>
</template>

<script>
export default {
  computed: {
    flattenedMenus() {
      const allItems = this.$options.menus
        .flatMap(mainMenu => mainMenu.children)
        .filter(item => 
          item.roles.some(role => 
            this.$store.state.user.details?.roles?.includes(role)
          )
        );
      return [...new Map(allItems.map(item => [item.path, item])).values()];
    }
  },
  menus: () => require('@/layout/Aside.vue').default.data().menus
};
</script>

<style scoped>
.dashboard-container {
  padding: 24px;
  background: #f0f2f5;
}
.function-card {
  cursor: pointer;
  transition: all 0.3s;
}
.function-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}
</style>