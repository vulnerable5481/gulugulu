import { defineStore } from 'pinia';
import { reactive } from 'vue';

// 用户信息
export const useUserStore = defineStore('userinfo', () => {
  // 用户信息
  const userInfo = reactive({
    userId: '',
    userName: '',
    avatar: require('@/assets/img/default.jpg'),
    roleLevel: '',
    status: '',
    token: '',
  });

  // 设置用户信息
  function setUserInfo(newUser) {
    // 使用 Object.assign 更新用户信息
    Object.assign(userInfo, newUser);
  }

  // 删除用户信息
  function removeUserInfo() {
    // 将用户信息重置为初始值
    Object.assign(userInfo, {
      userId: '',
      userName: '',
      avatar: require('@/assets/img/default.jpg'),
      roleLevel: '',
      status: '',
      token: '',
    });
  }

  // 返回响应式
  return {
    userInfo, //  userInfo
    setUserInfo, //  setUserInfo 方法
    removeUserInfo,
  };
});
