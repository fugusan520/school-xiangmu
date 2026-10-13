<template>
  <div>
    <a-spin size="large" :spinning="spinning">
      <a-row :gutter="20">
        <!-- 新增快递柜卡片 -->
        <a-col :span="8" class="item">
          <a-card hoverable class="add-item" @click="visible = true">
            <a-icon type="plus" />
            添加快递柜
          </a-card>
        </a-col>

        <!-- 快递柜列表 -->
        <a-col
          :span="8"
          v-for="(item, index) in data"
          :key="index"
          class="item"
        >
          <a-card hoverable>
            <template slot="actions" class="ant-card-actions">
              <span>地理位置: {{ item.location }}</span>
              <span>
                <router-link
                  v-if="item.status === 0"
                  :to="'/cabinet/box/' + item.id"
                >
                  <a-icon type="bar-chart" /> 柜口管理
                </router-link>
                <span v-else style="color:#aaa">
                  <a-icon type="stop" /> 暂不可用
                </span>
              </span>
              <span>
                <a-select
                  size="small"
                  :value="item.status"
                  style="width: 170px"
                  @change="changeStatus(item, $event)"
                >
                  <a-select-option :value="0">
                    正常
                    <span style="color:#999;font-size:12px">（可投递）</span>
                  </a-select-option>
                  <a-select-option :value="1">
                    故障中
                    <span style="color:#999;font-size:12px">（暂停使用）</span>
                  </a-select-option>
                </a-select>
              </span>

              <span>
                <a-popconfirm
                  title="确定要删除该快递柜吗？"
                  ok-text="删除"
                  cancel-text="取消"
                  @confirm="remove(item.id)"
                >
                  <a-icon type="delete" style="color: red" />
                  删除
                </a-popconfirm>
              </span>
            </template>

            <a-card-meta
              :title="item.cabinetName || item.cabinetNo"
              :description="
                '柜号: ' + item.cabinetNo + ' | 总格数: ' + item.totalBoxes
              "
            >
              <img
                class="image"
                slot="avatar"
                :src="imgList[Math.floor(Math.random() * imgList.length)]"
                alt=""
              />
            </a-card-meta>
          </a-card>
        </a-col>
      </a-row>
    </a-spin>

    <!-- 新增快递柜弹窗 -->
    <a-modal
      title="新增快递柜"
      :visible="visible"
      @ok="submit"
      @cancel="visible = false"
    >
      <a-input
        v-model="form.cabinetNo"
        addon-before="柜号"
        placeholder="请输入柜号"
        style="width: 300px; margin-bottom: 10px"
      />
      <a-input
        v-model="form.cabinetName"
        addon-before="柜子名称"
        placeholder="请输入快递柜名称"
        style="width: 300px; margin-bottom: 10px"
      />
      <a-input
        v-model="form.location"
        addon-before="地理位置"
        placeholder="请输入地理位置"
        style="width: 300px; margin-bottom: 10px"
      />
      <a-form-item label="总格数" style="margin-bottom: 0;">
        <a-input-number
          v-model="form.totalBoxes"
          :min="1"
          :max="1000"
          placeholder="请输入快递柜总格数"
          style="width: 300px;"
        />
      </a-form-item>
    </a-modal>
  </div>
</template>

<script>
import {
  FindAllCabinet,
  SaveCabinet,
  deleteCabinet,
  UpdateCabinet,
} from "@/api/cabinet";

export default {
  name: "CabinetList",
  data() {
    return {
      visible: false,
      spinning: false,
      data: [],
      form: {
        id: "",
        cabinetNo: "",
        cabinetName: "",
        location: "",
        totalBoxes: 0,
        status: 0,
      },
      imgList: [
        require("../../assets/cabinet0.svg"),
        require("../../assets/cabinet1.svg"),
        require("../../assets/cabinet2.svg"),
      ],
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    // ✅ 修改状态
    changeStatus(item, status) {
      const payload = {
        ...item,
        status,
      };

      UpdateCabinet(payload).then((res) => {
        if (res.status) {
          this.$message.success("状态更新成功");
          item.status = status; // 直接更新本地，避免重新加载
        }
      });
    },

    // ✅ 删除
    remove(id) {
      deleteCabinet(id).then((res) => {
        if (res.status) {
          this.$message.success("删除成功");
          this.loadData();
        }
      });
    },
    loadData() {
      this.spinning = true;
      FindAllCabinet().then((res) => {
        if (res.status) this.data = res.data;
        setTimeout(() => (this.spinning = false), 600);
      });
    },
    submit() {
      SaveCabinet(this.form).then((res) => {
        if (res.status) this.$message.success("添加成功");
        this.visible = false;
        this.loadData();
      });
    },
  },
};
</script>

<style scoped>
.add-item {
  text-align: center;
  line-height: 120px;
  min-height: 120px;
  border: 1px dashed #91949c;
}

.add-item:hover {
  border: 1px dashed #5a84fd;
  color: #5a84fd;
}

.item {
  margin-bottom: 20px;
}

.image {
  width: 80px;
  height: 80px;
}
</style>
