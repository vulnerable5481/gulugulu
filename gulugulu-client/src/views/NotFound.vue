<template>
  <div class="error-body">
    <div class="gulu-header">
      <HeaderBar :isFixedHeaderBar="true"></HeaderBar>
    </div>
    <div class="error-container">
      <div class="error-panel">
        <img src="@/assets/img/notfound/404notfound.png" alt="咕噜咕噜~~~~" />
      </div>
      <div class="rollback-area">
        <button class="rollback-btn" @click="returnLastPage">返回上一页</button>
      </div>
      <div class="error-manga">
        <img class="error-manga-img" :src="currentImg" alt="" />
      </div>
    </div>
  </div>
</template>

<script setup>
import HeaderBar from '@/components/header/HeaderBar.vue';
import { computed } from 'vue';
import { useRouter } from 'vue-router';

// 路由
const router = useRouter();

let currentImg = computed(() => {
  const index = Math.floor(Math.random() * 12) + 1; // 生成 1 到 7 的随机数
  return require(`@/assets/img/notfound/img${index}.png`);
});

// 返回上一页
function returnLastPage() {
  router.back();
}
</script>

<style scoped>
.error-body {
  padding-top: 70px;
  width: 100%;
  background-color: #f1f2f3;
}
.error-container {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  width: 980px;
  margin: 30px auto 30px;
  background-color: #ffffff;
  border-radius: 10px;
}

.error-manga-img {
  width: 100%;
  height: 100%;
}

.rollback-area {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 80px;
  width: 100%;
}

.rollback-btn {
  padding: 12px 20px;
  font-size: 16px;
  background-color: var(--Bl2);
  color: #fff;
  border-radius: 4px;
  transition: all 0.2s;
  cursor: pointer;
  border: 0;
}
</style>
