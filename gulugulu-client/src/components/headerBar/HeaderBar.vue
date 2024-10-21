<template>
  <div class="gulu-header-bar">
    <!-- 左边 -->
    <ul class="left-entry">
      <li>
        <a href="#">
          <i class="iconfont gulu-bilibili-fill" style="font-size: 21px"></i>
          <span>首页</span>
        </a>
      </li>
      <li>
        <a href="#" class="default-entry"><span>番剧</span></a>
      </li>
      <li>
        <a href="#" class="default-entry"><span>直播</span></a>
      </li>
      <li>
        <a href="#" class="default-entry"><span>游戏中心</span></a>
      </li>
      <li>
        <a href="#" class="default-entry"><span>会员购</span></a>
      </li>
      <li>
        <a href="#" class="default-entry"><span>漫画</span></a>
      </li>
      <li>
        <a href="#" class="default-entry">
          <i class="iconfont gulu-xiazai" style="font-size: 18px"></i>
          <span>下载客户端</span>
        </a>
      </li>
    </ul>
    <!-- 中间 -->
    <div class="center-search-container">
      <!-- 搜索框 -->
      <el-input
        class="nav-search-input"
        v-model="searchInput"
        placeholder="请输入搜索内容"
      >
        <template #suffix>
          <div class="nav-search-btn">
            <i class="gulu-sousuo iconfont"></i>
          </div>
        </template>
      </el-input>
    </div>
    <!-- 右边 -->
    <ul class="right-entry">
      <li>
        <a href="#">
          <img
            class="avatar"
            @click="showLogin"
            src="@/assets/img/default.jpg"
            alt="咕噜咕噜~~"
          />
        </a>
      </li>
      <li>
        <a href="#">
          <i
            style="font-size: 21px"
            class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-06-06 iconfont"
          ></i>
          <span>大会员</span>
        </a>
      </li>
      <li>
        <a href="#">
          <i style="font-size: 21px" class="gulu-xiaoxi iconfont"></i>
          <span>消息</span>
        </a>
      </li>
      <li>
        <a href="#">
          <i
            style="font-size: 20px"
            class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-03 iconfont"
          ></i>
          <span>动态</span>
        </a>
      </li>
      <li>
        <a href="#">
          <i
            style="font-size: 20px"
            class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-04 iconfont"
          ></i>
          <span>收藏</span>
        </a>
      </li>
      <li>
        <a href="#">
          <i
            style="font-size: 19px"
            class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-07 iconfont"
          ></i>
          <span>历史</span>
        </a>
      </li>
      <li>
        <a href="#">
          <i
            style="font-size: 20px"
            class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-05 iconfont"
          ></i>
          <span>创作中心</span>
        </a>
      </li>
      <li>
        <a href="#">
          <i
            style="font-size: 21px"
            class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-09 iconfont"
          ></i>
          <span>投稿</span>
        </a>
      </li>
    </ul>
    <!-- 登录界面 -->
    <el-dialog v-model="loginVisible" width="820" :before-close="handleClose">
      <LoginOrRegister
        ref="loginOrRegister"
      ></LoginOrRegister>
    </el-dialog>
  </div>
</template>

<script setup>
import LoginOrRegister from "@/components/loginRegister/LoginOrRegister.vue";
import { ref } from "vue";

// 搜索内容
let searchInput = ref("");
// 是否显示登录界面
let loginVisible = ref(false);
//获取组件
const loginOrRegister = ref();

//显示登录界面
function showLogin() {
  loginVisible.value = true;
}

//退出登录界面
function handleClose(done) {
  if (loginOrRegister.value) {
    loginOrRegister.value.init();
    done()
  }
}
</script>

<style scoped>
.gulu-header-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  width: 100%;
  max-width: 2560px;
  height: 64px;
  z-index: 10;
}

.left-entry {
  display: flex;
  flex: 1;
  align-items: center;
  justify-content: space-evenly;
  height: 64px;
}

.left-entry li a {
  color: var(--color-text-main);
  cursor: pointer;
}

.left-entry li a:first-child {
  display: flex;
  align-items: center;
  justify-content: space-evenly;
}

.left-entry li a:first-child span {
  padding-left: 3px;
}

.center-search-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 64px;
  border-radius: 6px;
}

.nav-search-input {
  height: 60%;
  width: 90%;
  opacity: 0.9;
}

.nav-search-input ::v-deep .el-input__wrapper {
  background-color: #e4e8e8;
  border-radius: 6px;
  box-shadow: none;
  padding: 1px 30px 1px 11px;
}
.nav-search-input ::v-deep .el-input__wrapper:hover {
  background-color: #fff;
}

.nav-search-input-active ::v-deep .el-input__wrapper {
  background-color: #0000001a;
}

.nav-search-btn {
  position: absolute;
  top: 10%;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  right: 8px;
  width: 32px;
  height: 32px;
  line-height: 32px;
  border-radius: 6px;
  transition: all 0.5s;
}

.nav-search-btn:hover {
  cursor: pointer;
  background-color: rgba(198, 189, 189, 0.3);
}

.right-entry {
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  flex: 1;
  align-items: center;
  height: 64px;
}

.right-entry li a {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}

.right-entry li:last-child a {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
  background-color: #fb7299;
  width: 6.25em;
  height: 2.5em;
  border-radius: 6px;
  transition: all 0.5s;
}

.right-entry li:last-child a:hover {
  background-color: #fc8bab;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 20px;
  transition: all 0.5s;
}

.avatar:hover {
  transform: translateY(30px) scale(2);
}

::v-deep .el-dialog {
  padding: 0;
  position: relative;
  border-radius: 12px;
}
</style>
