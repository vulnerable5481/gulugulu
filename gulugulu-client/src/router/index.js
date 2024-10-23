import { createRouter, createWebHashHistory } from "vue-router";

const Index = () => import("../views/Index.vue");
const NotFound = () => import("../views/NotFound.vue");

//路由的默认路径
const routes = [
  //默认路由
  { path: "/", redirect: " " },
  //首页路由
  { path: "", name: "index", component: Index, meta: { requestAuth: false } },
  //404路由
  {
    path: "/404notfound",
    name: "notFound",
    component: NotFound,
    meta: { requestAuth: false },
  },
];

//创建路由对象
const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

//暴露路由
export default router;
