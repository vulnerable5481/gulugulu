<template>
  <div class="videoIndex">
    <!-- 导航栏 -->
    <HeaderBar :isFixedHeaderBar="true"></HeaderBar>
    <!-- 视频详情页主体 -->
    <div class="video-container">
      <div class="left-container">
        <div class="video-info-container">
          <div class="video-info-title">{{ videoInfo.title }}</div>
          <div class="video-info-detail-list">
            <div class="detail-list-broadcast">
              <i class="gulu-kaishi iconfont"></i>
              <span class="video-info-detail-list-text">1.9万</span>
            </div>
            <div class="detail-list-comments">
              <i class="gulu-danmu1 iconfont" style="font-size: 14px"></i>
              <span class="video-info-detail-list-text">5</span>
            </div>
            <div class="detail-list-date">2024-11-01 12:28:40</div>
            <div class="detail-list-tyle" v-if="videoInfo.type == 0">
              <i class="gulu-jinzhi iconfont" style="margin-left: 30px; color: red"></i>
              <span style="margin-left: 5px">未经作者授权，禁止转载</span>
            </div>
          </div>
        </div>
        <div class="gulu-player">
          <!-- 骨架屏 -->
          <div class="stone" v-if="isloading"></div>
          <!-- 视频实体 -->
          <video
            class="gl-media"
            ref="videoRef"
            :src="videoInfo.videoUrl"
            @click="changeVideoStatus"
            @canplay="finishLoading"
            @timeupdate="changeProgress"
          ></video>
          <div class="gl-controls-wrap">
            <div class="gl-controls">
              <div class="controls-top">
                <div class="gl-progress-wrap">
                  <div class="gl-progress">
                    <div class="gl-progress-buffer"></div>
                    <div class="gl-progress-current" :style="{ width: (currentTime / videoInfo.duration) * 100 + '%' }"></div>
                  </div>
                </div>
              </div>
              <div class="controls-left">
                <i class="gl-play iconfont" @click="changeVideoStatus" :class="isPaused ? 'gulu-bofang1' : 'gulu-zanting1'"></i>
                <div class="controls-left-time">{{ currentHMS + '&nbsp;/&nbsp;' + convertTime(videoInfo.duration) }}</div>
              </div>
              <div class="controls-right">
                <div class="gl-resolution">1080P&nbsp;&nbsp;高清</div>
                <div class="gl-speed">倍速</div>
                <i class="gl-volume gulu-shengyin_shiti iconfont"></i>
                <i class="gl-set gulu-shezhi iconfont"></i>
                <i class="gl-picture gulu-huazhonghua iconfont"></i>
                <i class="gl-full gulu-quanping iconfont"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="right-container"></div>
    </div>
  </div>
</template>

<script setup>
import { queryVideo } from '@/apis/videoApi/videoRequest';
import HeaderBar from '@/components/header/HeaderBar.vue';
import { convertTime } from '@/utils/GuluUtils';
import { computed, onBeforeUnmount, onMounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// 路由
const route = useRoute();
const router = useRouter();
// 视频 dom元素
const videoRef = ref();

// 视频ID
const videoId = computed(() => {
  return route.query.videoId;
});
// 视频是否正在加载
let isloading = ref(true);
// 节流计时器
let playTimer;
let pauseTimer;
// 视频当前播放时间
let currentTime = ref(0);
let currentHMS = computed(() => {
  return convertTime(currentTime.value);
});
// 是否暂停播放
let isPaused = ref(false);
// 视频实体
let videoInfo = reactive({});

// 修改视频状态
function changeVideoStatus() {
  isPaused.value = !isPaused.value;
  if (isPaused.value == false) {
    playVideo(); // 播放视频
  } else {
    pauseVideo(); // 暂停视频
  }
}

// 空格控制视频状态
function handleSpacebarKey(e) {
  if (e.keyCode === 32 || e.key === ' ') {
    e.preventDefault(); // 阻止空格向下滚动
    changeVideoStatus();
  }
}

// 更新进度条
function changeProgress() {
  currentTime.value = Math.round(videoRef.value.currentTime);
}

function playVideo() {
  if (pauseTimer) {
    clearTimeout(pauseTimer);
  }
  playTimer = setTimeout(() => {
    const video = videoRef.value;
    video.play();
  }, 50);
}
function pauseVideo() {
  if (playTimer) {
    clearTimeout(pauseTimer);
  }
  pauseTimer = setTimeout(() => {
    const video = videoRef.value;
    video.pause();
  }, 50);
}

// 视频加载完毕
function finishLoading() {
  // 视频加载完毕
  isloading.value = false;
  // 添加键盘监听事件
  window.addEventListener('keydown', handleSpacebarKey);
  // 视频自动播放
  playVideo();
}

// 初始化
function init() {
  // 获取视频详细信息
  queryVideo(videoId.value).then(({ data, code, msg }) => {
    if (code == 200) {
      Object.assign(videoInfo, data);
      console.log(videoInfo);
    } else {
      // 处理错误
      router.replace({
        name: 'notFound',
      });
    }
  });
}

onMounted(() => {
  // 初始化
  init();
});

onBeforeUnmount(() => {
  // 清除监听事件
  window.removeEventListener('keydown', handleSpacebarKey);
});
</script>

<style scoped>
.videoIndex {
  width: 100%;
  height: 1000px;
}
.video-container {
  display: flex;
  margin-top: 64px;
  padding: 0 200px;
  width: 100%;
  height: 100%;
}

.left-container {
  max-width: 900px;
  flex: 2.5;
}
.right-container {
  max-width: 400px;
  padding-left: 30px;
  flex: 1;
}

.video-info-container {
  width: 100%;
  height: 8%;
}

.video-info-title {
  max-width: 770px;
  margin-top: 20px;
  font-size: 20px;
  font-family: '';
  white-space: nowrap; /* 禁止换行 */
  overflow: hidden; /* 超出部分隐藏 */
  text-overflow: ellipsis; /* 显示省略号 */
}

.video-info-detail-list {
  display: flex;
  align-items: center;
  margin-top: 10px;
  margin-left: 15px;
  font-size: 13px;
  color: var(--GR3);
}

.detail-list-broadcast {
  display: flex;
  align-items: center;
}

.video-info-detail-list-text {
  margin-left: 4px;
  margin-right: 15px;
  font-size: 13px;
}

.detail-list-tyle {
  display: flex;
  align-items: center;
}

.gulu-player {
  position: relative;
  width: 778px;
  height: 422px;
}

.stone {
  position: absolute;
  z-index: 1000;
  width: 100%;
  height: 100%;
  background-color: black;
}

.gl-media {
  width: 100%;
  height: 100%;
  background-size: cover; /* 防止视频拉伸而变形  */
}

.gl-controls-wrap {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%; /* 根据需要调整宽度 */
  height: 15%; /* 根据需要调整高度 */
  padding: 0 25px;
}

.gl-controls {
  display: flex;
  position: relative;
  width: 100%;
  height: 100%;
  color: #fff;
}

.gl-play {
  font-size: 26px;
  transition: all 0.5s;
  cursor: pointer;
}

.controls-left-time {
  margin-left: 10px;
  font-size: 12px;
}

.gl-progress-wrap {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  border-radius: 1.5px;
  cursor: pointer;
}

.gl-progress {
  position: relative;
  width: 100%;
  height: 4px;
  background-color: hsla(0, 0%, 100%, 0.2);
}
.gl-progress:hover {
  height: 6px;
}

.gl-progress-buffer {
  position: absolute;
  z-index: 10;
  top: 0;
  left: 0;
  width: 30%;
  height: 100%;
  background-color: hsla(0, 0%, 100%, 0.3);
}
.gl-progress-current {
  position: absolute;
  z-index: 100;
  top: 0;
  left: 0;
  height: 100%;
  background-color: #00a1d6;
}

.controls-left {
  flex: 3;
  display: flex;
  align-items: center;
}

.controls-right {
  flex: 2;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.gl-resolution {
  cursor: pointer;
}
.gl-speed {
  cursor: pointer;
}
.gl-volume {
  font-size: 20px;
  cursor: pointer;
}
.gl-set {
  font-size: 20px;
  cursor: pointer;
}
.gl-picture {
  font-size: 20px;
  cursor: pointer;
}
.gl-full {
  font-size: 20px;
  cursor: pointer;
}
</style>
