const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  //关闭严格模式
  lintOnSave: false,
  transpileDependencies: true,
  //配置
  devServer: {
    open: true,
    host: "localhost",
    port: 9090,
    https: false,
    proxy: {
      // 配置跨域
      "/api": {
        target: "http://localhost:10001",
        ws: true, //允许代理websocked相关的请求
        changeOrigin: true,
        pathRewrite: {
          "^/api": "",
        },
      },
    },
  },
});
