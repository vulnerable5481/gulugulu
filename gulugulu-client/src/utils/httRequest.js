import AxiosService from "./http";

const httpRequest = {
  //封装 GET 请求
  get(url, params = {}) {
    //确保每次请求 URL 都是独一无二的，避免因缓存导致的问题
    params._t = Date.now();
    //这里的 { params } 会被 Axios 自动识别添加到请求 URL 上
    return AxiosService.get(url, { params })
      .then((response) => response)
      .catch((error) => {
        console.error("GET 请求失败:", error);
        throw error; //抛出错误，拱调用者处理
      });
  },
  //封装 POST 请求
  //POST请求需要在 body里面放入数据
  post(url, data = {}) {
    //不需要时间戳
    return AxiosService.post(url, data)
      .then((response) => response)
      .catch((error) => {
        console.log("POST请求失败:", error);
        throw error;
      });
  },
};

export default httpRequest;
