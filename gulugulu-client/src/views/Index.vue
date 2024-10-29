<template>
  <div class="index" @scroll="handleScroll">
    <div class="gulu-header">
      <HeaderBar :isFixedHeaderBar="isFixedHeaderBar"></HeaderBar>
      <HeaderBanner> </HeaderBanner>
      <HeaderChannel></HeaderChannel>
    </div>
    <!-- 固定频道栏 -->

    <!-- 主题布局 -->
    <div class="gulu-main-layout">
      <Layout></Layout>
    </div>
  </div>
</template>

<script setup>
import HeaderBanner from '@/components/header/HeaderBanner.vue';
import HeaderBar from '@/components/header/HeaderBar.vue';
import HeaderChannel from '@/components/header/HeaderChannel.vue';
import Layout from '@/components/layout/Layout.vue';
import { onMounted, ref } from 'vue';

// 是否是固定导航栏
let isFixedHeaderBar = ref(false);

function handleScroll(e) {
  const srollTop = e.target.scrollTop;
  isFixedHeaderBar.value = srollTop > 200;
}
</script>

<style scoped>
.index {
  height: 100vh; /* 设置容器高度 */
  overflow-y: auto; /* 使内容可滚动 */
}

.gulu-header {
  position: relative;
}

.gulu-main-layout {
  background-color: #fff;
  margin: 0 auto;
  padding: 0 60px;
  max-width: 2100px;
  width: 100%;
}
</style>
