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
      <div class="video-card">
        <!-- 骨架屏 -->
        <div class="video-card_skeleton" v-if="loadingRandom">
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
            <!-- 视频主体 -->
            <div class="video-body">
              <picture class="video-card_thumbnail">
                <video
                  class="video-cover"
                  ref="videoRefs"
                  preload="metadata"
                  @mouseenter="playVideo(index)"
                  @mouseleave="pauseVideo(index)"
                >
                  <source :src="randomVideos[index - 1]?.url" type="video/mp4" />
                </video>
              </picture>
              <div class="video-card_views"></div>
              <div class="video-card_comments"></div>
              <div class="duration"></div>
            </div>
            <!-- 视频底部 -->
            <div class="video-card_footer">
              <div class="video-card_title">谭咏麟《爱情陷阱》86万众狂欢演唱会1984年Live全程记录</div>
              <div class="video-info">
                <div :class="'isfollowed' ? 'followed' : 'not-followed'">已关注</div>
                <div class="video-card_author video-card-font">迷路的森林狼</div>
                <div class="video-card_date video-card-font">·10.29</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { getRandomViews } from '@/apis/uploadApi/uploadRequest';
import { computed, onMounted, reactive, ref } from 'vue';

// 是否正在加载
let loadingRandom = ref(true);
// 随机视频
let randomVideos = reactive([]);
// 视频引用数组
let videoRefs = ref([]);
// 正在播放的视频
let currentPlayingVideo;
// 防抖计时器
let inTimer;
let outTimer;

// 图片集合
let imgs = reactive([]);
// 维护轮播图索引
let currentIndex = ref(0);
// 当前图片
const currentImg = computed(() => {
  return imgs[currentIndex.value];
});

// 右移
function rightImg() {
  currentIndex.value++;
  if (currentIndex.value == 9) {
    currentIndex.value = 0;
  }
}
// 左移
function leftImg() {
  currentIndex.value--;
  if (currentIndex.value == -1) {
    currentIndex.value = 8;
  }
}
// 初始化图片
function initCarousel() {
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

// 初始化随机视频
async function initRandomViews() {
  const response = await getRandomViews();
  if (response) {
    const { data } = response; // 确保 response 不是 null
    if (data.length <= 11) {
      // 如果服务器没有返回足够的视频数量，则不渲染前端页面，防止报Null错误
      return;
    }
    randomVideos.push(...data);
    loadingRandom.value = false;
  } else {
    loadingRandom.value = true;
    console.log('获取随机视频返回为空');
  }
}

// 播放视频;
function playVideo(index) {
  // 如果有正在播放的视频就重置播放
  if (currentPlayingVideo) {
    currentPlayingVideo = index;
  } else {
    pauseVideo(currentPlayingVideo);
  }
  clearTimeout(outTimer); //防抖
  inTimer = setTimeout(() => {
    index = index - 1;
    const videoElement = videoRefs.value[index];
    if (videoElement) {
      videoElement.muted = true; // 关闭声音
      videoElement.src = randomVideos[index].url; // 确保使用对应的视频 URL
      videoElement.load(); // 强制加载新视频
      videoElement.play();
    }
  }, 200);
}
// 停止视频;
function pauseVideo(index) {
  clearTimeout(inTimer); //防抖
  outTimer = setTimeout(() => {
    index = index - 1;
    const videoElement = videoRefs.value[index];
    if (videoElement) {
      videoElement.pause();
      videoElement.currentTime = 0; // 重置进度
    }
  }, 200); // 延迟500ms以上关闭视频,可防止那个讨人厌的报错
}

onMounted(async () => {
  initCarousel();
  // 等待初始化视频
  await initRandomViews();
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

.video-card__wrap {
  width: 100%;
  height: 100%;
}

.video-card_item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.video-body {
  position: relative;
  flex: 6.5;
  width: 100%;
  height: 100%;
}

.video-card_thumbnail {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1;
  width: 100%;
  height: 100%;
}

.video-cover {
  width: 100%;
  height: 100%;
  border-radius: 9px;
}

.video-card_footer {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 3.5;
  width: 100%;
  height: 100%;
}

.video-card_title {
  padding-right: 40px;
  display: -webkit-box;
  -webkit-box-orient: vertical; /* 设置为垂直盒子布局 */
  -webkit-line-clamp: 2; /* 限制显示的行数为2 */
  overflow: hidden; /* 隐藏超出部分 */
  height: 50px; /* 根据需要设置高度 */
  line-height: 25px; /* 设置行高 */
  transition: all 0.3s;
}

.video-card_title:hover {
  cursor: pointer;
  color: #15b5ee;
}

.video-info {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
  height: 30%;
  color: #adb1b6;
  transition: all 0.3s;
}

.video-info:hover {
  cursor: pointer;
  color: #15b5ee;
}

.followed {
  background-color: #fff0e3;
  color: #ff7f2a;
  border-radius: 4px;
}

.video-card_author {
  padding: 0 5px;
}
</style>
