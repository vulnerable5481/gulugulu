<template>
  <div class="videoIndex">
    <HeaderBar :isFixedHeaderBar="true"></HeaderBar>
    <div class="video-container">
      <div class="left-container">
        <!-- 标题 -->
        <div class="video-info-container">
          <div class="video-info-title">{{ videoInfo.title }}</div>
          <div class="video-info-detail-list">
            <div class="detail-list-broadcast">
              <i class="gulu-kaishi iconfont"></i>
              <span class="video-info-detail-list-text">1.9万</span>
            </div>
            <div class="detail-list-comments">
              <i class="gulu-danmu1 iconfont" style="font-size: 14px"></i>
              <span class="video-info-detail-list-text">5</span>
            </div>
            <div class="detail-list-date">2024-11-01 12:28:40</div>
            <div class="detail-list-tyle" v-if="videoInfo.type == 0">
              <i class="gulu-jinzhi iconfont" style="margin-left: 30px; color: red"></i>
              <span style="margin-left: 5px">未经作者授权，禁止转载</span>
            </div>
          </div>
        </div>
        <!-- 视频实体 -->
        <!-- <div class="test"></div> -->
        <GuluPlayer ref="guluPlayerRef" :danmuList="danmuList" :isDanmuOpen="isDanmuOpen" :videoInfo="videoInfo"></GuluPlayer>
        <div class="video-sending-bar">
          <div class="sending-info">1人正在观看，已装填 60 条弹幕</div>
          <div class="danmu-info">
            <i
              class="gulu-bofangqi-danmukai iconfont"
              :class="isDanmuOpen ? 'danmu-control-active' : 'danmu-control-dead'"
              v-if="isDanmuOpen"
              @click="changeDanmu"
            ></i>
            <i
              class="gulu-bofangqi-danmuguan iconfont"
              :class="isDanmuOpen ? 'danmu-control-active' : 'danmu-control-dead'"
              v-if="!isDanmuOpen"
              @click="changeDanmu"
            ></i>
            <div class="danmu-control-set">
              <i class="gulu-bofangqi-danmugundongkai iconfont danmu-set"></i>
              <i class="gulu-erciyuanb iconfont danmu-inner"></i>
            </div>
          </div>
          <div class="danmu-send-bar">
            <div class="danmu-send-wrap">
              <div class="danmu-color">
                <i class="gulu-shequhuodong iconfont" style="font-size: 18px"></i>
              </div>
              <input class="danmu-input" v-model="danmuContent" type="text" placeholder="发个友善的弹幕见证当下" />
              <button class="danmu-send-btn" @click="handleSendDanmu">发送</button>
            </div>
          </div>
        </div>
        <div class="tool-bar">
          <div class="tool-bar-wrap">
            <div class="tool-item">
              <i class="gulu-dianzan_kuai iconfont tool-default-entry"></i>
              <span>1.2万</span>
            </div>
            <div class="tool-item">
              <i class="gulu-toubix iconfont tool-default-entry"></i>
              <span>6058</span>
            </div>
            <div class="tool-item">
              <i class="gulu-shoucang iconfont tool-default-entry"></i>
              <span>5478</span>
            </div>
            <div class="tool-item">
              <i class="gulu-zhuanfa iconfont tool-default-entry"></i>
              <span>2478</span>
            </div>
          </div>
          <div class="tool-bar-love">
            <i class="gulu-TsurugiHina iconfont"></i>
          </div>
        </div>
        <div class="video-desc-container">
          <span class="video-desc-content" :style="{ maxHeight: isExpanded ? '1000px' : '84px' }">
            {{ videoInfo.description == '' ? '-' : videoInfo.description }}
          </span>
          <div class="video-desc-btn" v-if="isNeedExpanded" @click="handleExpand">
            <span>{{ isExpanded ? '收起' : '展开更多' }}</span>
          </div>
          <div class="video-tags" v-if="tags.length > 0">
            <div class="tag-item" v-for="(tag, index) in tags" id="index">
              {{ tag }}
            </div>
          </div>
        </div>
        <div class="video-comment-container">
          <div class="comment-header">
            <div class="comment-header-nav">
              <div class="comment-header-wrap">
                <div class="nav-title">评论<span class="nav-number">5547</span></div>
                <div
                  class="nav-hot nav-active"
                  @click="changeCommentType(true)"
                  :style="{ color: commentType == 0 ? 'black' : 'var(--GR3)' }"
                >
                  最热
                </div>
                <div
                  class="nav-new nav-active"
                  @click="changeCommentType(false)"
                  :style="{ color: commentType != 0 ? 'black' : 'var(--GR3)' }"
                >
                  最新
                </div>
              </div>
            </div>
            <div class="comment-box">
              <div class="user-avatar">
                <img :src="userInfo.avatar" alt="" />
              </div>
              <div class="comment-wrap" @click="isCommenting = true" @blur="isCommenting = false">
                <div class="comment-area">
                  <textarea
                    v-model="rootContent"
                    class="comment-input"
                    type="text"
                    placeholder="wifi连接中......检测到粉丝评论输出电波......"
                    @click="
                      CommentMatrix_X = -1;
                      CommentMatrix_Y = -1;
                    "
                    @input="handleComment"
                  ></textarea>
                </div>
                <div class="comment-tool" v-if="isCommenting">
                  <div class="comment-expression">
                    <div class="item"><i class="gulu-biaoqing iconfont comment-default-entry"></i></div>
                    <div class="item"><i class="gulu-aite iconfont comment-default-entry"></i></div>
                    <div class="item"><i class="gulu-tupian1 iconfont comment-default-entry"></i></div>
                    <div class="comment-send-btn" @click="handleSendComment(-1, -1)">发送</div>
                  </div>
                </div>
              </div>
            </div>
            <!-- 评论树 -->
            <div class="comment-list">
              <div class="comment-item" ref="commentList" v-for="(comment, index) in commentTree" :id="'comment-' + index">
                <div class="comment-avatar">
                  <img :src="comment.userAvatar" alt="" />
                </div>
                <div class="comment-content-container">
                  <div class="comment-content-header">
                    <div class="user-nickName">{{ comment.userName }}</div>
                    <img class="user-level" src="@/assets/img/grade/level_2.svg" alt="" />
                  </div>
                  <div class="comment-content">{{ comment.content }}</div>
                  <div class="comment-action-btn">
                    <div class="comment-time">{{ comment.createTime }}</div>
                    <div class="comment-like-container">
                      <div class="comment-like"><i class="gulu-dianzan iconfont"></i></div>
                      <div class="comment-like-count">{{ comment.likeCount }}</div>
                    </div>
                    <div class="comment-dislike"><i class="gulu-diancai iconfont"></i></div>
                    <div
                      class="comment-response"
                      @click="
                        CommentMatrix_X = index;
                        CommentMatrix_Y = 0;
                      "
                    >
                      <span>回复</span>
                    </div>
                  </div>
                  <!-- 回复框 -->
                  <div class="comment-box2" v-if="CommentMatrix_X === index && CommentMatrix_Y == 0">
                    <div class="user-avatar">
                      <img :src="userInfo.avatar" alt="" />
                    </div>
                    <div class="comment-wrap">
                      <div class="comment-area">
                        <textarea
                          v-model="response"
                          class="comment-input2"
                          type="text"
                          :placeholder="`回复 @${comment.userName} :`"
                          @input="handleComment()"
                        ></textarea>
                      </div>
                      <div class="comment-expression">
                        <div class="item"><i class="gulu-biaoqing iconfont comment-default-entry"></i></div>
                        <div class="item"><i class="gulu-aite iconfont comment-default-entry"></i></div>
                        <div class="comment-send-btn" @click="handleSendComment(comment, index)">发送</div>
                      </div>
                    </div>
                  </div>
                  <!-- 子评论链表 -->
                  <div class="comment-children">
                    <!-- 第一个子评论 -->
                    <div class="comment-item2" v-if="comment.children?.length >= 1">
                      <div class="comment-content-container2">
                        <div class="comment-content-container2">
                          <div class="comment-content2">
                            <img class="comment-child-avatar" :src="comment.children?.[0].userAvatar" alt="" />
                            <div class="user-nickName2">{{ comment.children?.[0].userName }}</div>
                            <img class="user-level2" src="@/assets/img/grade/level_2.svg" alt="" />
                            {{ comment.children?.[0].content }}
                          </div>

                          <div class="comment-action-btn">
                            <div class="comment-time">{{ comment.children[0].createTime }}</div>
                            <div class="comment-like-container">
                              <div class="comment-like"><i class="gulu-dianzan iconfont"></i></div>
                              <div class="comment-like-count">{{ comment.children[0].likeCount }}</div>
                            </div>
                            <div class="comment-dislike"><i class="gulu-diancai iconfont"></i></div>
                            <div
                              class="comment-response"
                              @click="
                                CommentMatrix_X = index;
                                CommentMatrix_Y = 1;
                              "
                            >
                              <span>回复</span>
                            </div>
                          </div>
                          <div class="comment-box2" v-if="CommentMatrix_X === index && CommentMatrix_Y == 1">
                            <div class="user-avatar">
                              <img :src="userInfo.avatar" alt="" />
                            </div>
                            <div class="comment-wrap">
                              <div class="comment-area">
                                <textarea
                                  v-model="response"
                                  class="comment-input2"
                                  type="text"
                                  :placeholder="`回复 @${comment.children?.[0].userName} :`"
                                  @input="handleComment"
                                ></textarea>
                              </div>
                              <div class="comment-expression">
                                <div class="item"><i class="gulu-biaoqing iconfont comment-default-entry"></i></div>
                                <div class="item"><i class="gulu-aite iconfont comment-default-entry"></i></div>
                                <div class="comment-send-btn" @click="handleSendComment(comment.children[0], index)">发送</div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <!-- 二级子评论 -->
                    <div class="children-wrap" v-if="clickedComments[index]">
                      <div
                        class="comment-item2"
                        v-for="(childComment, childIndex) in comment.children?.slice(1)"
                        :id="'childComment-' + index"
                      >
                        <div class="comment-content-container2">
                          <div class="comment-content2">
                            <img class="comment-child-avatar" :src="childComment.userAvatar" alt="" />
                            <div class="user-nickName2">{{ childComment.userName }}</div>
                            <img class="user-level2" src="@/assets/img/grade/level_2.svg" alt="" />
                            {{ childComment.content }}
                          </div>
                          <div class="comment-action-btn">
                            <div class="comment-time">{{ childComment.createTime }}</div>
                            <div class="comment-like-container">
                              <div class="comment-like"><i class="gulu-dianzan iconfont"></i></div>
                              <div class="comment-like-count">{{ childComment.likeCount }}</div>
                            </div>
                            <div class="comment-dislike"><i class="gulu-diancai iconfont"></i></div>
                            <div
                              class="comment-response"
                              @click="
                                CommentMatrix_Y = childIndex + 2;
                                CommentMatrix_X = index;
                              "
                            >
                              <span>回复</span>
                            </div>
                          </div>
                          <!-- 回复框 -->
                          <div class="comment-box2" v-if="CommentMatrix_X === index && CommentMatrix_Y === childIndex + 2">
                            <div class="user-avatar">
                              <img :src="userInfo.avatar" alt="" />
                            </div>
                            <div class="comment-wrap">
                              <div class="comment-area">
                                <textarea
                                  v-model="response"
                                  class="comment-input2"
                                  type="text"
                                  :placeholder="`回复 @${childComment.userName} :`"
                                  @input="handleComment"
                                ></textarea>
                              </div>
                              <div class="comment-expression">
                                <div class="item"><i class="gulu-biaoqing iconfont comment-default-entry"></i></div>
                                <div class="item"><i class="gulu-aite iconfont comment-default-entry"></i></div>
                                <div class="comment-send-btn" @click="handleSendComment(childComment, index)">发送</div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div v-if="comment.children?.length >= 2 && !clickedComments[index]" class="comment-expand">
                    <span @click="handleExpand(index)" class="comment-text">展示更多</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="right-container"></div>
    </div>
  </div>
</template>

<script setup>
import { getCommentTree, sendComment } from '@/apis/videoApi/commentRequest';
import { listDanmu, sendDanmu } from '@/apis/videoApi/danmuRequest';
import { queryVideo } from '@/apis/videoApi/videoRequest';
import HeaderBar from '@/components/header/HeaderBar.vue';
import GuluPlayer from '@/components/player/guluPlayer.vue';
import { useUserStore } from '@/store';
import { ElMessage } from 'element-plus';
import { computed, nextTick, onMounted, onUnmounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

// 路由
const route = useRoute();
const router = useRouter();

// commentList元素
const commentList = ref();
// 咕噜播放器 dom元素
const guluPlayerRef = ref();

// 是否展示简介
const isExpanded = ref(false);
// 是否需要展示更多按钮
const isNeedExpanded = ref(false);
// 是否播放弹幕
const isDanmuOpen = ref(true);
// 是否正在评论中
const isCommenting = ref(false);
// 加载评论类型 0:热度排序，1:时间排序
const commentType = ref(0);
// 评论矩阵 X轴坐标
const CommentMatrix_X = ref(-1);
// 评论矩阵 Y轴坐标
const CommentMatrix_Y = ref(-1);
// 记录已展开评论的数组
const clickedComments = ref([]);
// 评论树偏移量
const offset = ref(0);
// 要发送弹幕的内容
const danmuContent = ref('');
// 要发送弹幕的类型
const danmuType = ref(1); // 默认滚动
// 弹幕列表
const danmuList = ref([]);

// 记录上次滚动位置
let lastScrollPosition = 500; // 记录上次滚动位置,初始值500,忽略视频播放器的长度
// 节流标记
let isThrottled = false;

// 用户
const userInfo = useUserStore().userInfo;
// 视频ID
const videoId = computed(() => {
  return route.query.videoId;
});
// 视频实体
const videoInfo = reactive({});
// 标签
let tags = ref([]);
// 当前评论内容
let rootContent = ref('');
// 回复评论内容
let response = ref('');
// 评论树
let commentTree = ref([]);
// 热度评论树
let hotTree = ref([]);
// 时间评论树
let timeTree = ref([]);

// 展示/隐藏弹幕
function changeDanmu() {
  isDanmuOpen.value = !isDanmuOpen.value;
  nextTick(() => {
    //虽然上面的代码修改了isDanmuOpen的值，但是没有立刻同步到dom中，需要nextTick来确保DOM在isDanmuOpen更新后同步
    guluPlayerRef.value.showDanmu();
  });
}

// 发送弹幕
function handleSendDanmu() {
  if (!danmuContent.value) {
    ElMessage.error('请填写弹幕内容!');
    return;
  }
  // 构建弹幕实体
  const danmu = {
    videoId: videoId.value,
    userId: userInfo.userId,
    content: danmuContent.value,
    type: danmuType.value,
    time: guluPlayerRef.value?.getVideoRefValue().currentTime,
  };
  // 立即更新当前弹幕列表
  guluPlayerRef.value.displayNewestDanmu(danmuContent.value);
  sendDanmu(danmu).then((response) => {
    ElMessage.success('发送成功!');
    // 清除缓存
    danmuContent.value = '';
  });
}

// 切换评论排序类型
function changeCommentType(isHot) {
  // 重置分页偏移量
  offset.value = 0;
  if (isHot) {
    // 按照热度排序
    commentType.value = 0;
    // timeTree.value = commentTree.value; // save功能 以后再说吧，懒得写了
    initCommentTree(); // 重置评论区
  } else {
    // 按照时间排序
    commentType.value = 1;
    // hotTree.value = commentTree.value; // save功能  以后再说吧，懒得写了
    initCommentTree(); // 重置评论区
  }
}

// 回复评论
function handleResponse(index) {
  console.log('haha:', commentList);
}

// 展开简介
function handleExpandDesc() {
  isExpanded.value = !isExpanded.value;
}

// 展开更多子评论
function handleExpand(index) {
  clickedComments.value[index] = true;
}

// 检测评论内容
function handleComment() {}

// 发送评论
function handleSendComment(comment, x) {
  if (!rootContent.value) {
    ElMessage.error('不许发送空评论~~喵！');
    return;
  }
  // 构建发送数据
  const commentVo = {
    videoId: videoId.value,
    rootId: x == -1 ? 0 : comment.rootId == 0 ? comment.commentId : comment.rootId,
    userId: userInfo.userId,
    replyId: x == -1 ? 0 : comment.userId,
    content: x == -1 ? rootContent.value : response.value,
  };

  sendComment(commentVo)
    .then(({ data }) => {
      // 页面立即回显
      if (x != -1) {
        if (!commentTree.value[x].children) {
          commentTree.value[x].children = [];
        }
        commentTree.value[x].children.push(data);
        handleExpand(x); // 确保评论展示
      } else {
        commentTree.value.unshift(data); // 根评论直接添加即可
      }
      // 提示信息
      ElMessage.success('发送成功!');
      // 清除缓存、还原评论矩阵
      clearSendComment();
    })
    .catch(() => {
      ElMessage.error('发送失败!');
    });
}
// 清除发送评论后的缓存
function clearSendComment() {
  // 还原评论矩阵
  CommentMatrix_X.value = -1;
  CommentMatrix_Y.value = -1;
  // 清除评价内容
  response.value = '';
  rootContent.value = '';
}

// 滚动加载评论
function loadComment(e) {
  const scrollPosition = window.scrollY || document.documentElement.scrollTop;
  // console.log(scrollPosition);
  if (scrollPosition - lastScrollPosition >= 500 && !isThrottled) {
    isThrottled = true; // 设置节流标记，防止多次触发
    lastScrollPosition = scrollPosition; // 更新上次滚动位置
    // console.log('上次滚动位置', lastScrollPosition);
    getCommentTree(videoId.value, offset.value, commentType.value)
      .then(({ data, code }) => {
        // 如果code == 501 表示评论已加载完毕
        if (code == 501) {
          window.removeEventListener('scroll', loadComment);
        }
        // 取消节流标记
        isThrottled = false;
        // 更新评论树分页偏移量
        offset.value += 10;
        commentTree.value.push(...data); // push 扩展数据数组
      })
      .catch((error) => {
        console.log(error);
      });
    // 我突然发现设置一个定时器来取消节流标记，还不如直接利用加载评论的这段时间！
    // // 设置定时器来取消节流标记
    // throttleTimeout = setTimeout(() => {
    //   isThrottled = false;
    // }, 1000); // 1000ms后取消节流标记
  }
}

// 初始化
function initVideo() {
  // 获取视频详细信息
  queryVideo(videoId.value).then(({ data, code }) => {
    if (code == 200) {
      Object.assign(videoInfo, data);
      // 处理videoInfo里面的标签数据
      if (videoInfo.tags) {
        tags.value = videoInfo.tags.split(',');
      }
    } else {
      // 处理错误
      router.replace({
        name: 'notFound',
      });
    }
  });
  // 初始化评论区前十条评论
  initCommentTree();
  // 初始化弹幕
  listDanmu(videoId.value).then(({ data }) => {
    danmuList.value = data.data;
  });
}

// 初始化评论区
function initCommentTree() {
  // 加载评论区
  getCommentTree(videoId.value, offset.value, commentType.value).then(({ data }) => {
    commentTree.value = data;
    offset.value += 10;
    window.addEventListener('scroll', loadComment); // 监听事件-滚动加载评论区
    lastScrollPosition = 500; // 初始化滚动距离，默认为500
  });
}

onMounted(() => {
  window.scrollTo(0, 0);
  // 初始化视频详情
  initVideo();
});

onUnmounted(() => {
  // 取消绑定事件
  window.removeEventListener('scroll', loadComment); // 监听事件-滚动加载评论区
});
</script>

<style scoped>
.videoIndex {
  width: 100%;
  height: 2000px;
  scroll-behavior: smooth;
}
.video-container {
  display: flex;
  margin-top: 64px;
  padding: 0 200px;
  width: 100%;
  height: 100%;
}

.left-container {
  max-width: 900px;
  flex: 2.5;
}
.right-container {
  max-width: 400px;
  padding-left: 30px;
  flex: 1;
}

.video-info-container {
  width: 100%;
  height: 8%;
  max-height: 80px;
}

.video-info-title {
  max-width: 770px;
  margin-top: 20px;
  font-size: 20px;
  font-family: '';
  white-space: nowrap; /* 禁止换行 */
  overflow: hidden; /* 超出部分隐藏 */
  text-overflow: ellipsis; /* 显示省略号 */
}

.video-info-detail-list {
  display: flex;
  align-items: center;
  margin-top: 10px;
  margin-left: 15px;
  font-size: 13px;
  color: var(--GR3);
}

.detail-list-broadcast {
  display: flex;
  align-items: center;
}

.video-info-detail-list-text {
  margin-left: 4px;
  margin-right: 15px;
  font-size: 13px;
}

.detail-list-tyle {
  display: flex;
  align-items: center;
}

.test {
  width: 778px;
  height: 422px;
  background-color: black;
}

.video-sending-bar {
  padding-left: 5px;
  display: flex;
  align-items: center;
  width: 100%;
  max-height: 50px;
  height: 5%;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  font-size: 13px;
  color: var(--GR3);
}

.danmu-info {
  display: flex;
  margin-left: 30px;
}

.danmu-control-dead {
  font-size: 30px;
  cursor: pointer;
}
.danmu-control-active {
  font-size: 30px;
  color: var(--PK1);
  cursor: pointer;
}

.danmu-control-set {
  position: relative;
  cursor: pointer;
}
.danmu-control-set:hover {
  color: var(--PK1);
}

.danmu-set {
  margin-left: 10px;
  font-size: 30px;
  transition: all 0.2s;
  cursor: pointer;
}

.danmu-inner {
  position: absolute;
  font-size: 13px;
  bottom: 20%;
  right: 22%;
}

.danmu-send-bar {
  margin-left: 10px;
  width: 60%;
  height: 65%;
}

.danmu-send-wrap {
  display: flex;
  width: 100%;
  height: 100%;
  border-radius: 8px;
  background-color: var(--GR2);
}

.danmu-color {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.danmu-input {
  flex: 7;
  height: 100%;
  border-radius: 8px;
  outline: none;
  border: none;
  color: var(--GR3);
  background-color: var(--GR2);
}

.danmu-send-btn {
  width: 15%;
  height: 100%;
  background-color: var(--Bl2);
  border-top-right-radius: 8px;
  border-bottom-right-radius: 8px;
  border: none;
  outline: none;
  color: #fff;
}

.tool-bar {
  position: relative;
  padding: 10px 0;
  width: 100%;
  border-bottom: 1px solid var(--GR1);
}

.tool-bar-wrap {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 50%;
}

.tool-bar-love {
  position: absolute;
  top: 0;
  right: 3%;
}
.tool-bar-love i {
  font-size: 40px;
  color: var(--Bl2);
}

.tool-item {
  display: flex;
  align-items: center;
  transition: all 0.4s;
}
.tool-item:hover {
  cursor: pointer;
  color: var(--Bl2);
}
.tool-item span {
  padding-left: 8px;
  padding-top: 3px;
}

.tool-default-entry {
  font-size: 27px;
  cursor: pointer;
}

.video-desc-container {
  display: flex;
  flex-direction: column;
  padding-top: 10px;
  padding-bottom: 15px;
  width: 100%;
  height: auto;
  border-bottom: 1px solid var(--GR1);
}

.video-desc-content {
  width: 100%;
  max-height: 84px;
  margin-bottom: 10px;
  white-space: pre-wrap;
  letter-spacing: 0;
  font-size: 15px;
  line-height: 24px;
  overflow: hidden;
  word-break: break-all;
  line-break: anywhere;
  color: var(--text1);
}

.video-desc-btn {
  display: flex;
}
.video-desc-btn span {
  font-size: 14px;
  color: var(--GR4);
  cursor: pointer;
}
.video-desc-btn span:hover {
  color: var(--Bl2);
}

.video-tags {
  display: flex;
  flex-wrap: wrap;
  overflow: hidden;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
  height: 36px;
}

.tag-item {
  display: flex;
  align-items: center;
  justify-self: center;
  margin-top: 8px;
  margin-right: 10px;
  padding: 0 20px;
  height: 28px;
  border-radius: 18px;
  font-size: 14px;
  color: var(--GR4);
  background-color: var(--GR2);
  cursor: pointer;
}

.video-comment-container {
  width: 100%;
}

.comment-header {
  display: flex;
  align-items: center;
  flex-direction: column;
  padding-top: 10px;
}

.comment-header-nav {
  display: flex;
  align-items: center;
  width: 100%;
  height: 50px;
}

.comment-header-wrap {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 25%;
}

.nav-title {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-family: 'microsoft yahei';
  color: var(--text1);
}

.nav-number {
  margin-left: 5px;
  margin-top: 3px;
  font-size: 13px;
  color: var(--GR3);
}

.nav-hot {
  padding-left: 20px;
  padding-right: 15px;
  border-right: 1px solid var(--GR3);
}

.nav-active {
  font-size: 13px;
  margin-top: 3px;
  cursor: pointer;
}
.nav-active:hover {
  color: var(--Bl2) !important;
}

.comment-box {
  display: flex;
  width: 100%;
}

.user-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 50px;
}

.user-avatar img {
  flex-shrink: 0;
  width: 48px;
  height: 48px;
  border-radius: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.comment-area {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.comment-input {
  display: flex;
  align-items: center;
  width: 100%;
  max-height: 100px;
  padding: 10px;
  padding-left: 20px;
  border: none;
  outline: none;
  overflow-y: auto; /* 超过最大高度时显示滚动条 */
  resize: none;
  border-radius: 8px;
  background-color: var(--GR2);
  transition: background-color 0.2s;
  font-family: 'Harmony Font';
  color: var(--text2);
}
.comment-input:hover {
  border: 1px solid #c9ccd0;
  background-color: #fff;
}
.comment-input:focus {
  border: 1px solid #c9ccd0;
  background-color: #fff;
}
.comment-input::placeholder {
  font-family: 'Harmony Font';
  font-size: 14px;
  color: var(--GR3);
  padding-top: 2px;
}

.comment-tool {
  display: flex;
  width: 100%;
  height: 50px;
}

.comment-expression {
  position: relative;
  display: flex;
  width: 100%;
}

.item:first-child {
  margin-left: 5px;
}
.item {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  margin-top: 10px;
  margin-right: 10px;
  border: 1px solid var(--GR1);
  cursor: pointer;
}

.comment-default-entry {
  font-size: 16px;
  color: #61666d;
}

.comment-send-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  width: 80px;
  height: 35px;
  border-radius: 4px;
  position: absolute;
  top: 50%;
  transform: translateY(-30%);
  right: 0;
  background-color: #7fd6f5;
  cursor: pointer;
}
.comment-send-btn:hover {
  background-color: var(--Bl2);
}

.comment-list {
  margin-top: 20px;
  width: 100%;
  height: 500px;
}

.comment-item {
  display: flex;
  width: 100%;
  margin-bottom: 10px;
}
.comment-item2 {
  display: flex;
  width: 100%;
}

.comment-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 50px;
}
.comment-avatar img {
  width: 48px;
  height: 48px;
  border-radius: 24px;
}
.comment-avatar2 {
  padding-right: 10px;
  display: flex;
  justify-content: center;
}
.comment-avatar2 img {
  width: 30px;
  height: 30px;
  border-radius: 15px;
}
.comment-child-avatar {
  padding-bottom: 3px;
  width: 30px;
  height: 30px;
  border-radius: 15px;
}

.comment-content-container {
  width: 100%;
  height: 100%;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--GR1);
}
.comment-content-container2 {
  width: 100%;
  height: 100%;
}

.comment-content-header {
  display: flex;
  align-items: center;
  width: 100%;
  height: 35px;
}

.user-nickName {
  display: flex;
  align-items: center;
  height: 35px;
  font-size: 14px;
  color: var(--GR5);
}
.user-nickName2 {
  padding-left: 3px;
  display: inline-block;
  font-size: 14px;
  color: var(--GR5);
}

.user-level {
  padding-left: 4px;
  width: 35px;
  height: 35px;
}
.user-level2 {
  padding-left: 4px;
  width: 35px;
  height: 35px;
}

.comment-content {
  max-height: 400px;
  color: var(--text2);
  font-size: 15px;
  line-height: 24px;
}
.comment-content2 {
  max-height: 400px;
  color: var(--text2);
  font-size: 15px;
  line-height: 18px;
}

.comment-action-btn {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 5px;
  margin-bottom: 10px;
  width: 37%;
  height: 20%;
  color: var(--GR3);
}
.comment-action-btn2 {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 5px;
  margin-left: 65px;
  margin-bottom: 10px;
  width: 37%;
  height: 20%;
  color: var(--GR3);
}

.comment-time {
  color: var(--GR3);
  font-size: 13px;
}

.comment-like-container {
  display: flex;
  align-items: center;
  cursor: pointer;
}
.comment-like-container:hover {
  color: var(--Bl2) !important;
}

.comment-like {
  cursor: pointer;
}

.comment-like-count {
  margin-left: 5px;
  font-size: 13px;
  color: var(--GR4);
}

.comment-dislike i {
  font-size: 13px;
}

.comment-dislike:hover {
  color: var(--Bl2);
}

.comment-response {
  cursor: pointer;
  color: var(--GR4);
}
.comment-response:hover {
  color: var(--Bl2);
}

.comment-box2 {
  display: flex;
}

.comment-wrap {
  width: 100%;
}

.comment-input2 {
  display: flex;
  align-items: center;
  width: 100%;
  max-height: 100px;
  padding: 10px;
  padding-left: 20px;
  border: none;
  outline: none;
  overflow-y: auto; /* 超过最大高度时显示滚动条 */
  resize: none;
  border-radius: 8px;
  background-color: #fff;
  border: 1px solid var(--GR3);
  color: var(--text2);
  transition: background-color 0.2s;
  font-family: 'Harmony Font';
}

.comment-expand span {
  cursor: pointer;
}

.comment-text {
  color: var(--GR3);
}
</style>
