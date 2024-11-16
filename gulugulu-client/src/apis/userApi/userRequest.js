import httpRequest from '@/network/httRequest.js';

// 用户登录
export function login(user) {
  return httpRequest
    .post('/user/login', user)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log('用户登录失败~~~');
      throw error;
    });
}

//用户注册
export function register(userVO) {
  return httpRequest
    .post('/user/register', userVO)
    .then((response) => response)
    .catch((err) => {
      console.log('用户注册失败~~~');
      throw err;
    });
}

// 用户退出
export function exit(token) {
  return httpRequest
    .post(`/user/exit?token=${token}`)
    .then((response) => {
      return response;
    })
    .catch((error) => {
      throw error;
    });
}
