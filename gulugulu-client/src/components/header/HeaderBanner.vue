<template>
  <div class="gulu-header_banner">
    <!-- 滑动图 -->
    <div
      class="animated-banner"
      ref="animatedBanner"
      @mouseenter.stop="handleMouseEnter"
      @mouseleave.stop="handleMouseLeave"
      @mousemove.stop="handleMouseMove"
    >
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/1.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/2.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/3.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/4.webp"
          data-height="300"
          data-width="3200"
          height="159"
          width="1696"
          style="
            height: 159px;
            width: 1696px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <!-- 群雁 -->
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/5.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/6.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <!-- 高铁列车 -->
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/7.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(110px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/8.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/9.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/10.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(10.4px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/11.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/12.webp"
          data-height="300"
          data-width="3200"
          height="159"
          width="1696"
          style="
            height: 159px;
            width: 1696px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/13.webp"
          data-height="300"
          data-width="3200"
          height="162"
          width="1728"
          style="
            height: 162px;
            width: 1728px;
            transform: translate(0px, 1.08px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/14.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/15.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <!-- 大雁 -->
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/16.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, -10.4px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <!-- 人物1 TODO: 放大效果-->
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/17.webp"
          data-height="129"
          data-width="200"
          height="67"
          width="104"
          style="
            height: 67.08px;
            width: 104px;
            transform: translate(260px, 10.4px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <!-- 人物2 TODO: 上下摇摆-->
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/18.webp"
          data-height="164"
          data-width="256"
          height="85"
          width="133"
          style="
            height: 85.28px;
            width: 133.12px;
            transform: translate(104px, 10.4px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <!-- 落叶 -->
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/19.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <!-- 视频 -->
      <div class="layer">
        <video
          ref="animatedBannerVideo"
          loop=""
          :src="require('@/assets/img/animated-banner/20.webm')"
          playsinline=""
          width="1664"
          height="156"
          style="
            object-fit: cover;
            height: 156px;
            width: 1664px;
            transform: translate(0px, 10.4px) rotate(0deg) scale(1);
            opacity: 1;
          "
          data-height="300"
          data-width="3200"
          muted
          autoplay
        ></video>
      </div>
      <!-- 柱子 -->
      <div class="layer">
        <img
          src="@/assets/img/animated-banner/21.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
      <!-- Wing -->
      <div class="layer">
        <img
          ref="wing"
          src="@/assets/img/animated-banner/22.webp"
          data-height="300"
          data-width="3200"
          height="156"
          width="1664"
          style="
            height: 156px;
            width: 1664px;
            transform: translate(0px, 0px) rotate(0deg) scale(1);
            opacity: 1;
          "
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import HeaderBar from "@/components/header/HeaderBar.vue";
import { onMounted, ref } from "vue";

// 播放视频的函数
//注意这里涉及到生命周期问题,需要声明在外侧
const animatedBannerVideo = ref();
function broadcastVideo() {
  animatedBannerVideo.value.play();
}

const styleMap = {
  1: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  2: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  3: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  4: {
    initialStyle: {
      height: "159px",
      width: "1669px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  5: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  6: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  7: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 110,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 6 },
    element: null,
  },
  8: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  9: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  10: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 10.4,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  11: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  12: {
    initialStyle: {
      height: "159px",
      width: "1696px",
      translateX: 0,
      translateY: 1.08,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  13: {
    initialStyle: {
      height: "162px",
      width: "1728px",
      translateX: 0,
      translateY: 1.08,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  14: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  15: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 35 },
    element: null,
  },
  16: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 7 },
    element: null,
  },
  17: {
    initialStyle: {
      height: "67.08px",
      width: "104px",
      translateX: 260,
      translateY: 10.4,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 7 },
    element: null,
  },
  18: {
    initialStyle: {
      height: "85.28px",
      width: "133.12px",
      translateX: 104,
      translateY: 10.4,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 10 },
    element: null,
  },
  19: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 5 },
    element: null,
  },
  20: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 15 },
    element: null,
  },
  21: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 10 },
    element: null,
  },
  22: {
    initialStyle: {
      height: "156px",
      width: "1664px",
      translateX: 0,
      translateY: 0,
      rotate: 0,
      scale: 1,
      opacity: 1,
    },
    style: { direction: "x", scale: 6 },
    element: null,
  },
};
let animatedBanner = ref();
let layers = [];
let initMouseLeft = 0;

//初始化
function init() {
  Object.keys(styleMap).forEach((key) => {
    const current = styleMap[key];
    const initStyle = current.initialStyle;
    //初始化element
    current.element = layers[key - 1].children[0];
    //初始化样式
    current.element.style = `height: ${initStyle.height}; width: ${initStyle.width};
     transform: translate(${initStyle.translateX}px, ${initStyle.translateY}px) rotate(${initStyle.rotate}deg) scale(${initStyle.scale});
      opacity: ${initStyle.opacity}; object-fit: ${initStyle.objectFit}`;
  });
}

//鼠标进入
function handleMouseEnter(event) {
  initMouseLeft = event.pageX;
}

function calcPosition(x, scale) {
  return (x - initMouseLeft) / scale;
}

//鼠标移动
function handleMouseMove(event) {
  //添加样式
  Object.keys(styleMap).forEach((key) => {
    const current = styleMap[key];
    if (current.style) {
      const initStyle = current.initialStyle;
      const style = current.style;
      const element = current.element;
      //计算偏差值
      const mouseLeft = calcPosition(event.pageX, current.style.scale);
      //修改样式
      let resultStyle = `height: ${initStyle.height}; width: ${initStyle.width};
                          opacity: ${initStyle.opacity};`;
      if (style.direction == "x") {
        resultStyle += `transform: translate(${
          initStyle.translateX + mouseLeft
        }px, ${initStyle.translateY}px) rotate(${initStyle.rotate}deg) scale(${
          initStyle.scale
        });`;
      } else {
        resultStyle += `transform: translate(${initStyle.translateX}px, ${
          initStyle.translateY + mouseLeft
        }px) rotate(${initStyle.rotate}deg) scale(${initStyle.scale});`;
      }
      //定制样式
      //给图片18增加缩小放大的效果
      if (key == 18) {
        const scaleDeg = mouseLeft / 600;
        resultStyle += `transform: translate(${
          initStyle.translateX + mouseLeft
        }px, ${initStyle.translateY}px) rotate(${initStyle.rotate}deg) scale(${
          initStyle.scale - scaleDeg
        });`;
      }
      //给图片17增加上下摇摆的效果
      if (key == 17) {
        const rotateDeg = mouseLeft / 10;
        resultStyle += `transform: translate(${
          initStyle.translateX + mouseLeft
        }px, ${initStyle.translateY}px) rotate(${
          initStyle.rotate + rotateDeg
        }deg) scale(${initStyle.scale});`;
      }
      current.element.style = resultStyle;
    }
  });
}

//鼠标移出
function handleMouseLeave() {
  //复原
  Object.keys(styleMap).forEach((key) => {
    const current = styleMap[key];
    const initStyle = current.initialStyle
    //追加过渡
    //因为init中会初始化，又会将过渡给抹除，所以不用担心过渡会影响鼠标进入时的效果
    current.element.style = `height: ${initStyle.height}; width: ${initStyle.width};
     transform: translate(${initStyle.translateX}px, ${initStyle.translateY}px) rotate(${initStyle.rotate}deg) scale(${initStyle.scale});
      opacity: ${initStyle.opacity}; object-fit: ${initStyle.objectFit};transition:all .5s`;
  });
}

//挂载
onMounted(() => {
  //获取banner中的layers
  layers = animatedBanner.value.children;
  //自动播放视频
  broadcastVideo();
  //初始化style
  init();
});
</script>

<style scoped>
.gulu-header_banner {
  position: relative;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  overflow: hidden;
  min-width: 1000px;
  min-height: 155px;
  height: 9.375vw;
}

.animated-banner {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1;
  overflow: hidden;
}
.animated-banner > .layer {
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
