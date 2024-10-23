<template>
  <div class="main-layout">
    <div class="recommonded-container">
      <!--  轮播图 -->
      <div class="recommended-swipe">
        <!-- 骨架屏 -->
        <div class="recommended-swipe-shim"></div>
        <!-- 轮播图主体 -->
        <div class="recommended-swipe-body">
          <div class="carousel-area" v-if="currentImg">
            <img
              class="carousel-inner-img"
              :src="currentImg.url"
              alt="咕噜咕噜被玩坏了~~~~"
            />
          </div>
          <div class="carousel-footer">
            <div class="carousel-footer-text"></div>
            <div class="carsousel-footer-dots"></div>
          </div>
          <div class="arrows"></div>
        </div>
      </div>
      <!--  随机推荐,用v-for渲染-->
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue";

let imgs = reactive([]);
//维护轮播图索引
let currentIndex = ref(0);
//当前图片
const currentImg = computed(() => {
  return imgs[currentIndex.value];
});

//右移
function rightImg() {
  currentIndex.value++;
  if (currentIndex.value == 9) {
    currentIndex.value = 0;
  }
  console.log(currentIndex.value);
}
//左移
function leftImg() {
  currentIndex.value--;
  if (currentIndex.value == -1) {
    currentIndex.value = 8;
  }
  console.log(currentIndex.value);
}

function init() {
  // 初始化轮播图图片
  for (let i = 1; i <= 9; i++) {
    const img = {
      id: i,
      url: require(`@/assets/img/header-layout/img${i}.png`),
    };
    imgs.push(img);
  }
}

onMounted(() => {
  init();
  console.log(imgs);
});
</script>

<style scoped>
.main-layout {
  background-color: #fff;
  margin: 0 auto;
  padding: 0 60px;
  max-width: 2100px;
  width: 100%;
}
</style>
