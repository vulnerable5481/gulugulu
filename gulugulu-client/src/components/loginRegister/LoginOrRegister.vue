<template>
  <div class="gugu-loginRegister-content">
    <div class="gulu-loginRegister-left">
      <img class="gulu-img" src="@/assets/img/gulu.gif" alt="咕噜~~~" />
    </div>
    <div class="gulu-loginRegister-right">
      <div class="login-tab-wp">
        <span class="login-tab-wp-secret" @click="changeVisible(0)"
          >密码登录</span
        >
        <div class="login-tab-line"></div>
        <span class="login-tab-wp-sms" @click="changeVisible(2)">短信登录</span>
      </div>
      <div class="login-pwd-wp">
        <!-- 登录界面 -->
        <Login
          v-if="show_login"
          @changeVisible="changeVisible"
          @changeImg="changeImg"
        ></Login>
        <!-- 短信注册界面 -->
        <SMSRegister v-if="show_Sms"></SMSRegister>
        <!-- 密码注册界面 -->
        <SecretRegister
          v-if="show_register"
          @changeImg="changeImg"
        ></SecretRegister>
      </div>
      <!-- TODO: 待完成  其他登录 比如QQ 微信 -->
      <!-- <div class="login-sns-wp"></div> -->
    </div>
    <div class="gulu-loginRegister-footer">
      <span
        >未注册过咕噜咕噜的手机号，我们将自动帮助你注册账号<br /><span
          >登录或完成注册代表你同意</span
        ><a href="#">用户协议</a><span>和</span><a href="#">隐私政策</a></span
      >
    </div>
    <div class="left-img">
      <img @click="emit('handleClose')" :src="leftimg" alt="咕噜~~" />
    </div>
    <div class="right-img">
      <img :src="rightimg" alt="咕噜~~" />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, defineExpose } from "vue";
import Login from "./Login.vue";
import SMSRegister from "./SMSRegister.vue";
import SecretRegister from "./SecretRegister.vue";

//图片
let leftimg = ref(require("@/assets/img/login-register/left.png"));
let rightimg = ref(require("@/assets/img/login-register/right.png"));
//显示 登录界面
let show_login = ref(true);
//显示 注册界面
let show_register = ref(false);
//显示 短信界面
let show_Sms = ref(false);

//显示  登录/注册/短信界面
function changeVisible(number) {
  show_login.value = false;
  show_register.value = false;
  show_Sms.value = false;
  if (number == 0) {
    show_login.value = true;
  }
  if (number == 1) {
    show_register.value = true;
  }
  if (number == 2) {
    show_Sms.value = true;
  }
}

// 修改两侧图片
function changeImg(imgData) {
  if (imgData == 0) {
    leftimg.value = require("@/assets/img/login-register/left.png");
    rightimg.value = require("@/assets/img/login-register/right.png");
    //复原图片
  } else {
    //修改图片
    leftimg.value = require("@/assets/img/login-register/left-blind.png");
    rightimg.value = require("@/assets/img/login-register/right-blind.png");
  }
}

//初始化登录界面
function init() {
  show_login.value = true;
  show_Sms.value = false;
  show_register.value = false;
}

defineExpose({
  init,
});
</script>

<style scoped>
.gugu-loginRegister-content {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  width: 100%;
  height: 400px;
}

.left-img {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100px;
  height: 100px;
}
.left-img img {
  width: 100%;
  height: 100%;
}
.right-img {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 100px;
  height: 100px;
}
.right-img img {
  width: 100%;
  height: 100%;
}

.gulu-loginRegister-left {
  display: flex;
  width: 300px;
  height: 300px;
}

.gulu-img {
  width: 100%;
  height: 100%;
}

.gulu-loginRegister-right {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
  width: 455px;
  height: 300px;
}

.login-tab-wp {
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  flex: 2;
  width: 65%;
  font-size: var(--font-size-big);
}

.login-tab-wp-secret:hover {
  color: var(--Bl1);
  cursor: pointer;
}

.login-tab-line {
  height: 22.5px;
  color: var(--GR1);
  width: 1px;
  border: 1px solid;
}

.login-tab-wp-sms:hover {
  color: var(--Bl1);
  cursor: pointer;
}

.login-pwd-wp {
  flex: 4;
}

.login-sns-wp {
  flex: 4;
}

.gulu-loginRegister-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 400px;
  height: 100px;
}

.gulu-loginRegister-footer span {
  font-size: 14px;
  color: #999;
}

.gulu-loginRegister-footer a {
  padding: 0 6px;
  font-weight: 400;
  font-size: 14px;
  color: #16a9da;
}
</style>
