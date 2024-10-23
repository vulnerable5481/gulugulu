<template>
  <div class="large-header">
    <HeaderBanner> </HeaderBanner>
    <HeaderChannel></HeaderChannel>
  </div>
  <!-- 主题布局 -->
  <Layout></Layout>
</template>

<script setup>
import HeaderBanner from "@/components/headerBanner/HeaderBanner.vue";
import HeaderChannel from "@/components/headerChannel/HeaderChannel.vue";
import Layout from "@/components/layout/Layout.vue";
</script>

<style scoped></style>
