<template>
  <div class="container">
    <div class="box1"></div>
      <div class="box2"></div>
    <div class="box3"></div>
  </div>
</template>

<script setup></script>
<style scoped>
.container {
  display: flex;
  transform-style: preserve-3d;
  perspective: 3300px;
  align-items: center;
  justify-content: space-between;
  height: 200px;
}
.box1 {
  display: flex;
  width: 200px;
  height: 200px;
  background-color: pink;
}
.box2 {
  display: flex;
  width: 200px;
  height: 200px;
  background-image: url("../assets/img/default.jpg");
  background-size: 100%;
  transition: all 0.7s;
  animation: box2_move 10s ease infinite;
}
.box2:hover {
  transform: rotateY(120deg);
}
.box3 {
  display: flex;
  width: 200px;
  height: 200px;
  background-color: black;
}
@keyframes box2_move {
  0% {
    transform: translate(0, 0) rotateZ(180deg);
  }
  25% {
    transform: translate(400px, 0) rotateY(180deg);
  }
  50% {
    transform: translate(400px, 400px) rotateZ(180deg);
  }
  75% {
    transform: translate(0, 400px) rotateY(180deg);
  }
  100% {
    transform: translate(0, 0) rotateZ(180deg);
  }
}
</style>
