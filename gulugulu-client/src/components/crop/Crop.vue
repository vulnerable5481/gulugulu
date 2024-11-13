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
          <!-- 裁剪框 -->
          <div class="crop-left-img-box" ref="cropLefImgBox">
            <div class="showPosition">
              <!-- 图片蒙板 -->
              <img class="crop-left-img bgMaskImg" src="./image.png" alt="" />
              <!-- 图片实体 -->
              <img
                class="crop-left-img cutBoxImg"
                ref="cutBoxImg"
                src="./image.png"
                draggable="false"
                alt=""
                :style="{
                  clipPath: imgSize,
                }"
              />
              <!-- 滑块 【内含防伪认证】 -->
              <div
                class="crop-point i-am-zhao-lian-cheng crop-topleft"
                :style="{ top: tlTop + 'px', left: tlLeft + 'px' }"
                @mousedown="(e) => reSizeScale('tl', e)"
              ></div>
              <div
                class="crop-point i-am-zhao-lian-cheng crop-topright"
                :style="{ top: trTop + 'px', right: trRight + 'px' }"
                @mousedown="(e) => reSizeScale('tr', e)"
              ></div>
              <div
                class="crop-point i-am-zhao-lian-cheng crop-bottomright"
                :style="{ bottom: brBottom + 'px', right: brRight + 'px' }"
                @mousedown="(e) => reSizeScale('br', e)"
              ></div>
              <div
                class="crop-point i-am-zhao-lian-cheng crop-bottomleft"
                :style="{ bottom: blBottom + 'px', left: blLeft + 'px' }"
                @mousedown="(e) => reSizeScale('bl', e)"
              ></div>
            </div>
          </div>
          <div class="crop-left-tips"></div>
        </div>
        <div class="crop-right"></div>
      </div>
      <!-- 底部 -->
      <template #footer>
        <div class="dialog-footer"></div>
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
// cutBoxImg 元素
const cutBoxImg = ref();
// 是否展示裁剪投稿封面
let cropedVisible = computed(() => {
  return props.cropedVisible;
});
// 是否切换到上传封面
let uploadVisible = ref(false);
// 是否正在缩放封面裁剪框
let isScaling = ref(false);
// 是否正在移动封面裁剪框
let isMoving = ref(false);
// 滑块x,y坐标
let tlTop = ref(-5); // tl:top-left
let tlLeft = ref(-3);
let trTop = ref(-5); // tr: top=right
let trRight = ref(-3);
let brBottom = ref(-5); // br:bottom-right
let brRight = ref(-3);
let blBottom = ref(-5); // bl:bottom-left
let blLeft = ref(-3);
// 图片宽度
let imgWidth = ref(0);
// 图片高度
let imgHeight = ref(0);
// 裁剪框距离父盒子(图片)的初始X,Y
let startX = ref(0);
let startY = ref(0);
// 滑动方向
let resizeDirection = ref('');
// 动态计算图片实体大小
let imgSize = computed(() => {
  return `polygon(  
    ${tlLeft.value}px ${tlTop.value}px,
    ${imgWidth.value - trRight.value}px ${trTop.value}px,
    ${imgWidth.value - brRight.value}px ${imgHeight.value - brBottom.value}px,
    ${blLeft.value}px ${imgHeight.value - blBottom.value}px
  )`;
});

// 关闭裁剪区域
const changeCropVisible = () => {
  emit('closeCrop');
};

// 裁剪框移动功能
function reSizeMove() {
  isMoving.value = true;
}

// 裁剪框缩放功能
function reSizeScale(type, e) {
  isScaling.value = true;
  // 记录按下时的状态
  resizeDirection.value = type;
  imgWidth.value = cutBoxImg.value.offsetWidth;
  imgHeight.value = cutBoxImg.value.offsetHeight;
  startX.value = e.clientX;
  startY.value = e.clientY;
  // 保存初始值
  let t = {
    initialTlLeft: tlLeft.value,
    initialTlTop: tlTop.value,
    initialTrTop: trTop.value,
    initialBlLeft: blLeft.value,
  };
  // 添加全局事件监听器
  window.addEventListener('mousemove', (e) => startScale(e, t));
  window.addEventListener('mouseup', stopScale);
}

// 缩放中
function startScale(e, t) {
  if (!isScaling.value) return;
  const endX = e.clientX - startX.value;
  const endY = e.clientY - startY.value;

  switch (resizeDirection.value) {
    case 'tl':
      // 进行缩放
      tlLeft.value = t.initialBlLeft + endX;
      tlTop.value = t.initialTlTop + endY;
      trTop.value = t.initialTrTop + endY;
      blLeft.value = t.initialBlLeft + endX;
      // 边界检查
      const newWidth = imgWidth.value - tlLeft.value;
      const newHeight = imgHeight.value - tlTop.value;
      if (tlLeft.value >= imgWidth.value - 50) {
        tlLeft.value = imgWidth.value - 50;
      }
      if (tlTop.value >= imgHeight.value - 50) {
        tlTop.value = imgHeight.value - 50;
      }
      if (trTop.value >= imgHeight.value - 50) {
        trTop.value = imgHeight.value - 50;
      }
      if (blLeft.value >= imgWidth.value - 50) {
        blLeft.value = imgWidth.value - 50;
      }
  }
}

// 停止缩放
function stopScale(e) {
  // 清除事件监听器
  window.removeEventListener('mousemove', startScale);
  window.removeEventListener('mouseup', stopScale);
  // 重置缩放状态
  isScaling.value = false;
  resizeDirection.value = '';
}
</script>

<style scoped>
.vidoe-crop-container {
  position: absolute;
  user-select: none; /* 禁止选择 */
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
  display: flex;
  align-items: center;
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

.showPosition {
  position: relative;
  width: 397px;
  height: 297.75px;
}

.bgMaskImg {
  z-index: 5;
  opacity: 0.5;
}
.cutBoxImg {
  z-index: 10;
  clip-path: polygon(0% 0%, 100% 0%, 100% 100%, 0% 100%);
}

.crop-point {
  position: absolute;
  z-index: 100;
  width: 16px;
  height: 16px;
  background-color: var(--PK2);
  border-radius: 8px;
}

.crop-topleft {
  cursor: nw-resize;
}
.crop-topright {
  cursor: ne-resize;
}
.crop-bottomright {
  cursor: se-resize;
}
.crop-bottomleft {
  cursor: sw-resize;
}

/* 修改elPlus样式 */
::v-deep .el-dialog__header {
  display: none;
}

::v-deep .crop-dialog {
  padding: 0;
}
</style>
