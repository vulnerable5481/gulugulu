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
  // 控制每一次跳转后的滚动位置
  scrollBehavior(to, from, savedPosition) {
    // 管他呢，一律顶部完事
    return { top: 0 };
    // // 判断目标路由是否是视频详情页路由
    // if (to.name === 'videoIndex') {
    //   // 如果是视频详情页，必须滚动到页面顶部
    //   return { top: 0 };
    // }

    // // 默认:滚动到顶部/保持之前的滚动位置
    // if (savedPosition) {
    //   // 保持之前的滚动位置
    //   return savedPosition;
    // } else {
    //   // 默认滚动到顶部
    //   return { top: 0 };
    // }
  },
});

// 全局前置守卫
router.beforeEach((to, from, next) => {
  initUserInfo();

  next();
});

// 全局后置守卫

// 暴露路由
export default router;
