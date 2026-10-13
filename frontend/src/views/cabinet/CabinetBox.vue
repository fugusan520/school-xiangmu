<template>
  <div>
    <a-spin :spinning="loading">
      <!-- 信息提示 -->
      <a-alert
        v-if="form.cabinetId"
        type="info"
        show-icon
        :message="`当前快递柜 ID：${form.cabinetId}`"
        style="margin-bottom: 16px"
      />

      <a-row :gutter="20">
        <!-- 新增柜口卡片 -->
        <a-col v-if="form.cabinetId" :span="6" class="item">
          <a-card hoverable class="add-item" @click="openModal">
            <a-icon type="plus" /> 添加柜口
          </a-card>
        </a-col>

        <!-- 柜口列表 -->
        <a-col
          v-for="item in cabinetBoxList"
          :key="item.id"
          :span="6"
          class="item"
        >
          <a-card hoverable>
            <template slot="actions">
              <!-- 状态选择 -->
              <a-select
                v-model="item.status"
                size="small"
                :style="{ width: '150px' }"
                :disabled="item.status === STATUS.OCCUPIED"
                @change="handleStatusChange(item, $event)"
              >
                <a-select-option :value="STATUS.AVAILABLE"
                  >空闲（可用）</a-select-option
                >
                <a-select-option :value="STATUS.OCCUPIED"
                  >占用中</a-select-option
                >
                <a-select-option :value="STATUS.FAULT">故障</a-select-option>
              </a-select>

              <!-- 删除操作 -->
              <a-popconfirm
                title="确定删除该柜口？"
                :ok-text="'确定'"
                :cancel-text="'取消'"
                @confirm="handleDelete(item)"
              >
                <a-icon type="delete" style="color: #ff4d4f" />
              </a-popconfirm>
            </template>

            <div class="box-card-body">
              <!-- SVG 图片放在左边 -->
              <img
                :src="getBoxSvg(item.boxType)"
                :alt="getBoxTypeText(item.boxType)"
                class="box-svg"
              />
              <!-- 文字信息放在右边 -->
              <div class="box-info">
                <div class="box-title">柜口 {{ item.boxNo }}</div>
                <div class="box-desc">{{ getBoxTypeText(item.boxType) }}</div>
              </div>
              <div
                class="box-cabinet-info"
                v-if="item.cabinetName || item.cabinetName"
              >
                <div class="cabinet-name">
                  {{ item.cabinetName || item.cabinetName }}
                </div>
              </div>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </a-spin>

    <!-- 新增柜口弹窗 -->
    <a-modal
      title="新增柜口"
      :visible="modalVisible"
      :confirm-loading="submitting"
      @ok="handleSubmit"
      @cancel="closeModal"
      :mask-closable="false"
    >
      <!-- 模式选择 -->
      <a-radio-group v-model="batchMode" style="margin-bottom:16px">
        <a-radio :value="false">单个新增</a-radio>
        <a-radio :value="true">批量生成</a-radio>
      </a-radio-group>

      <!-- ========== 单个新增 ========== -->
      <a-form v-if="!batchMode" layout="vertical">
        <a-form-item label="柜口编号">
          <a-input v-model="formData.boxNo" placeholder="如 A01" />
        </a-form-item>

        <a-form-item label="柜口类型">
          <a-select v-model="formData.boxType" placeholder="请选择柜口类型">
            <a-select-option :value="1">小柜</a-select-option>
            <a-select-option :value="2">中柜</a-select-option>
            <a-select-option :value="3">大柜</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>

      <!-- ========== 批量生成 ========== -->
      <a-form v-else layout="vertical">
        <a-form-item label="柜口编号前缀">
          <a-input v-model="batchForm.prefix" placeholder="如 A / B / BOX" />
        </a-form-item>

        <a-form-item label="起始编号">
          <a-input-number
            v-model="batchForm.start"
            :min="1"
            style="width:100%"
          />
        </a-form-item>

        <a-form-item label="生成数量">
          <a-input-number
            v-model="batchForm.count"
            :min="1"
            :max="100"
            style="width:100%"
          />
        </a-form-item>

        <a-form-item label="柜口类型">
          <a-select v-model="batchForm.boxType" placeholder="请选择柜口类型">
            <a-select-option :value="1">小柜</a-select-option>
            <a-select-option :value="2">中柜</a-select-option>
            <a-select-option :value="3">大柜</a-select-option>
          </a-select>
        </a-form-item>

        <!-- 预览提示 -->
        <a-alert type="info" show-icon :message="previewText" />
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import {
  findBoxList,
  saveBox,
  deleteBox,
  updateBoxStatus,
  batchSave,
  findBoxList2,
} from "@/api/cabinetBox";

// 常量定义
const STATUS = {
  AVAILABLE: 0,
  OCCUPIED: 1,
  FAULT: 2,
};

const BOX_TYPES = [
  { value: 1, label: "小柜" },
  { value: 2, label: "中柜" },
  { value: 3, label: "大柜" },
];

export default {
  name: "CabinetBoxManagement",
  data() {
    return {
      batchMode: false,

      batchForm: {
        prefix: "",
        start: 1,
        count: 10,
        boxType: null,
      },
      loading: false,
      submitting: false,
      modalVisible: false,
      cabinetBoxList: [],
      formData: this.getInitialFormData(),
    };
  },
  computed: {
    previewText() {
      if (!this.batchForm.prefix || !this.batchForm.count) {
        return "请输入前缀和生成数量";
      }
      const arr = [];
      for (let i = 0; i < Math.min(this.batchForm.count, 5); i++) {
        arr.push(
          this.batchForm.prefix +
            String(this.batchForm.start + i).padStart(2, "0")
        );
      }
      return `将生成：${arr.join("、")}${
        this.batchForm.count > 5 ? " ..." : ""
      }`;
    },

    STATUS: () => STATUS,
    BOX_TYPES: () => BOX_TYPES,
    form() {
      return {
        cabinetId: this.$route.params.cabinetId,
      };
    },
  },
  mounted() {
    console.log("cabinetId =", this.$route.params.cabinetId);
    this.fetchData();
  },
  methods: {
    getBoxSvg(type) {
      switch (type) {
        case 1:
          return require("@/assets/small.svg");
        case 2:
          return require("@/assets/medium.svg");
        case 3:
          return require("@/assets/large.svg");
        default:
          return "";
      }
    },

    /**
     * 获取初始表单数据
     */
    getInitialFormData() {
      return {
        cabinetId: this.$route.params.cabinetId,
        boxNo: "",
        boxType: null,
      };
    },

    /**
     * 打开新增弹窗
     */
    openModal() {
      this.modalVisible = true;
    },

    /**
     * 关闭弹窗并重置表单
     */
    closeModal() {
      this.modalVisible = false;
      this.resetForm();
    },

    /**
     * 重置表单数据
     */
    resetForm() {
      this.formData = this.getInitialFormData();
    },

    /**
     * 获取柜口类型文本
     * @param {number} type - 柜口类型
     * @returns {string} 类型文本
     */
    getBoxTypeText(type) {
      const typeObj = BOX_TYPES.find((item) => item.value === type);
      return typeObj ? typeObj.label : "未知类型";
    },

    /**
     * 加载柜口列表数据
     */
    async fetchData() {
      try {
        this.loading = true;

        let response;
        // 如果 cabinetId 有值才传参数，否则直接调用不带参数的接口
        if (
          this.form.cabinetId != null &&
          this.form.cabinetId !== "" &&
          this.form.cabinetId !== "undefined"
        ) {
          response = await findBoxList(this.form.cabinetId);
        } else {
          console.log("hhhhhhhhhhhh");
          response = await findBoxList2(); // 后端接口允许不传参数返回所有柜口或列表
        }

        this.cabinetBoxList = response.data || [];
      } catch (error) {
        console.error("加载柜口列表失败:", error);
        this.$message.error("加载柜口列表失败，请稍后重试");
      } finally {
        this.loading = false;
      }
    },

    async handleSubmit() {
      if (this.batchMode) {
        await this.submitBatch();
      } else {
        await this.submitSingle();
      }
    },

    /**
     * 提交新增柜口表单
     */
    async submitSingle() {
      // 表单验证
      if (!this.validateForm()) {
        return;
      }

      try {
        this.submitting = true;
        const res = await saveBox(this.formData);
        if (res.code === 200) {
          this.$message.success("新增成功");
        }
        this.closeModal();
        await this.fetchData();
      } catch (error) {
        console.error("新增柜口失败:", error);
        this.$message.error(`新增失败: ${error.message || "请检查输入信息"}`);
      } finally {
        this.submitting = false;
      }
    },

    /**
     * 表单验证
     * @returns {boolean} 验证结果
     */
    validateForm() {
      if (!this.formData.boxNo.trim()) {
        this.$message.warning("请输入柜口编号");
        return false;
      }

      if (!this.formData.boxType) {
        this.$message.warning("请选择柜口类型");
        return false;
      }

      // 检查编号是否重复
      const isDuplicate = this.cabinetBoxList.some(
        (item) => item.boxNo === this.formData.boxNo
      );
      if (isDuplicate) {
        this.$message.warning("该柜口编号已存在，请使用其他编号");
        return false;
      }

      return true;
    },

    /**
     * 更新柜口状态
     * @param {Object} item - 柜口对象
     * @param {number} status - 新状态
     */
    async handleStatusChange(item, status) {
      try {
        const requestData = {
          id: item.id,
          boxNo: item.boxNo, 
          cabinetId: item.cabinetId,
          boxType:item.boxType,
          status: status,
        };
        await updateBoxStatus(requestData);
        this.$message.success("状态更新成功");
        // 更新本地数据，避免重新加载整个列表
        item.status = status;
      } catch (error) {
        console.error("更新状态失败:", error);
        this.$message.error("状态更新失败");
        // 恢复原状态
        this.$forceUpdate();
      }
    },

    /**
     * 删除柜口
     * @param {Object} item - 柜口对象
     */
    async handleDelete(item) {
      console.log("item:", item);
      // 前置检查
      if (item.status === STATUS.OCCUPIED) {
        this.$message.warning("占用中的柜口不能删除");
        return;
      }

      try {
        await deleteBox(item.id);
        this.$message.success("删除成功");
        // 移除本地数据，避免重新加载整个列表
        const index = this.cabinetBoxList.findIndex(
          (box) => box.id === item.id
        );
        if (index !== -1) {
          this.cabinetBoxList.splice(index, 1);
        }
      } catch (error) {
        console.error("删除柜口失败:", error);
        this.$message.error("删除失败，请稍后重试");
      }
    },
    async submitBatch() {
      const { prefix, start, count, boxType } = this.batchForm;

      if (!prefix || !count || !boxType) {
        this.$message.warning("请填写完整的批量生成信息");
        return;
      }

      const payload = [];
      for (let i = 0; i < count; i++) {
        payload.push({
          cabinetId: this.form.cabinetId,
          boxNo: prefix + String(start + i).padStart(2, "0"),
          boxType,
        });
      }

      try {
        this.submitting = true;
        const res = await batchSave(payload);
        if (res.code === 200) {
          this.$message.success(`成功生成 ${count} 个柜口`);
        }
        this.closeModal();
        await this.fetchData();
      } catch (e) {
        this.$message.error("批量生成失败");
      } finally {
        this.submitting = false;
      }
    },
  },
};
</script>

<style scoped>
.item {
  margin-bottom: 16px;
}

.add-item {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #1890ff;
}

.add-item:hover {
  border-color: #1890ff;
}

.item .ant-card {
  height: 140px;
  display: flex;
  flex-direction: column;
}
.item .ant-card-body {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.item .ant-card {
  display: flex;
  flex-direction: column;
}

.item .ant-card-body {
  flex: 1;
  padding-bottom: 40px; /* 给actions留出空间 */
}

.item .ant-card-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fafafa;
}
.box-card-body {
  display: flex;
  align-items: center;
  text-align: left;
  width: 100%;
}

.box-svg {
  width: 48px;
  height: 48px;
  margin-right: 12px; /* 图片和文字之间的间距 */
  flex-shrink: 0; /* 防止图片被压缩 */
}

.box-info {
  flex: 1;
  min-width: 0; /* 防止文本溢出 */
}

.box-title {
  font-weight: 600;
  font-size: 14px;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.box-desc {
  font-size: 12px;
  color: #999;
}

.add-item {
  height: 100px; /* 与其他卡片保持一致 */
  display: flex;
  flex-direction: row; /* 也改为横向 */
  align-items: center;
  justify-content: center;
}

.add-item .anticon-plus {
  margin-right: 8px;
}

/* 确保actions区域不会太拥挤 */
.item .ant-card-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 16px;
}

/* 调整选择器宽度 */
.item .ant-select {
  width: 140px !important; /* 缩小一些，适应横向布局 */
}
</style>
