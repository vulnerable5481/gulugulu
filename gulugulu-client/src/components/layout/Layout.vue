<template>
  <div class="recommended-body">
    <!--  轮播图 -->
    <div class="recommended-carousel">
      <!-- 骨架屏 -->
      <div class="carousel-shim"></div>
      <!-- 轮播图主体 -->
      <div class="carousel-body">
        <div class="carousel-area" v-if="currentImg">
          <img class="carousel-img" style="width: 100%; height: 100%" :src="currentImg.url" alt="咕噜咕噜被玩坏了~~~~" />
        </div>
        <div class="carousel-footer">
          <div class="carousel-footer-text"></div>
          <div class="carsousel-footer-dots"></div>
        </div>
        <div class="arrows">
          <button @click="leftImg">
            <i class="gulu-left-arrow iconfont" style="color: #fff"></i>
          </button>
          <button @click="rightImg">
            <i class="gulu-right-arrow iconfont" style="color: #fff"></i>
          </button>
        </div>
      </div>
    </div>
    <!--  随机推荐,用v-for渲染-->
    <div class="feed_card" v-for="index in 11" :key="index">
      <transition name="skeleton">
        <div class="video-card">
          <!-- 骨架屏 -->
          <div class="video-card_skeleton" :class="loadingRandom ? 'loading_animation' : 'hide'">
            <div class="video-card_skeleton-cover"></div>
            <div class="video-card_skeleton-info">
              <p class="video-card__skeleton--text"></p>
              <p class="video-card__skeleton--text short"></p>
              <p class="video-card__skeleton--text light"></p>
            </div>
          </div>
          <!-- 实体内容 -->
          <div class="video-card__wrap" v-if="!loadingRandom">
            <div class="video-card_item">
              <!-- 视频封面 TODO: 视频预览怎么做？-->
              <a href="#视频地址">
                <picture class="video-card_thumbnail">
                  <img src="#视频封面" alt="#视频标题" />
                  <div class="video-card_views"></div>
                  <div class="video-card_comments"></div>
                  <div class="duration"></div>
                </picture>
              </a>
              <!-- 视频底部 -->
              <div class="vieo-card_footer">
                <div class="video-card_title"></div>
                <div :class="'isfollowed' ? 'followed' : 'hide'"></div>
                <div class="video-card_up"></div>
                <div class="video-card_date"></div>
              </div>
            </div>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';

// 是否加载完毕
let loadingRandom = ref(true);
// 图片集合
let imgs = reactive([]);
// 维护轮播图索引
let currentIndex = ref(0);
// 当前图片
const currentImg = computed(() => {
  return imgs[currentIndex.value];
});

// 模拟加载状态切换
setTimeout(() => {
  loadingRandom.value = false;
}, 5000);

//右移
function rightImg() {
  currentIndex.value++;
  if (currentIndex.value == 9) {
    currentIndex.value = 0;
  }
}
//左移
function leftImg() {
  currentIndex.value--;
  if (currentIndex.value == -1) {
    currentIndex.value = 8;
  }
}
function init() {
  // 初始化轮播图图片
  for (let i = 1; i <= 9; i++) {
    const img = {
      id: i,
      url: require(`@/assets/img/header-layout/img${i}.png`),
      color: `var(--Carimg${i})`,
    };
    imgs.push(img);
  }
}

onMounted(() => {
  init();
});
</script>

<style scoped>
.recommended-body {
  height: 2000px;
  display: grid;
  grid-template-columns: repeat(5, 263px);
  grid-template-rows: repeat(5, 223px);
  grid-auto-rows: 263px;
  grid-auto-columns: 223px;
  gap: 20px;
  position: relative;
  width: 100%;
}
.recommended-carousel {
  position: relative;
  /* 跨越从第1列到第3列，占据两个网格列的宽度，等价于 grid-column: span 2; */
  grid-column: 1/3;
  /* 跨越从第1行到第3行，占据两个网格行的高度 */
  grid-row: 1/3;
}
.carousel-body {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  /* 没有视觉效果的平移，它可以触发 GPU 加速，以提高性能和动画平滑度 */
  transform: translateZ(0);
  border-radius: 6px;
  overflow: hidden;
}

.carousel-img {
  transition: all 1s linear;
}

.arrows {
  position: absolute;
  bottom: 25%;
  right: 0;
  display: flex;
  justify-content: space-around;
  width: 15%;
}
.arrows button {
  width: 28px;
  height: 28px;
  border: 1px solid #fff;
  background-color: #554742;
  border-radius: 6px;
  opacity: 0.8;
}

.hide {
  display: none;
}
</style>
