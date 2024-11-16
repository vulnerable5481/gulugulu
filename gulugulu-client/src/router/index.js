import { createRouter, createWebHashHistory } from 'vue-router';
import { initUserInfo } from '@/apis/userApi/userApi';
// 静态引入
import Index from '@/views/Index.vue';
import NotFound from '@/views/NotFound.vue';
import VideoIndex from '@/views/video/videoIndex.vue';

// 按需引入
const UploadVideo = () => import('@/views/uploadVideo/UploadVideo.vue');

// 路由的默认路径
const routes = [
  // 默认路由
  { path: '/', redirect: ' ' },
  // 首页路由
  { path: '', name: 'index', component: Index, meta: { requestAuth: false } },
  // 投稿路由
  { path: '/upload/video', name: 'uploadVideo', component: UploadVideo, meta: { requestAuth: false } },
  // 视频详情页路由
  { path: '/video/BV:bvId', name: 'videoIndex', component: VideoIndex, meta: { requestAuth: false } },
  // 404路由
  {
    path: '/404notfound',
    name: 'notFound',
    component: NotFound,
    meta: { requestAuth: false },
  },
];

// 创建路由对象
const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

// 全局前置守卫
router.beforeEach((to, from, next) => {
  initUserInfo();

  next();
});

// 全局后置守卫

// 暴露路由
export default router;
