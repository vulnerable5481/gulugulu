import { exit, login, register } from '@/apis/userApi/userRequest';
import { useUserStore } from '@/store';
import { isEmpty } from '@/utils/GuluUtils';
import { ElMessage } from 'element-plus';

// 用户密码注册
export async function handleRegister(user) {
  //1.判断信息
  if (!user.userName || user.userName == '') {
    ElMessage.error('账号不能为空');
    return;
  }
  if (!user.userName || user.password == '') {
    ElMessage.error('密码不能为空');
    return;
  }
  if (!user.userName || user.mksuerPwd == '') {
    ElMessage.error('请确认密码!');
    return;
  }
  if (user.password != user.mksuerPwd) {
    ElMessage.error('前后密码不一致');
    return;
  }
  //2.注册
  const result = await register(user);
  if (result.data.code == 200) {
    ElMessage.success('注册成功~~~请移步至登录~o( =∩ω∩= )m');
  } else {
    ElMessage.error(result.data.msg);
  }
}

// 用户登录
export async function handleLogin(user) {
  // 校验数据
  if (isEmpty(user.userName)) {
    ElMessage.error('账号不能为空~');
    return;
  }
  if (isEmpty(user.password)) {
    ElMessage.error('请输入密码~');
    return;
  }
  // 登录
  const { data, code, msg } = await login(user);
  // 登录失败
  if (code != 200 || !data) {
    const err = msg ? msg : '咕噜咕噜被玩坏了 o(╥﹏╥)o 这绝对不是咕噜的错~~~~';
    ElMessage.error(err);
    return;
  }
  // 保存信息
  const userStore = useUserStore();
  userStore.setUserInfo(data); // pinia
  localStorage.setItem('userInfo', JSON.stringify(data)); // 本地浏览器
  // 欢饮回来
  ElMessage.success(`欢迎${data.userName}回来!`);
}

// 用户退出
export function userExit() {
  // 删除用户缓存
  const token = useUserStore().userInfo.token;
  exit(token).then(({ data }) => {
    if (data.code == 200) {
      // 删除本地信息
      localStorage.removeItem('userInfo');
      // 删除pinia信息
      useUserStore().removeUserInfo();
    } else {
      ElMessage.error(`${useUserStore().userInfo.userName}退出失败~~`);
    }
  });
}

//初始化用户
export function initUserInfo() {
  const s = localStorage.getItem('userInfo');
  const userInfo = JSON.parse(s);
  const userStore = useUserStore();
  userStore.setUserInfo(userInfo);
}
