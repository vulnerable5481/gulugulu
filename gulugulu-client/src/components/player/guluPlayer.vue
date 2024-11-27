<template>
  <div class="gulu-player" ref="guluPlayer">
    <!-- 遮罩层 -->
    <div class="mask" v-if="isloading"></div>
    <!-- 视频实体 -->
    <video
      class="gl-media"
      ref="videoRef"
      loop
      :src="videoInfo.videoUrl"
      @click="changeVideoStatus"
      @canplay="finishLoading"
      @timeupdate="changeProgress"
    ></video>
    <div class="gl-controls-wrap" :style="{ height: isFullScreen ? '10%' : '13%' }">
      <div class="gl-controls">
        <div class="controls-top">
          <div class="gl-progress-wrap">
            <div class="gl-progress" ref="glProgress" @click="calcProcessLength">
              <div class="gl-progress-buffer"></div>
              <div class="gl-progress-current" :style="{ width: (currentTime / videoInfo.duration) * 100 + '%' }"></div>
            </div>
          </div>
        </div>
        <div class="controls-left" :class="isFullScreen ? 'controls-left-active' : ''">
          <i
            class="gl-play iconfont"
            @click="changeVideoStatus"
            :class="[isPaused ? 'gulu-bofang1' : 'gulu-zanting1', isFullScreen ? 'gl-fullScreen-play' : 'gl-common-paly']"
          ></i>
          <div :style="{ fontSize: isFullScreen ? '15px' : '13px' }" class="controls-left-time">
            {{ currentHMS + '&nbsp;/&nbsp;' + convertTime(videoInfo.duration) }}
          </div>
        </div>
        <div class="controls-center" :class="isFullScreen ? 'controls-center-active' : ''"></div>
        <div class="controls-right">
          <div class="gl-resolution" :style="{ fontSize: isFullScreen ? '16px' : '14px' }">1080P&nbsp;&nbsp;高清</div>
          <div class="gl-speed" :style="{ fontSize: isFullScreen ? '16px' : '14px' }">倍速</div>
          <i class="gl-volume gulu-shengyin_shiti" :class="isFullScreen ? 'gl-fullScreen-entry' : 'gl-common-entry'"></i>
          <i class="gl-set gulu-shezhi" :class="isFullScreen ? 'gl-fullScreen-entry' : 'gl-common-entry'"></i>
          <i class="gl-picture gulu-huazhonghua" :class="isFullScreen ? 'gl-fullScreen-entry' : 'gl-common-entry'"></i>
          <i class="gl-full gulu-quanping" :class="isFullScreen ? 'gl-fullScreen-entry' : 'gl-common-entry'" @click="changeFullScreen"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { convertTime } from '@/utils/GuluUtils';
import { computed, onBeforeUnmount, onMounted, reactive, ref } from 'vue';

// props
const props = defineProps({
  videoInfo: {},
});

// 播放器 dom元素
const guluPlayer = ref();
// 视频 dom元素
const videoRef = ref();
// 进度条 dom元素
const glProgress = ref();
// 视频是否正在加载
const isloading = ref(true);
// 视频是否暂停播放
const isPaused = ref(false);
// 是否处于全屏状态
const isFullScreen = ref(false);
// video.play()
let playPromise;
// 节流计时器
let playTimer;
let pauseTimer;
// 视频当前播放时间
const currentTime = ref(0);
const currentHMS = computed(() => {
  return convertTime(currentTime.value);
});
// 视频实体
const videoInfo = props.videoInfo;

// 修改视频状态
function changeVideoStatus() {
  isPaused.value = !isPaused.value;
  if (isPaused.value == false) {
    playVideo(); // 播放视频
  } else {
    pauseVideo(); // 暂停视频
  }
}

// 监听键盘按键触发对应事件
function handleKeyboard(e) {
  // 空格键 播放/暂停
  if (e.keyCode === 32 || e.key === ' ') {
    e.preventDefault(); // 阻止空格向下滚动
    changeVideoStatus();
  }

  // F键 控制全屏
  else if (e.keyCode === 70) {
    changeFullScreen();
  }

  // →键 快进5s
  else if (e.keyCode == 39) {
    e.preventDefault(); // 阻止→键水平滚动
    videoRef.value.currentTime += 5;
  }

  // ←键 回退5s
  else if (e.keyCode == 37) {
    e.preventDefault(); // 阻止→键水平滚动
    videoRef.value.currentTime -= 5;
  }

  // ↑键 音量+10
  else if (e.keyCode == 38) {
    e.preventDefault(); // 阻止↑键垂直滚动
    videoRef.value.volume = videoRef.value.volume + 0.1 > 1 ? 1 : videoRef.value.volume + 0.1;
  }

  // ↓键 音量-10
  else if (e.keyCode == 40) {
    e.preventDefault(); // 阻止↓键垂直滚动
    videoRef.value.volume = videoRef.value.volume - 0.1 < 0 ? 0 : videoRef.value.volume - 0.1;
  }
  // ↓键 音量-10
}

// 点击更新进度条
function calcProcessLength(e) {
  // 计算进度条更新后的位置
  const totalLength = glProgress.value.offsetWidth;
  const clickPosition = e.offsetX;
  const rate = Math.round((clickPosition / totalLength) * 100) / 100;
  // 更新视频
  videoRef.value.currentTime = videoInfo.duration * rate;
  // 更新进度条
  changeProgress();
}

// 更新进度条
function changeProgress() {
  try {
    currentTime.value = Math.round(videoRef.value.currentTime);
  } catch (error) {
    // 这里有个莫名其妙的错误，反正也没什么恶性影响，不管了
  }
}

// 控制全屏
function changeFullScreen() {
  const guluPlayerElement = guluPlayer.value;

  // 检查当前是否是全屏模式
  if (!document.fullscreenElement && !document.webkitFullscreenElement && !document.mozFullScreenElement && !document.msFullscreenElement) {
    // 进入全屏
    isFullScreen.value = true;
    // 如果当前不是全屏，进入全屏
    if (guluPlayerElement.requestFullscreen) {
      guluPlayerElement.requestFullscreen(); // 标准浏览器方法
    } else if (guluPlayerElement.mozRequestFullScreen) {
      guluPlayerElement.mozRequestFullScreen(); // Firefox
    } else if (guluPlayerElement.webkitRequestFullscreen) {
      guluPlayerElement.webkitRequestFullscreen(); // Chrome, Safari 和 Opera
    } else if (guluPlayerElement.msRequestFullscreen) {
      guluPlayerElement.msRequestFullscreen(); // IE/Edge
    }
  } else {
    // 退出全屏
    isFullScreen.value = false;
    // 如果当前是全屏，退出全屏
    if (document.exitFullscreen) {
      document.exitFullscreen(); // 标准浏览器方法
    } else if (document.mozCancelFullScreen) {
      document.mozCancelFullScreen(); // Firefox
    } else if (document.webkitExitFullscreen) {
      document.webkitExitFullscreen(); // Chrome, Safari 和 Opera
    } else if (document.msExitFullscreen) {
      document.msExitFullscreen(); // IE/Edge
    }
  }
}

// 播放视频
function playVideo() {
  if (pauseTimer) {
    clearTimeout(pauseTimer);
  }
  playTimer = setTimeout(() => {
    const video = videoRef.value;
    playPromise = video.play();
    if (playPromise) {
      playPromise.catch((error) => {
        console.log('视频播放失败:', error);
      });
    } else {
      console.log('没有获得playPromise');
    }
  }, 50);
}
// 暂停视频
function pauseVideo() {
  if (playTimer) {
    clearTimeout(pauseTimer);
  }
  pauseTimer = setTimeout(() => {
    if (playPromise) {
      playPromise
        .then(() => {
          const video = videoRef.value;
          video.pause();
        })
        .catch((error) => {
          console.log(error);
        });
    } else {
      console.log('playPromise不存在');
    }
  }, 50);
}

// 视频加载完毕
function finishLoading() {
  // 视频加载完毕
  isloading.value = false;
  // 添加键盘监听事件
  window.addEventListener('keydown', handleKeyboard);
  // 视频自动播放
  playVideo();
}

onBeforeUnmount(() => {
  // 清除监听事件
  window.removeEventListener('keydown', handleKeyboard);
});
</script>

<style scoped>
.gulu-player {
  position: relative;
  width: 778px;
  height: 422px;
}

.mask {
  position: absolute;
  z-index: 1000;
  width: 100%;
  height: 100%;
  background-color: black;
}

.gl-media {
  width: 100%;
  height: 100%;
  object-fit: cover;
  background-size: cover; /* 防止视频拉伸而变形  */
}

.gl-controls-wrap {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%; /* 根据需要调整宽度 */
  height: 13%; /* 根据需要调整高度 */
  padding: 0 10px;
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

.gl-fullScreen-play {
  font-size: 30px;
  cursor: pointer;
}
.gl-common-paly {
  font-size: 24px;
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

.controls-left-active {
  flex: 2;
}

.controls-center-active {
  flex: 3;
}

.controls-right {
  flex: 2;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.gl-resolution {
  font-size: 14px;
  cursor: pointer;
}

.gl-speed {
  font-size: 14px;
  cursor: pointer;
}

.gl-common-entry {
  font-family: 'iconfont' !important;
  font-size: 20px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
}

.gl-fullScreen-entry {
  font-size: 25px;
  font-family: 'iconfont' !important;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
}

/* 隐藏全屏后的样式 */
video::-webkit-media-controls {
  display: none; /* 隐藏所有浏览器默认控件 */
}
</style>
