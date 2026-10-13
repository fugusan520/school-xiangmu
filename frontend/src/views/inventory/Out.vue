<template>
  <div class="inventory-container">
    <h1>出库管理</h1>
    <a-input-search
      placeholder="搜索商品名称"
      v-model="searchText"
      @search="handleSearch"
      style="width: 200px; margin-bottom: 20px"
    />
    <a-button type="primary" @click="showAddModal">新建出库记录</a-button>
    <a-table :columns="columns" :data-source="filteredData" rowKey="id">
      <template v-slot:name="{ text }">
        {{ text }}
      </template>
      <template v-slot:quantity="{ text }">
        {{ text }}
      </template>
      <template v-slot:time="{ text }">
        {{ text }}
      </template>
      <template v-slot:action="{ record }">
        <a @click="showEditModal(record)">编辑</a>
        <span style="margin: 0 8px">|</span>
        <a @click="handleDelete(record.id)">删除</a>
      </template>
    </a-table>
    <a-modal :title="isEditing ? '编辑出库记录' : '新建出库记录'" :visible="addModalVisible" @cancel="addModalVisible = false" @ok="isEditing ? handleEdit : handleAdd">
      <a-form :form="addForm" label-width="80px">
        <a-form-item label="商品名称">
          <a-input v-model="addForm.value.name" />
        </a-form-item>
        <a-form-item label="数量">
          <a-input-number v-model="addForm.value.quantity" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { FindAllInventory, InAndOut } from '@/api/inventory'

export default {
  name: 'InventoryOut',
  setup() {
    const data = ref([])
    const searchText = ref('')
    const filteredData = computed(() => {
      return data.value.filter(item => item.name.includes(searchText.value))
    })
    const columns = ref([
      { title: '商品名称', dataIndex: 'name', key: 'name' },
      { title: '数量', dataIndex: 'quantity', key: 'quantity' },
      { title: '出库时间', dataIndex: 'time', key: 'time' },
      { title: '操作', key: 'action', slots: { customRender: 'action' } }
    ])
    const addModalVisible = ref(false)
    const addForm = ref({ name: '', quantity: 0 })
    const isEditing = ref(false)
    const editingId = ref(null)

    const fetchData = async () => {
      try {
        const response = await FindAllInventory()
        data.value = response.data
      } catch (error) {
        console.error('获取出库数据失败:', error)
      }
    }

    onMounted(() => {
      fetchData()
    })

    const showAddModal = () => {
      addModalVisible.value = true
      isEditing.value = false
      addForm.value = { name: '', quantity: 0 }
    }

    const handleAdd = async () => {
      try {
        await InAndOut('out', addForm.value)
        await fetchData()
        addModalVisible.value = false
      } catch (error) {
        console.error('添加出库记录失败:', error)
      }
    }

    const showEditModal = (record) => {
      isEditing.value = true
      editingId.value = record.id
      addForm.value = { ...record }
      addModalVisible.value = true
    }

    const handleEdit = async () => {
      try {
        await InAndOut('out', { ...addForm.value, id: editingId.value })
        await fetchData()
        addModalVisible.value = false
        isEditing.value = false
      } catch (error) {
        console.error('编辑出库记录失败:', error)
      }
    }

    const handleDelete = async (id) => {
      try {
        // 需补充删除接口调用
        await fetchData()
      } catch (error) {
        console.error('删除出库记录失败:', error)
      }
    }

    const handleSearch = () => {
      // Trigger data filtering
      filteredData.value // This line triggers the computed property
    }

    return {
      data,
      columns,
      addModalVisible,
      addForm,
      showAddModal,
      handleAdd,
      searchText,
      filteredData,
      isEditing,
      showEditModal,
      handleEdit,
      handleDelete,
      handleSearch
    }
  }
};
</script>

<style scoped>
.inventory-container {
  padding: 20px;
}
</style>