import { createRouter, createWebHashHistory } from "vue-router"

const Index = () => import('../views/Index.vue')


//路由的默认路径
const routes = [
  //默认路由
  { path: "/", redirect: " " },
  { path: "", name: "index", component: Index, meta: { requestAuth: false } },
]

//创建路由对象
const router = createRouter({
  history: createWebHashHistory(),
  routes
})


//暴露路由
export default router