<template>
  <div class="gulu-header-bar" :class="props.isFixedHeaderBar ? 'fixed-header-bar' : ''">
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
        :class="isFixedHeaderBar ? 'nav-search-input-active' : ''"
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
        <!-- 未登录状态 -->
        <div class="avatar-login" @click="showLogin" v-if="userInfo.userId == ''">
          <span>登录</span>
        </div>
        <!-- 登录显示头像 -->
        <div class="avatar-bubble" @mousemove="handleMouseEnter" @mouseleave="handleMouseLeave" v-else>
          <img class="avatar" :src="userInfo.avatar" :alt="`${userInfo.userName}的头像`" :class="{ 'avatar-big': isAvatarBig }" />
          <div class="avatar-panel" :style="{ display: popoverDisplay }">
            <div class="panel-proper">
              <a class="nickname-item" href="#" style="color: var(--PK1)">{{ userInfo.userName }}</a>
              <div class="vip-container">
                <a class="vip-item" href="#">
                  <img :src="vipImg" alt="" />
                </a>
              </div>
              <div class="coins-item" style="color: var(--GR3)">
                <div>硬币:<span style="color: black; padding: 0 3px">561</span></div>
                <div>贝壳:<span style="color: black; padding-left: 3px">0</span></div>
              </div>
              <div class="counts-item">
                <div class="single-count-item">
                  <div class="count-num">328</div>
                  <div class="count-text">关注</div>
                </div>
                <div class="single-count-item">
                  <div class="count-num">45</div>
                  <div class="count-text">粉丝</div>
                </div>
                <div class="single-count-item">
                  <div class="count-num">15</div>
                  <div class="count-text">动态</div>
                </div>
              </div>
              <div class="exit-item">
                <button class="exit-btn" @click="handleExit">退出登录</button>
              </div>
            </div>
          </div>
        </div>
      </li>
      <li>
        <a href="#">
          <i style="font-size: 21px" class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-06-06 iconfont"></i>
          <span>大会员</span>
        </a>
      </li>
      <li class="msg-box" @mouseenter="displayMsgBubble" @mouseleave="hideMsgBubble">
        <a href="#">
          <i style="font-size: 21px" class="gulu-xiaoxi iconfont"></i>
          <span>消息</span>
          <ul class="msg-bubble header-bubble" v-if="isOpenMsg">
            <li><a href="#">回复我的</a></li>
            <li><a href="#">@我的</a></li>
            <li><a href="#">收到的赞</a></li>
            <li><a href="#">系统消息</a></li>
            <li><a href="#">我的消息</a></li>
          </ul>
        </a>
      </li>
      <li>
        <a href="#">
          <i style="font-size: 20px" class="gulu-zhongxindongtai iconfont"></i>
          <span>动态</span>
        </a>
      </li>
      <li>
        <a href="#">
          <i style="font-size: 20px" class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-04 iconfont"></i>
          <span>收藏</span>
        </a>
      </li>
      <li>
        <a href="#">
          <i style="font-size: 19px" class="gulu-lishiguiji iconfont"></i>
          <span>历史</span>
        </a>
      </li>
      <li>
        <a href="#">
          <i style="font-size: 20px" class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-05 iconfont"></i>
          <span>创作中心</span>
        </a>
      </li>
      <li>
        <router-link :to="{ name: 'uploadVideo' }">
          <a href="#">
            <i style="font-size: 21px" class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-09 iconfont"></i>
            <span>投稿</span>
          </a>
        </router-link>
      </li>
    </ul>
    <!-- 登录界面 -->
    <el-dialog v-model="loginVisible" width="820" :before-close="handleClose">
      <LoginOrRegister ref="loginOrRegister"></LoginOrRegister>
    </el-dialog>
  </div>
</template>

<script setup>
import { userExit } from '@/apis/userApi/userApi';
import LoginOrRegister from '@/components/loginRegister/LoginOrRegister.vue';
import { useUserStore } from '@/store/index.js';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref } from 'vue';

const props = defineProps({
  isFixedHeaderBar: {
    type: Boolean,
    default: false,
  },
});
// 获取组件
const loginOrRegister = ref();
// 获取userStore
const userStore = useUserStore();

// 是否展示登录框
const loginVisible = ref(false);
// 头像气泡框的显隐
const isAvatarBig = ref(false);
const popoverDisplay = ref('none');
let avatarInTimer;
let avatarOutTimer;
// 气泡框的显隐
const isOpenMsg = ref(false);
let msgInTimer;
let msgOutTimer;

// 搜索内容
let searchInput = ref('');
// 用户信息
const { userInfo } = storeToRefs(userStore);
// 会员等级图片
const vipImg = computed(() => {
  const roleLevel = userInfo.value.roleLevel;
  if (roleLevel == 2) {
    // 年度大会员
    return require('@/assets/img/vip/vip2.png');
  } else if (roleLevel == 1) {
    // 大会员
    return '';
  } else {
    // 普通会员
    return '';
  }
});

// 显示登录界面
function showLogin() {
  loginVisible.value = true;
}
// 退出登录界面
function handleClose(done) {
  if (loginOrRegister.value) {
    loginOrRegister.value.init();
    done();
  }
}

// 退出登录
function handleExit() {
  userExit();
}

// 悬浮头像时，气泡的显隐
function handleMouseEnter() {
  clearTimeout(avatarOutTimer); // 这里要清除隐藏的计时器，否则在0.2秒内出入头像，会导致头像变大但气泡突然消失
  avatarInTimer = setTimeout(() => {
    popoverDisplay.value = '';
    isAvatarBig.value = true;
  }, 100);
}
function handleMouseLeave() {
  clearTimeout(avatarInTimer); // 清除显示计时器防止快速经过头像时的气泡闪烁
  avatarOutTimer = setTimeout(() => {
    popoverDisplay.value = 'none';
    isAvatarBig.value = false;
  }, 200);
}

// 消息栏的显隐
function displayMsgBubble() {
  if (msgOutTimer) {
    clearTimeout(msgOutTimer);
  }
  msgInTimer = setTimeout(() => {
    isOpenMsg.value = true;
  }, 300);
}
function hideMsgBubble() {
  if (msgInTimer) {
    clearTimeout(msgInTimer);
  }
  msgOutTimer = setTimeout(() => {
    isOpenMsg.value = false;
  }, 300);
}
</script>

<style scoped>
.gulu-header-bar {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1002;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 0 24px;
  max-width: 2560px;
  height: 64px;
}

.fixed-header-bar {
  position: fixed;
  top: 0;
  border-bottom: 1px solid var(--GR1);
  box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.05);
  background-color: #fff;
}

.fixed-header-bar .left-entry li a {
  color: var(--BK1);
}
.fixed-header-bar .right-entry li i {
  color: var(--BK1);
}
.fixed-header-bar .right-entry li a {
  color: var(--BK2);
}
.fixed-header-bar .right-entry li:last-child i {
  color: var(--Wt1);
}
.fixed-header-bar .right-entry li:last-child a {
  color: var(--Wt1);
}

.left-entry {
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  flex: 1;
  height: 64px;
}

.left-entry li a {
  color: var(--Wt1);
  cursor: pointer;
}

.default-entry {
  color: var(--Wt1);
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
  border-radius: 6px;
}

.nav-search-input-active {
  border: 1px solid var(--GR1);
}

.nav-search-input ::v-deep .el-input__wrapper {
  background-color: #f2f3f4;
  border-radius: 6px;
  box-shadow: none;
  padding: 1px 30px 1px 11px;
}
.nav-search-input ::v-deep .el-input__wrapper:hover {
  background-color: #fff;
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
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  flex: 1;
  align-items: center;
  height: 64px;
}

.right-entry > li a {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: var(--Wt1);
  font-size: 14px;
  cursor: pointer;
}

.right-entry > li:last-child a {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  color: var(--Wt1);
  font-size: 14px;
  cursor: pointer;
  background-color: var(--PK1);
  width: 6.25em;
  height: 2.5em;
  border-radius: 6px;
  transition: all 0.5s;
}

@media (max-width: 1133px) {
  .right-entry li a span {
    font-size: 0px;
  }
}

.right-entry > li:last-child a:hover {
  background-color: var(--PK-Hover1);
}

.header-bubble {
  top: 100%;
  position: absolute;
  width: 142px;
  border-radius: 8px;
}

.msg-box {
  transition: animation 0.3s linear;
}
.msg-box:hover .gulu-xiaoxi {
  animation: jump 0.3s;
}
@keyframes jump {
  50% {
    transform: translateY(-5px);
  }
  100% {
    transform: translateY(0);
  }
}

.msg-bubble {
  left: 16%;
  padding: 12px 0;
  background-color: var(--Wt1);
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  border: 1px solid var(--GR1);
}
.msg-bubble > li {
  position: relative;
  display: flex;
  align-items: center;
  padding: 10px 0 10px 27px;
  text-align: left;
  font-size: 14px;
  transition: background-color 0.3s;
  cursor: pointer;
}
.msg-bubble > li a {
  color: var(--Text3);
}
.msg-bubble > li:hover {
  background-color: var(--GR2);
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 20px;
  transition: all 0.5s;
  cursor: pointer;
  position: relative;
  z-index: 2;
}

.avatar-big {
  transform: translateY(30px) scale(2);
}

.avatar-login {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  cursor: pointer;
  background-color: var(--PK1);
  border-radius: 20px;
}

.avatar-login span {
  color: var(--Wt1);
}

.avatar-bubble {
  position: relative;
  z-index: 1;
}

.avatar-panel {
  position: absolute;
  top: 50px;
  left: -130px;
  width: 300px;
  height: 475px;
  padding: 0 16px;
  background-color: var(--Wt1);
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  z-index: 1; /* 确保它在图片下面 */
}

.panel-proper {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-top: 50px;
  width: 100%;
}

.nickname-item {
  font-size: 20px !important;
  font-weight: 500;
}

.vip-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.vip-item {
  margin: 5px 0;
  width: 83px;
  height: 20px;
}
.vip-item img {
  width: 100%;
  height: 100%;
}

.coins-item {
  font-size: 12px;
  font-family: 'microsoft yahei';
  display: flex;
  justify-content: center;
}

.counts-item {
  margin-top: 5px;
  display: flex;
  justify-content: space-evenly;
}

.single-count-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.count-num {
  font-size: 18px;
}

.count-text {
  font-size: 12px;
  color: var(--GR3);
}

.exit-item {
  margin-top: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.exit-btn {
  width: 100px;
  height: 40px;
  border: none;
  border-radius: 4px;
  color: #fff;
  background-color: var(--PK1);
}

/* 修改el样式 */
::v-deep .el-dialog {
  padding: 0;
  position: relative;
  border-radius: 12px;
}
</style>
