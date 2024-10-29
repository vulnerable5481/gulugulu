import httpRequest from "@/network/httRequest.js";

// 用户登录
export function login(user) {
  return httpRequest
    .post("/user/login", user)
    .then((response) => response.data)
    .catch((error) => {
      console.log("用户登录失败~~~");
      throw error;
    });
}

//用户注册
export function register(userVO) {
  return httpRequest
    .post("/user/register", userVO)
    .then((response) => response)
    .catch((err) => {
      console.log("用户注册失败~~~");
      throw err;
    });
}
