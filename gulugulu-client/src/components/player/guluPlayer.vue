<template>
  <div class="gulu-player" ref="guluPlayer">
    <!-- 遮罩层 -->
    <div class="gl-mask" v-if="isloading"></div>
    <!-- 视频实体 -->
    <div class="danmu-wrap">
      <!-- 弹幕轨道 -->
      <div class="danmu_track" ref="tracksRef" v-for="index in tracks" :style="{ height: `${trackHeight}px` }"></div>
    </div>
    <video
      class="gl-media"
      ref="videoRef"
      loop
      :src="videoInfo.videoUrl"
      @dblclick="changeFullScreen"
      @click="changeVideoStatus(true)"
      @canplay="finishLoading"
      @seeked="handleSeedked"
      @timeupdate="updateTime"
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
            class="gl-play iconfont gl-play-active"
            @click="changeVideoStatus(true)"
            :style="{ fontSize: '25px' }"
            :class="[isPaused ? 'gulu-bofang1' : 'gulu-zanting1']"
          ></i>
          <div :style="{ fontSize: isFullScreen ? '15px' : '13px' }" class="controls-left-time">
            {{ currentHMS + '&nbsp;/&nbsp;' + convertTime(videoInfo.duration) }}
          </div>
        </div>
        <div class="controls-center" :class="isFullScreen ? 'controls-center-active' : ''">
          <div class="gl-video-sending-bar">
            <div class="gl-danmu-info">
              <i
                class="gulu-bofangqi-danmukai iconfont"
                :style="{ fontSize: '35px' }"
                :class="isDanmuOpen ? 'danmu-control-active' : 'danmu-control-dead'"
                v-if="isDanmuOpen"
                @click="changeDanmu"
              ></i>
              <i
                class="gulu-bofangqi-danmuguan iconfont"
                :style="{ fontSize: '35px' }"
                :class="isDanmuOpen ? 'danmu-control-active' : 'danmu-control-dead'"
                v-if="!isDanmuOpen"
                @click="changeDanmu"
              ></i>
              <div class="danmu-control-set">
                <i class="gulu-bofangqi-danmugundongkai iconfont gl-danmu-set" :style="{ fontSize: '35px' }"></i>
              </div>
            </div>
            <div class="gl-danmu-send-bar">
              <div class="gl-danmu-send-wrap">
                <div class="gl-danmu-color">
                  <i class="gulu-shequhuodong iconfont" style="font-size: 18px"></i>
                </div>
                <input class="gl-danmu-input" v-model="danmuContent" type="text" placeholder="发个友善的弹幕见证当下" />
                <button class="gl-danmu-send-btn" @click="handleSendDanmu">发送</button>
              </div>
            </div>
          </div>
        </div>
        <div class="controls-right">
          <div
            class="gl-resolution"
            :style="{ fontSize: isFullScreen ? '16px' : '14px' }"
            @mouseenter="openVideoCtr(3)"
            @mouseleave="closeVideoCtr(3)"
          >
            <span>1080P&nbsp;&nbsp;高清</span>
            <ul
              v-if="isOpenResolution"
              class="gl-resolution-menu"
              :class="isFullScreen ? 'gl-resolution-menu-big' : 'gl-resolution-menu-small'"
            >
              <li>1080P超清</li>
              <li>720P高清</li>
              <li>480P清晰</li>
              <li>360P流畅</li>
            </ul>
          </div>
          <div
            class="gl-speed"
            :style="{ fontSize: isFullScreen ? '16px' : '14px' }"
            @mouseenter="openVideoCtr(1)"
            @mouseleave="closeVideoCtr(1)"
          >
            <span class="gl-speed-font">倍速</span>
            <ul class="gl-speed-menu" v-if="isOpenSpeed" :class="isFullScreen ? 'gl-speed-menu-big' : 'gl-speed-menu-small'">
              <li @click="changeSpeed(2)">2.0x</li>
              <li @click="changeSpeed(1.5)">1.5x</li>
              <li @click="changeSpeed(1.25)">1.25x</li>
              <li @click="changeSpeed(1.0)">1.0x</li>
              <li @click="changeSpeed(0.75)">0.75x</li>
              <li @click="changeSpeed(0.5)">0.5x</li>
            </ul>
          </div>
          <div class="gl-voice" ref="glVoiceRef" @mouseenter="openVideoCtr(2)">
            <i class="gl-volume gulu-shengyin_shiti" :class="isFullScreen ? 'gl-fullScreen-entry' : 'gl-common-entry'"></i>
            <el-slider
              v-if="isOpenVolume"
              class="gl-volume-menu"
              :class="isFullScreen ? 'gl-volume-menu-big' : 'gl-volume-menu-small'"
              v-model="volume"
              @change="closeVolumeMenu"
              @input="changeVolume"
              @mousedown="deleteVolumeEnter"
              vertical
              height="100px"
            />
          </div>
          <i class="gl-set gulu-shezhi" :class="isFullScreen ? 'gl-fullScreen-entry' : 'gl-common-entry'"></i>
          <i class="gl-picture gulu-huazhonghua" :class="isFullScreen ? 'gl-fullScreen-entry' : 'gl-common-entry'"></i>
          <i class="gl-full gulu-quanping" :class="isFullScreen ? 'gl-fullScreen-entry' : 'gl-common-entry'" @click="changeFullScreen"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/store';
import { convertTime } from '@/utils/GuluUtils';
import { computed, nextTick, onBeforeUnmount, onMounted, reactive, ref, toRef, toRefs } from 'vue';

// props
const props = defineProps({
  videoInfo: {},
  danmuList: {},
  isDanmuOpen: {
    type: Boolean,
  },
});

// 播放器 dom元素
const guluPlayer = ref();
// 视频 dom元素
const videoRef = ref();
// 进度条 dom元素
const glProgress = ref();
// 弹幕轨道 dom元素
const tracksRef = ref();
// 音量设置界面 dom元素
const glVoiceRef = ref();

// 视频是否正在加载
const isloading = ref(true);
// 视频是否暂停播放
const isPaused = ref(false);
// 是否处于全屏状态
const isFullScreen = ref(false);
// 是否展示弹幕
const { isDanmuOpen } = toRefs(props);
// 视频设置相关
const isOpenSpeed = ref(false);
const isOpenVolume = ref(false);
const isOpenResolution = ref(false);
const volume = ref(0);
// 事件处理函数的引用    【专门为了解决ELmentUI 与 v-if的冲突 而这样设计的,有点不好评价，感觉不如我自己手写的方法好！】
const closeVolumeCtr = () => closeVideoCtr(2); // 封装成函数引用
// 用一个对象来存储每个视频控制界面的定时器
let videoInTimers = {
  speed: null,
  volume: null,
  resolution: null,
};
let videoOutTimers = {
  speed: null,
  volume: null,
  resolution: null,
};

// video.play()
let playPromise;
// 视频防抖计时器
let playTimer;
let pauseTimer;
// 视频当前播放时间
const currentTime = ref(0);
const currentHMS = computed(() => {
  return convertTime(currentTime.value);
});
// 视频实体
const videoInfo = props.videoInfo;
// 弹幕列表
const { danmuList } = toRefs(props);
// 弹幕轨道的高度
const trackHeight = 30; // 默认给30px吧
// 弹幕轨道数量
const tracks = ref(0);
let oldTracks = 0; // 该变量用于控制缩小全屏后，恢复弹幕轨道数量
// 弹幕索引
let dmIndex = 0;
// 要发送弹幕的内容
const danmuContent = ref('');

// 修改视频状态
function changeVideoStatus(ischanged) {
  if (ischanged) {
    isPaused.value = !isPaused.value;
  }
  if (isPaused.value == false) {
    // 播放视频
    playVideo();
    // 开始弹幕的滚动
    nextTick(() => {
      for (let i = 0; i < tracks.value; i++) {
        const childTrack = tracksRef.value[i].children;
        for (let j = 0; j < childTrack.length; j++) {
          childTrack[j].style.setProperty('animation-play-state', 'running');
        }
      }
    });
  } else {
    // 暂停视频
    pauseVideo();
    // 停止弹幕的滚动
    nextTick(() => {
      for (let i = 0; i < tracks.value; i++) {
        const childTrack = tracksRef.value[i].children;
        for (let j = 0; j < childTrack.length; j++) {
          childTrack[j].style.setProperty('animation-play-state', 'paused');
        }
      }
    });
  }
}

// 监听键盘按键触发对应事件  【有一个疑问 这些键盘字符对应的数字是依据什么？答案：Ascii码值!】
function handleKeyboard(e) {
  // 空格键 播放/暂停
  if (e.keyCode === 32 || e.key === ' ') {
    e.preventDefault(); // 阻止空格向下滚动
    changeVideoStatus(true);
  }

  // F键 控制全屏
  else if (e.keyCode === 70) {
    changeFullScreen();
  }

  // →键 快进5s
  else if (e.keyCode == 39) {
    e.preventDefault(); // 阻止→键水平滚动
    // 判断是否修改左下角视频状态图标
    changeVideoStatus(isPaused.value);
    // 清除当前屏幕中的弹幕
    clearDanmu();
    videoRef.value.currentTime += 5;
  }

  // ←键 回退5s
  else if (e.keyCode == 37) {
    e.preventDefault(); // 阻止→键水平滚动
    // 判断是否修改左下角视频状态图标
    changeVideoStatus(isPaused.value);
    // 清除当前屏幕中的弹幕
    clearDanmu();
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
}

/**
 * 展示视频设置
 * @param  type 1:倍速控制;2:音量控制;3:视频清晰度控制;4:画中画控制;5:设置开关
 */
function openVideoCtr(type) {
  switch (type) {
    case 1:
      // 倍速控制
      if (videoOutTimers.speed) {
        clearTimeout(videoOutTimers.speed);
      }
      videoInTimers.speed = setTimeout(() => {
        isOpenSpeed.value = true;
      }, 200);
      break;
    case 2:
      // 音量控制
      if (videoOutTimers.volume) {
        clearTimeout(videoOutTimers.volume);
      }
      videoInTimers.volume = setTimeout(() => {
        isOpenVolume.value = true;
      }, 200);
      break;
    case 3:
      // 视频清晰度控制
      if (videoOutTimers.resolution) {
        clearTimeout(videoOutTimers.resolution);
      }
      videoOutTimers.resolution = setTimeout(() => {
        isOpenResolution.value = true;
      }, 200);
      break;
    case 4:
      // 画中画控制
      break;

    case 5:
      // 设置开关控制
      break;

    default:
      break;
  }
}
/**
 * 关闭视频设置
 * @param  type 1:倍速控制;2:音量控制;3:视频清晰度控制;4:画中画控制;5:设置开关
 */
function closeVideoCtr(type) {
  switch (type) {
    case 1:
      // 倍速控制
      if (videoInTimers.speed) {
        clearTimeout(videoInTimers.speed);
      }
      videoOutTimers.speed = setTimeout(() => {
        isOpenSpeed.value = false;
      }, 200);
      break;
    case 2:
      // 音量控制
      if (videoInTimers.volume) {
        clearTimeout(videoInTimers.volume);
      }
      videoOutTimers.volume = setTimeout(() => {
        isOpenVolume.value = false;
      }, 200);
      break;
    case 3:
      // 视频清晰度控制
      if (videoInTimers.resolution) {
        clearTimeout(videoInTimers.resolution);
      }
      videoOutTimers.resolution = setTimeout(() => {
        isOpenResolution.value = false;
      }, 200);
      break;
    case 4:
      // 画中画控制
      break;

    case 5:
      // 设置开关控制
      break;

    default:
      break;
  }
}

/**
 * 控制视频倍速
 * @param  speed 视频倍速
 */
function changeSpeed(speed) {
  videoRef.value.playbackRate = speed;
  console.log(videoRef.value.playbackRate);
}

// 控制音量大小
function changeVolume() {
  videoRef.value.volume = volume.value / 100;
}
// 关闭音量设置界面
function closeVolumeMenu() {
  // 恢复音量图标自动关闭
  glVoiceRef.value.addEventListener('mouseleave', closeVolumeCtr);
  closeVideoCtr(2);
}
// 清除音量图标自动关闭事件
function deleteVolumeEnter() {
  if (glVoiceRef.value) {
    glVoiceRef.value.removeEventListener('mouseleave', closeVolumeCtr);
  }
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
  // 更新视频状态 【只要点击进度条则一定处于播放状态】
  isPaused.value = false;
}

// 更新进度条
function changeProgress() {
  try {
    currentTime.value = Math.round(videoRef.value.currentTime);
  } catch (error) {
    // 这里有个莫名其妙的错误，反正也没什么恶性影响，不管了
  }
}

// 实时更新视频进度
function updateTime() {
  // 更新进度条
  changeProgress();
  // 更新弹幕
  if (isDanmuOpen.value && !isPaused.value) {
    displayDanmu();
  }
}

// 加载当前时间点的弹幕  【轮询时间就用更新视频的时间好了,大概300~500ms一次】
function displayDanmu() {
  const curTime = videoRef.value?.currentTime;
  // 如果弹幕处于当前时间区间，则加载该弹幕
  if (dmIndex < danmuList.value.length && danmuList.value[dmIndex]?.time <= curTime) {
    const curDanmu = danmuList.value[dmIndex++];
    // console.log(curDanmu.content);
    // 初始化弹幕样式
    const danmuElement = document.createElement('div');
    danmuElement.classList.add('danmu-item'); // 如果style scoped 就无效!
    danmuElement.innerText = curDanmu.content;
    danmuElement.style.setProperty('color', `#${curDanmu.color}`);
    danmuElement.style.setProperty('--video-width', `${tracksRef.value[0].clientWidth}px`);
    // 本来我是想让每一个弹幕自己隐藏起来，但是直接隐藏轨道不更简单吗
    // danmuElement.style.setProperty('visibility', isDanmuOpen.value ? 'visible' : 'hidden');
    // 标识自己发出的弹幕  【使用青色方框标识】
    if (curDanmu.userId == useUserStore().userInfo.userId) {
      danmuElement.style.setProperty('border', '2px solid #307A7A');
    }

    let targetTrack = -1;
    // 寻找空闲轨道
    for (let i = 0; i < tracks.value; i++) {
      // 如果当前轨道没有弹幕则直接添加到该轨道
      if (tracksRef.value[i].children.length < 1) {
        targetTrack = i;
        break;
      }
      // 计算轨道是否空闲
      const childTrack = tracksRef.value[i].children;
      const lastDanmu = childTrack[childTrack.length - 1]; // 当前轨道的最后一个弹幕
      const trackWidth = tracksRef.value[0].clientWidth; // 轨道长度
      const danmuWidth = lastDanmu.clientWidth; // 弹幕长度
      // 弹幕距离左边的距离 (注意这里有个坑：此处计算的距离其实是dom元素被创建时距离左边的距离，没有考虑到动画的translateX())
      // const leftDistance = lastDanmu.offsetLeft;
      const leftDistance = lastDanmu.getBoundingClientRect().left; // 弹幕距离左边的距离 (这个方法考虑到了动画,translateX等因素的影响)
      const offset = trackWidth - (leftDistance + danmuWidth); // 弹幕右偏移量
      if (offset >= 100) {
        targetTrack = i;
        break;
      }
    }

    // 抛弃/放入等待队列
    if (targetTrack == -1) {
      return; // 暂时直接抛弃
    }

    // 添加到轨道
    tracksRef.value[targetTrack].appendChild(danmuElement);

    // 滚动弹幕
    if (curDanmu.type == 1) {
      danmuElement.classList.add('roll'); // 打标记

      const danmuWidth = danmuElement.offsetWidth; // 获取弹幕的宽度
      const trackWidth = tracksRef.value[0].offsetWidth; // 获取轨道的宽度(每个轨道的宽度都是一样的)

      // 设置动画实现滚动
      // danmuElement.style.setProperty('animation-duration', `10s`); // 这个可以动态计算，全屏非全屏分类
      // danmuElement.style.setProperty('--video-width', `${trackWidth}px`);

      // 通过translateX实现滚动
      // danmuElement.style.setProperty('transform', `translateX(calc(-1 * var(--video-width)))`);
    } else if (curDanmu.type == 2) {
      // 顶部弹幕
    } else {
      // 底部弹幕
    }
    // 动画结束就移除弹幕
    danmuElement.addEventListener('animationend', () => {
      danmuElement.remove();
    });
  }
}

// 挂载刚发出的弹幕
function displayNewestDanmu(danmuContent) {
  const newDanmu = {
    danmuId: -1,
    userId: useUserStore().userInfo.userId,
    videoId: videoInfo.videoId,
    content: danmuContent,
    time: videoRef.value?.currentTime,
    type: 1,
    color: 'FFFFFF',
  };
  danmuList.value.splice(dmIndex, 0, newDanmu);
}

// 视频跳转相关事件
function handleSeedked() {
  // 清除当前屏幕中的弹幕
  clearDanmu();
  // 恢复当前时间点的弹幕
  const curTime = videoRef.value?.currentTime;
  const matchedIndex = danmuList.value.findIndex((danmu) => danmu?.time >= curTime);
  dmIndex = matchedIndex; // 返回大于等于当前时间点的第一个弹幕下标索引,若不存在则为-1
}

// 清除当前屏幕所有弹幕
function clearDanmu() {
  for (let i = 0; i < tracks.value; i++) {
    const track = tracksRef.value[i];
    track.innerHTML = ''; // 清空轨道上的所有弹幕
  }
}

// 隐藏或展示当前屏幕所有弹幕
function showDanmu() {
  for (let i = 0; i < tracks.value; i++) {
    const track = tracksRef.value[i];
    track.style.setProperty('visibility', isDanmuOpen.value ? 'visible' : 'hidden');
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
    // 手动更新弹幕轨道数量
    tracks.value = Math.floor(screen.height / trackHeight);
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
    // 还原弹幕轨道数量
    tracks.value = oldTracks;
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
        isPaused.value = true;
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
  // 添加键盘监听事件
  window.addEventListener('keydown', handleKeyboard);
  // 加载弹幕轨道数量
  tracks.value = Math.floor(videoRef.value?.clientHeight / trackHeight);
  oldTracks = tracks.value;
  // 初始化音量
  volume.value = videoRef.value.volume * 100;
  // 取消遮罩层
  isloading.value = false;
  // 视频自动播放
  playVideo();
}

// 暴露数据
defineExpose({
  displayNewestDanmu, // 挂载刚发出的弹幕
  showDanmu, // 隐藏弹幕
  getVideoRefValue() {
    return videoRef.value; // 通过 getter 直接暴露 videoRef.value
  },
});
// 事件处理函数的引用
onMounted(() => {
  // 监听事件
  glVoiceRef.value.addEventListener('mouseleave', closeVolumeCtr); // 专门搞个事件处理函数的引用是为了解决音量设置menu与elmentUI的冲突
});

onBeforeUnmount(() => {
  // 清除监听事件
  window.removeEventListener('keydown', handleKeyboard);
});
</script>

<style>
.gulu-player {
  position: relative;
  width: 778px;
  height: 422px;
}

.danmu-wrap {
  position: absolute;
  z-index: 500;
  width: 100%;
  height: 100%;
  pointer-events: none; /* 穿透弹幕遮罩层 */
  overflow: hidden;
}

.danmu_track {
  position: relative;
  display: flex;
  width: 100%;
}

.danmu-item {
  font-size: 23px;
  will-change: transform; /* 强制浏览器提高弹幕的渲染速度 */
  /* transition: all 10s linear; */
  /* transform: translateX(var(--video-width)); */
  animation: danmuLeftToRight 10s linear;
}

/* 滚动的动画 */
@keyframes danmuLeftToRight {
  0% {
    transform: translateX(var(--video-width)); /* 从右侧开始 */
  }
  100% {
    transform: translateX(calc(-1 * var(--video-width))); /* 向左移动，离开视口 */
  }
}

.gl-mask {
  position: absolute;
  z-index: 2000;
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
  z-index: 1000;
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
  padding: 0 10px;
}

.gl-fullScreen-play {
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

.controls-center {
  flex: 2;
  display: none;
}

.controls-center-active {
  flex: 3;
  display: flex;
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
  position: relative;
  font-size: 14px;
  cursor: pointer;
}
.gl-speed:hover {
  transition: all 0.3s;
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

.gl-danmu-info {
  display: flex;
  align-items: center;
  justify-content: center;
}

.gl-danmu-set {
  padding: 0 10px;
}

.gl-video-sending-bar {
  visibility: hidden; /* 先隐藏起来吧  */
  display: flex;
  align-items: center;
  width: 100%;
  height: 100%;
}

.gl-danmu-send-bar {
  margin-left: 10px;
  width: 70%;
  height: 40%;
}

.gl-danmu-send-wrap {
  display: flex;
  width: 100%;
  height: 100%;
  border-radius: 8px;
  background-color: #4d4d4d;
}

.gl-danmu-color {
  display: flex;
  align-items: center;
  padding: 0 10px;
  height: 100%;
}

.gl-danmu-input {
  width: 85%;
  height: 100%;
  border-radius: 8px;
  outline: none;
  border: none;
  color: var(--GR3);
  background-color: #4d4d4d;
}

.gl-danmu-send-btn {
  width: 15%;
  height: 100%;
  background-color: var(--Bl2);
  border-top-right-radius: 8px;
  border-bottom-right-radius: 8px;
  border: none;
  outline: none;
  color: #fff;
}

.gl-video-sending-bar {
  display: flex;
}

.gl-speed-menu {
  /* display: none; */
  width: 70px;
  font-size: 14px;
  position: absolute;
  top: 0;
}
.gl-speed-menu li {
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  height: 36px;
  line-height: 36px;
  background-color: #292929;
  opacity: 0.7;
  border: none;
}
.gl-speed-menu li:hover {
  color: var(--Bl2);
}
.gl-speed-menu-small {
  right: calc(1 / 5 * 100%);
  transform: translate(35%, -109%);
}
.gl-speed-menu-big {
  right: calc(1 / 7 * 100%);
  transform: translate(35%, -116%);
}

.gl-resolution-menu {
  width: 150px;
  font-size: 13px;
  position: absolute;
  top: 0;
}
.gl-resolution-menu li {
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  height: 36px;
  line-height: 36px;
  background-color: #292929;
  opacity: 0.7;
  border: none;
}
.gl-resolution-menu li:hover {
  color: var(--Bl2);
}
.gl-resolution-menu-small {
  right: calc(2 / 5 * 100%);
  transform: translate(70%, -100%);
}
.gl-resolution-menu-big {
  right: calc(2 / 7 * 100%);
  transform: translate(70%, -100%);
}

/* el滑块样式调整 */
.el-slider {
  position: absolute !important;
  top: 0%;
  right: 0%;
}
.gl-volume-menu-small {
  right: calc(1 / 5 * 100%);
  transform: translate(75%, -160%);
}
.gl-volume-menu-big {
  right: calc(1 / 7 * 100%);
  transform: translate(55%, -116%);
}

.el-slider__button {
  width: 12px !important;
  height: 12px !important;
  background-color: var(--Bl2) !important;
}

/* 隐藏全屏后的样式 */
video::-webkit-media-controls {
  display: none; /* 隐藏所有浏览器默认控件 */
}
</style>
