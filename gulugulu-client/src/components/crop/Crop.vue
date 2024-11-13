<template>
  <div class="vidoe-crop-container">
    <el-dialog class="crop-dialog" v-model="cropedVisible" width="800" :close-on-click-modal="false">
      <div class="crop-title">
        <div class="crop-title-item" :class="!uploadVisible ? 'crop-title-item-active' : ''" @click="uploadVisible = fasle">
          <span>截取封面</span>
        </div>
        <div class="crop-title-item" :class="uploadVisible ? 'crop-title-item-active' : ''" @click="uploadVisible = true">
          <span>上传封面</span>
        </div>
        <div class="crop-title-close" @click="changeCropVisible">×</div>
      </div>
      <div class="crop-body">
        <div class="crop-left">
          <div class="crop-left-text">拖拽选框裁剪</div>
          <div class="crop-left-img-box">
            <img class="crop-left-img bgMaskImg" src="./image.png" alt="" />
            <img class="crop-left-img cutBoxImg" src="./image.png" alt="" />
          </div>
          <div class="crop-left-tips"></div>
        </div>
        <div class="crop-right"></div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="dialogVisible = false"> Confirm </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';

// emit
const emit = defineEmits(['']);
// Prop
const props = defineProps({
  cropedVisible: {
    type: Boolean,
  },
});
// 是否展示裁剪投稿封面
let cropedVisible = computed(() => {
  return props.cropedVisible;
});
// 是否切换到上传封面
let uploadVisible = ref(false);

// 关闭裁剪区域
const changeCropVisible = () => {
  emit('closeCrop');
};

//
</script>

<style scoped>
.vidoe-crop-container {
  position: absolute;
}

.crop-title {
  position: relative;
  display: flex;
  align-items: center;
  padding: 0 32px;
  height: 63px;
  border-bottom: 1px solid var(--GR2);
}

.crop-title-item {
  display: flex;
  align-items: center;
  margin: 0 12px;
  height: 100%;
  font-weight: 500;
  font-size: 16px;
  cursor: pointer;
}
.crop-title-item-active {
  color: var(--Bl1);
  border-bottom: 3px solid var(--Bl1);
}

.crop-title-close {
  display: flex;
  align-items: center;
  position: absolute;
  top: 0;
  right: 32px;
  height: 100%;
  font-size: 20px;
  cursor: pointer;
}

.crop-body {
  padding: 0 32px;
  width: 800px;
  height: 550px;
}

.crop-left {
  width: 530px;
}

.crop-left-text {
  padding-top: 5%;
  height: 15%;
  color: #18191c;
  font-family: PingFang SC;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: normal;
}

.crop-left-img-box {
  position: relative;
  padding: 16px;
  width: 530px;
  height: 330px;
  border-radius: 4px;
}

.crop-left-img {
  position: absolute;
  width: 397px;
  height: 297.75px;
}

.bgMaskImg {
  z-index: 1;
  opacity: 0.5;
}
.cutBoxImg {
  z-index: 10;
  clip-path: polygon(20% 20%, 80% 20%, 80% 80%, 20% 80%);
}

/* 修改elPlus样式 */
::v-deep .el-dialog__header {
  display: none;
}

::v-deep .crop-dialog {
  padding: 0;
}
</style>
