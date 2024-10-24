import { defineStore } from "pinia";
import { reactive } from "vue";

// 用户信息
export const useUserStore = defineStore("userinfo", () => {
  // 用户信息
  const userInfo = reactive({
    userId: "",
    userName: "",
    avatar: require("@/assets/img/default.jpg"),
    roleLevel: "",
    status: "",
    token: "",
  });

  function setUserInfo(newUser) {
    // 使用 Object.assign 更新用户信息
    Object.assign(userInfo, newUser);
  }

  // 返回响应式
  return {
    userInfo, // 返回 userInfo
    setUserInfo, // 返回 setUserInfo 方法
  };
});

//管理登录注册界面
export const useLRStore = defineStore("LRstore", () => {
  //显示 登录界面
  let show_login = ref(true);
  //显示 注册界面
  let show_register = ref(false);
  //显示 短信界面
  let show_Sms = ref(false);
});
