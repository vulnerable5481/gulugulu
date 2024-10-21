<template>
  <div class="gulu-header-channel">
    <div class="channel-icons">
      <a href="#">
        <img src="@/assets/img/header-channel/p5r.png" alt="古咕噜咕噜~~" />
        <span>动态</span>
      </a>
      <a href="#">
        <div class="icon_populor">
          <i
            class="gulu-remen iconfont"
            style="font-size: 25px; color: #fff"
          ></i>
        </div>
        <span>热门</span>
      </a>
    </div>
    <div class="channel-container">
      <ul class="channel-container-left">
        <li class="channel-link">
          <a href="#"><span>番剧</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>国创</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>综艺</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>动画</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>鬼畜</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>舞蹈</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>娱乐</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>科技</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>美食</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>汽车</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>运动</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>电影</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>电视剧</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>纪录片</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>游戏</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>音乐</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>影视</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>知识</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>咨询</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>生活</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>时尚</span></a>
        </li>
        <li class="channel-link">
          <a href="#"><span>更多</span></a>
          <i
            class="gulu-gengduo iconfont"
            style="font-size: 10px; padding-top: 5px; padding-left: 3px"
          ></i>
        </li>
      </ul>
      <ul class="channel-container-right">
        <li>
          <a href="#"
            ><i
              class="gulu-icon-zhuanlan-zhuanlanwenzhang iconfont"
              style="font-size: 23px"
            ></i
            ><span>专栏</span></a
          >
        </li>
        <li>
          <a href="#"
            ><i class="gulu-huodong1 iconfont"></i><span>活动</span></a
          >
        </li>
        <li>
          <a href="#"
            ><i class="gulu-shequhuodong iconfont"></i><span>社区中心</span></a
          >
        </li>
        <li>
          <a href="#"><i class="gulu-15 iconfont"></i><span>直播</span></a>
        </li>
        <li>
          <a href="#"><i class="gulu-ketang1 iconfont"></i><span>课堂</span></a>
        </li>
        <li>
          <a href="#"
            ><i class="gulu-nav_xingerebang iconfont"></i
            ><span>新歌热榜</span></a
          >
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.gulu-header-channel {
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin: 0 auto;
  width: 95%;
  height: 110px;
}

.channel-icons {
  display: flex;
  align-items: center;
  justify-content: space-around;
  flex: 1;
  height: 72px;
}

.channel-icons a {
  display: flex;
  align-items: center;
  flex-direction: column;
}

.channel-icons span {
  display: flex;
  justify-content: center;
  font-size: 14px;
  color: #18191c;
}

.channel-icons img {
  width: 50px;
  height: 50px;
  border-radius: 25px;
}

.icon_populor {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  border-radius: 25px;
  background-color: #f07775;
}

.channel-container {
  display: flex;
  flex: 9;
  height: 66px;
}

.channel-container-left {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  align-items: center;
  flex: 8;
  padding-right: 20px;
  border-right: 1px solid #e2e4e6;
}

.gulu-header-channel .channel-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 5.5rem;
  height: 26px;
  line-height: 26px;
  border-radius: 5px;
  background-color: #f6f7f8;
  transition: all 0.3s;
}

@media (max-width: 1440px) {
  .gulu-header-channel .channel-link {
    width: 4.5rem;
  }
}

.gulu-header-channel .channel-link:hover {
  background-color: #e2e4e6;
}

.gulu-header-channel .channel-link a {
  color: #61666d;
}

.channel-container-right {
  display: flex;
  align-items: center;
  justify-content: space-around;
  flex-wrap: wrap;
  flex: 2;
}

.channel-container-right li {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 26px;
}

.channel-container-right li a {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #61666d;
}

.channel-container-right li a i {
  margin-right: 5px; /* 增加图标和文本之间的间距 */
}
</style>
