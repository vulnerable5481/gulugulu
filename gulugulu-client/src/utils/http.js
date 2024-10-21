import axios from "axios";

const AxiosService = axios.create({
  baseURL: "/api",
  timeout: 10 * 100000, //请求超时时间
  headers: { "Content-Type": "application/json;charset=UTF-8" },
});

// 请求拦截器
AxiosService.interceptors.request.use(
  (config) => {
    //根据你的后端业务来写,如果权限验证，token
    return config;
  },
  (err) => {
    //若出现错误，则直接报错
    Promise.reject(err);
  }
);

// 响应拦截器
AxiosService.interceptors.response.use(
  (res) => {
    // 这里用于处理返回的结果，比如如果是返回401无权限，可能会是跳回到登录页的操作，结合自己的业务逻辑写
    // 一定结合自己的后端的返回代码进行操作
    // res通常包含后端返回的主要数据。如果你只关心后端返回的数据而不需要访问响应的其他元信息（如状态码、响应头等），那么返回 res.data 会使代码更加简洁。
    return res;
  },
  (err) => {
    // 会将错误传递到调用该请求的代码中，便于后续处理错误。
    return Promise.reject(err);
  }
);

export default AxiosService;
