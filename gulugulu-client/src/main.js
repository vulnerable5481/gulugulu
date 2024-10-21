import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

import "./assets/fonts/iconfont.css";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import { createPinia } from "pinia";
// 全局样式表
import "./assets/css/base.css";

const app = createApp(App);
const pinia = createPinia();

app.use(router);
app.use(ElementPlus);
app.use(pinia);

app.mount("#app");
