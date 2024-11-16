<template>
  <!-- <div>
    <input type="file" ref="fileInput" @change="handleupload" />
  </div> -->
  <div class="upload-index">
    <!-- TODO:凑空做一个动画效果，只要是上传失败就搞一个飞过去的效果 -->
    <img class="errorImg" style="display: none" src="@/assets/img/girl.gif" alt="" />
    <!-- 头部 -->
    <div class="header">
      <div class="left-block">
        <i class="gulu-TsurugiHina1 iconfont" style="font-size: 50px; color: var(--Bl4)"></i>
        <span class="create-font">创作中心</span>
        <i class="gulu-bilibili1 iconfont main-site" style="color: #757575"></i>
        <span class="site-font">主站</span>
      </div>
      <div class="center-block"></div>
      <div class="right-block">
        <img class="avatar" :src="userAvatar" alt="咕噜~~" />
        <div class="tips-calendar">
          <span>成为up主的第457天</span>
        </div>
        <i class="gulu-xiaoxi iconfont message"></i>
        <i class="gulu-xiazai iconfont upload"></i>
      </div>
    </div>
    <!-- 视频投稿 -->
    <div class="upload-body">
      <!-- 侧边栏 -->
      <div class="sidebar">
        <div class="nav-upload-btn">
          <button>
            <i class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-09 iconfont btn-icon"></i>
            <span class="btn-font">投稿</span>
          </button>
        </div>
      </div>
      <!-- 投稿模块 -->
      <div class="up-wrap">
        <!-- 上传视频 -->
        <div class="up-body" v-if="!editedVisible">
          <ul class="up-nav">
            <li>
              <a href="/#/upload/video"><span class="up-nav-font">视频投稿</span></a>
            </li>
            <li>
              <a href="/#/404notfound"><span class="up-nav-font">音频投稿</span></a>
            </li>
            <li>
              <a href="/#/404notfound"><span class="up-nav-font">专栏投稿</span></a>
            </li>
          </ul>
          <div class="up-main">
            <div class="up-banner">
              <div class="up-drap-body">
                <i class="gulu-heiseBzhandaohangtubiao-lunkuohuamiaobian-09 iconfont drap-iconfont"></i>
                <span class="drap-font">拖拽到此处可以上传</span>
                <button class="up-btn" @click="triggerFileInput">上传视频</button>
                <input type="file" ref="fileInput" @change="handleUpload" style="display: none" />
              </div>
              <div class="up-footer">
                <div class="up-footer-protocol">
                  <span>上传视频，即表示您已同意</span>
                  <a href="/#/404notFound" style="color: var(--Bl4)">咕噜咕噜使用协议</a>
                  <span>与</span>
                  <a href="/#/404notFound" style="color: var(--Bl4)">咕噜咕噜社区公约</a>
                  <span>请勿上传色情，反动等违法视频</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 编辑视频投稿 -->
        <div class="sub-body" v-if="editedVisible">
          <div class="sub-title">
            <div class="sub-title-text">发布视频</div>
          </div>
          <div class="task-queue">
            <div class="task-list-content">
              <div class="task-list-content-item">
                <span class="task-selected">{{ videoSubmission.title }}</span>
                <span class="task-upload-text">上传中...</span>
              </div>
            </div>
            <div class="file-item">
              <i class="gulu-leimujianying iconfont" style="font-size: 40px; color: #13227a"></i>
              <div class="file-item-content">
                <div class="file-item-content-detail">
                  <div class="file-item-text">{{ videoSubmission.title }}</div>
                  <div class="file-item-content-status-text">
                    <span class="status-text">已经上传:{{ fileTransfer.trsfSize }}MB/{{ fileTransfer.fileSize }}MB</span>
                    <span>&nbsp;&nbsp;</span>
                    <span class="status-text">当前速度:{{ fileTransfer.speed }}MB/s 剩余时间:{{ fileTransfer.remainingTime }}s</span>
                  </div>
                </div>
                <div class="file-item-content-progress">
                  <div class="file-item-content-bar" :style="{ width: fileTransfer.trsfRate + '%' }"></div>
                </div>
                <div class="file-item-content-operate">
                  <div class="progress-text status-text" style="font-size: 12px">{{ fileTransfer.trsfRate }}%</div>
                  <div class="operate-btn">
                    <i class="gulu-kaishi iconfont operate-btn"></i>
                    <i class="gulu-zhongxinshangchuan iconfont operate-btn"></i>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="sub-form">
            <div class="form-title">
              <span class="sub-title-text">基本设置</span>
            </div>
            <div class="form-item">
              <div class="cover">
                <div class="section-title">
                  <span class="section-title-deg">*</span>
                  <span class="section-title-text">封面</span>
                </div>
                <div class="cover-content">
                  <!-- 用于裁剪视频封面 -->
                  <video ref="videoRef" :src="videoURL" style="display: none"></video>
                  <canvas ref="canvasRef" style="display: none"></canvas>
                  <!-- <img :src="cover" alt="咕噜~~~" /> -->
                  <img :src="cover" alt="咕噜~~~" />
                </div>
                <div class="cover-right">
                  <button class="cover-right-btn" @click="cropedVisible = true">更换封面</button>
                  <span class="cover-right-txt">系统默认选中第一帧为视频封面</span>
                  <span class="cover-right-txt">*默认情况下您的封面将以16:9比例展示</span>
                </div>
              </div>
            </div>
            <div class="form-item">
              <div class="title-container">
                <div class="section-title">
                  <span class="section-title-deg">*</span>
                  <span class="section-title-text">标题</span>
                </div>
                <div class="video-title-content">
                  <div class="input-wrapper" :class="isFocused ? 'input-wrapper-active' : ''">
                    <input
                      class="input-instance"
                      v-model="videoSubmission.title"
                      type="text"
                      placeholder="UP主/节目名+《游戏名》+主要标题+期号"
                      @input="limitInput"
                      @focus="isFocused = true"
                      @blur="isFocused = false"
                    />
                    <div class="input-max-tip">{{ videoSubmission.title.length }}/80</div>
                  </div>
                </div>
              </div>
            </div>
            <div class="form-item">
              <div class="type-container">
                <div class="section-title">
                  <span class="section-title-deg">*</span>
                  <span class="section-title-text">类型</span>
                </div>
                <div class="type-content">
                  <div class="left-type-container" @click="changeVideoType(0)">
                    <div class="type-box">
                      <div class="type-box-circle" :class="videoSubmission.type == 0 ? 'type-box-active' : ''"></div>
                    </div>
                    <span class="type-box-font" :class="videoSubmission.type == 0 ? 'type-box-font-active' : ''">自制</span>
                  </div>
                  <div class="right-type-container" @click="changeVideoType(1)">
                    <div class="type-box">
                      <div class="type-box-circle" :class="videoSubmission.type == 1 ? 'type-box-active' : ''"></div>
                    </div>
                    <span class="type-box-font" :class="videoSubmission.type == 1 ? 'type-box-font-active' : ''">转载</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="form-item">
              <div class="section-container">
                <div class="section-title">
                  <span class="section-title-deg">*</span>
                  <span class="section-title-text">分区</span>
                </div>
                <div class="section-content">
                  <el-cascader :options="options" v-model="selectedCatagory" @change="changeCatagory" />
                </div>
              </div>
            </div>
            <div class="form-item">
              <div class="tag-container">
                <div class="section-title tag-title">
                  <span class="section-title-deg">*</span>
                  <span class="section-title-text">标签</span>
                </div>
                <div class="tag-content">
                  <div class="tag-input-wrap">
                    <input
                      class="tag-input"
                      v-model="tagInput"
                      type="text"
                      @keydown.enter="handleEnter"
                      placeholder="按回车键Enter创建标签"
                    />
                  </div>
                  <div class="recoment-tag-container">
                    <span class="recoment-tag">推荐标签:</span>
                    <div class="recoment-tag-list">
                      <div class="recoment-tag-item" @click="addTags(tag)" v-for="(tag, index) in recomentTags" key="index">
                        {{ tag }}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="form-item">
              <div class="desc-container">
                <div class="section-title">
                  <span class="section-title-deg" style="visibility: hidden">*</span>
                  <span class="section-title-text">简介</span>
                </div>
                <div class="desc-content">
                  <div class="desc-wrap">
                    <textarea class="desc-textarea" v-model="videoSubmission.description" @change="limitDesc"></textarea>
                    <div class="desc-size">{{ videoSubmission.description.length }} / 2000</div>
                  </div>
                </div>
              </div>
            </div>
            <div class="sub-btn-container">
              <button class="sub-btn" @click="handleuploadVideo">立即投稿</button>
            </div>
          </div>
        </div>
      </div>
      <!-- 裁剪模块 -->
      <Crop :cropedVisible="cropedVisible" @closeCrop="closeCrop"></Crop>
    </div>
  </div>
</template>

<script setup>
import { uploadVideoChunk } from '@/apis/uploadApi/videoApi';
import SparkMD5 from 'spark-md5';
import { computed, nextTick, onMounted, reactive, ref } from 'vue';
import { v4 as uuidv4 } from 'uuid';
import { useUserStore } from '@/store';
import { ElMessage } from 'element-plus';
import { getCatagoryTree } from '@/apis/catagoryApi/catagoryRequest';
import Crop from '@/components/crop/Crop.vue';
import { uploadImg } from '@/apis/uploadApi/uploadRequest';
import { saveVideo } from '@/apis/videoApi/videoRequest';
import { useRouter } from 'vue-router';

// pinia
const store = useUserStore();
// 路由
const router = useRouter();
// 是否展示编辑投稿视频
let editedVisible = ref(false);
// 是否展示裁剪投稿封面
let cropedVisible = ref(false);
// 文件input DOM
const fileInput = ref();
// video DOM
const videoRef = ref();
// canvasRef DOM
const canvasRef = ref();

// 定义分片传输单位
const chunkSize = 1024 * 1024 * 8; //按照8MB为基本单位切片
// 分片集合
let chunks = [];
// 选中分区
let selectedCatagory = ref([]);
// 绑定分区
const options = ref([]);
// 分类树
let tree = ref([]);
// 推荐标签集合
let recomentTags = ref([]);
// 输入框标签集合
let tags = ref([]);
// 标签输入框文本值
let tagInput = ref('');
// 视频临时url  【上传的视频的内存地址】
let videoURL = ref('');
// 视频帧集合
let frames = reactive([]);
// sessionId
let sessionId = ref('');
// 默认视频第一帧为封面
let cover = ref(''); // 初始封面url
let coverBlob = ref(''); // 初始封面的二进制文件流
// 用户头像
const userAvatar = computed(() => {
  return store.userInfo.avatar;
});
// 是否激活标题文本框
let isFocused = ref(false);

// 文件传输信息实体
let fileTransfer = reactive({
  duration: 0, // 视频长度
  fileSize: 0, // 视频大小
  remainingTime: 0, // 剩余时间
  trsfRate: 0, // 已上传文件大小的百分比 【注意：计算需/100】
  trsfSize: 0, // 已上传文件大小
  speed: 3, // 传输速率
});
// 投稿视频实体
let videoSubmission = reactive({
  title: '',
  type: 0,
  duration: 0,
  videoUrl: '',
  coverUrl: '',
  tags: '',
  description: '',
});

// websocket更新进度条
const websocket = new WebSocket('ws://localhost:9090/api/processWs');
websocket.onopen = function (event) {
  console.log('进度条通信建立');
};
websocket.onclose = function (event) {
  console.log('进度条通信关闭');
  websocket.close();
};
websocket.onmessage = function (event) {
  if (event.data.length > 10) {
    sessionId.value = event.data; // 如果长度超过10，保存 sessionId
  } else {
    fileTransfer.trsfRate = event.data; // 否则保存 trsfRate
    // 计算剩余上传时间  parseFloat是为了将 保留两位小数的字符串再变回数字,未来可能需要计算真实的上传速度
    fileTransfer.remainingTime = parseFloat(((fileTransfer.fileSize * (1 - fileTransfer.trsfRate / 100)) / 3).toFixed(2));
    // 计算已上传文件大小
    fileTransfer.trsfSize = parseFloat((fileTransfer.trsfRate / 100) * fileTransfer.fileSize).toFixed(2);
  }
};

// 限制投稿标题字数
function limitInput() {
  if (videoSubmission.title.length > 80) {
    videoSubmission.title = videoSubmission.title.slice(0, 80);
  }
}

// 限制简介字数
function limitDesc() {
  if (videoSubmission.description.length >= 2000) {
    ElMessage.error('简介不能大于2000字！');
    videoSubmission.description = videoSubmission.description.slice(0, 2000);
  }
}

// 设置视频类型
function changeVideoType(type) {
  //type : 0是自制，1是转载
  videoSubmission.type = type;
}

// 触发修改分区，更新推荐标签
function changeCatagory() {
  if (selectedCatagory.value) {
    const targetName = selectedCatagory.value[1]; // 第二个是二级分类
    let target = '';
    // 获取二级分类
    tree.value.forEach((item) => {
      const children = item.children;
      if (children) {
        for (let i = 0; i < children.length; i++) {
          if (children[i].catryName == targetName) {
            target = children[i];
          }
        }
      }
    });
    // 切割字符串，获取推荐标签
    if (target.tags) {
      recomentTags.value = target.tags.split(',');
    }
  }
}

// 触发回车键，生成标签
function handleEnter() {
  if (tags.value.length >= 10) {
    ElMessage.error('标签最多十个啦！魔理沙装不下惹~');
    return;
  }

  if (tagInput.value.trim()) {
    let isRepeated = false; // 标签是否重复
    const newTag = tagInput.value.trim();
    tags.value.forEach((tag) => {
      if (tag == newTag) {
        isRepeated = true;
        return;
      }
    });

    // 如果重复则报错
    if (isRepeated) {
      ElMessage.error('标签不能重复！！！');
      return;
    }

    // 记录标签
    tags.value.push(newTag);
    if (!videoSubmission.tags) {
      videoSubmission.tags = newTag;
    } else {
      videoSubmission.tags += ',' + newTag;
    }

    // 清空输入框
    tagInput.value = '';
  }
}

// 添加推荐标签
function addTags(tag) {
  if (tags.value.length >= 10) {
    ElMessage.error('标签最多十个啦！魔理沙装不下惹~');
    return;
  }

  const newTag = tag.trim();
  if (newTag) {
    let isRepeated = false; // 标签是否重复
    tags.value.forEach((item) => {
      if (item == newTag) {
        isRepeated = true;
        return;
      }
    });

    // 如果重复则报错
    if (isRepeated) {
      ElMessage.error('标签不能重复！！！');
      return;
    }

    // 记录标签
    tags.value.push(newTag);
    if (!videoSubmission.tags) {
      videoSubmission.tags = newTag;
    } else {
      videoSubmission.tags += ',' + newTag;
    }
  }
}

// 关闭裁剪区域
function closeCrop() {
  console.log('关闭');
  cropedVisible.value = false;
}

// 初始化视频封面
function initCover(file) {
  let url = '';
  const canvas = canvasRef.value; // 获取画布元素
  const video = videoRef.value; // 获取视频元素

  videoURL.value = URL.createObjectURL(file); // 保存临时视频内存地址(即url)
  video.crossOrigin = 'anonymous'; // 设置跨域 【声明在赋值url之前】
  const ctx = canvas.getContext('2d'); // 获取canvas的2D绘图上下文
  video.muted = true; // 静音操作，防止声音破坏用户体验

  // 等待视频元数据加载完成
  video.addEventListener('loadedmetadata', () => {
    // 获取时长
    videoSubmission.duration = Math.round(video.duration); // 四舍五入
    // 图片大小等同于视频大小
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    // 视频跳转到第3秒,默认以第三秒为封面
    video.currentTime = 3;
  });

  // 等待视频跳转到指定时间点
  video.addEventListener('seeked', async () => {
    // 视频帧绘制到canvas
    ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
    // 生成 视频帧图片url
    url = canvas.toDataURL('image/jpeg');
    // 切割图片，完成图片适配操作
    const blob = await cropCover(url);
    // 返回裁剪后的帧图片作为封面
    coverBlob.value = blob;
  });
}

// 裁剪视频帧
function cropCover(url) {
  return new Promise((resolve) => {
    // 创建一个Image元素来加载url
    const img = new Image();
    img.src = url;
    // 获取用于裁剪的canvas
    const canvas = canvasRef.value;
    const ctx = canvas.getContext('2d');
    // 定义标准宽高比
    const targetRate = 16 / 9; // 经过测试16/9的效果优于4/3
    // 当图片加载完成后，进行裁剪适配操作
    img.onload = () => {
      // 计算视频的原始宽高
      const srcWidth = img.width;
      const srcHeight = img.height;
      const srcRate = srcWidth / srcHeight;
      // 定义最终宽高
      let targetWidth = srcWidth;
      let targetHeight = srcHeight;
      if (srcRate <= targetRate) {
        // 如果原始比例 < 标准宽高比 (宽短高长)
        targetHeight = targetWidth / targetRate;
      } else {
        // 如果原始比例 > 标准宽高比 (宽长高短)
        targetWidth = targetHeight * targetRate;
      }
      // 计算裁剪后的图片偏移量
      const offsetX = (srcWidth - targetWidth) / 2;
      const offsetY = (srcHeight - targetHeight) / 2;
      // 绘制新图片
      ctx.drawImage(img, offsetX, offsetY, targetWidth, targetHeight, 0, 0, targetWidth, targetHeight);
      // 返回新图片给页面
      cover.value = canvas.toDataURL('image/jpg');
      // 创建图片二进制文件
      canvas.toBlob((blob) => {
        if (blob) {
          resolve(new File([blob], 'temp', { type: 'image/jpeg' }));
        } else {
          reject(new Error('Blob 创建失败'));
        }
      }, 'image/jpeg');
    };
  });
}

// 上传投稿
function handleuploadVideo() {
  const rate = fileTransfer.trsfRate;
  if (rate == 100) {
    let formData = new FormData();
    formData.append('cover', coverBlob.value, videoSubmission.title + '.jpg');
    // 上传封面
    uploadImg(formData).then(({ data }) => {
      // 等待封面上传成功，上传稿件
      videoSubmission.coverUrl = data;
      // 上传稿件
      saveVideo(videoSubmission).then(({ data }) => {
        if (data.code == 200) {
          ElMessage.success('视频投稿成功，请耐心等待审核通过');
          // 强制重新加载页面
          window.location.reload();
          // 确保页面滚动到顶部
          window.scrollTo(0, 0);
        }
      });
    });
  } else {
    ElMessage.warning('请在视频传输完毕后上传视频!');
  }
}

// 触发上传事件
function triggerFileInput() {
  fileInput.value.click();
}

// 上传文件
async function handleUpload() {
  // 获取文件
  const file = fileInput.value.files[0];
  const fileSizeInMB = (file.size / (1024 * 1024)).toFixed(2); // 转换为 MB，保留两位小数
  const title = file.name.split('.mp4')[0];
  // 视频不能超过1GB
  if (fileSizeInMB >= 1024) {
    // 文件大小违规，驳回上传请求!
    ElMessage.error('视频太大了，魔理沙装不下呜~'); // 【珍惜存储空间，从魔理沙做起】
    return;
  }

  //保存文件信息
  fileTransfer.fileSize = fileSizeInMB;
  videoSubmission.title = title;
  // videoSubmission.duration = file.duration; // 无法直接获取时间,可以从初始化封面获取时长
  const totalChunks = Math.ceil(file.size / chunkSize);

  // 切换内嵌页
  editedVisible.value = true;

  // 初始化视频封面
  nextTick(() => {
    initCover(file);
  });

  // 生成并传输文件分片
  const url = '';
  await fhash(file)
    .then((hash) => {
      //分片单独上传到服务器
      for (let i = 0; i < totalChunks; i++) {
        const chunk = chunks[i];
        const formData = new FormData();
        formData.append('id', i);
        formData.append('data', chunk);
        formData.append('hash', hash);
        formData.append('total', totalChunks);
        formData.append('sessionId', sessionId.value);
        uploadVideoChunk(formData).then(({ data }) => {
          if (data) {
            videoSubmission.videoUrl = data;
          }
        });
        // 【我本来打算根据服务器返回的url，来创建视频帧封面，实际上我可以直接用前端上传的file生成临时url来直接在前端就创作封面！！！】
        // .then(({ data }) => {
        //   if (i == totalChunks - 1) {
        //     // 获取视频url
        //     fileTransfer.url = data;
        //     // 初始化视频封面 【默认选取第一帧当封面,即第0秒时的画面】
        //     //【实际上还有一个更加快速高效的方法来制作视频帧封面，即根据上传的file直接本地制作封面】
        //     // TODO: 等我后面有空新增一个函数，文件如果<300 mb 直接本地制作封面
        //     getVideoBase64(0).then((res) => {
        //       cover.value = res;
        //     });
        //   }
        // })
        // .catch((error) => {
        //   console.log(error);
        // });
        //测试重传
        // // 仅在分片 i 为 20 时不上传
        // if (i === 20 || i === 22 || i === 24) {
        //   console.warn(`分片 ${i} 被跳过`);
        //   continue; // 跳过该分片
        // }
        // uploadVideoChunk(formData).then((response) => {
        //   // 出错，断点续传
        //   if (i == totalChunks - 1) {
        //     const errorIds = response.data.split(',').map(Number); // 转换成数字数组
        //     //  重传错误分片
        //     for (let j = 0; j < errorIds.length; j++) {
        //       const id = errorIds[j];
        //       console.log('重传分片:', id);
        //       const chunk = chunks[id];
        //       const formData = new FormData();
        //       formData.append('id', id);
        //       formData.append('data', chunk);
        //       formData.append('hash', hash);
        //       formData.append('size', chunkSize);
        //       formData.append('total', totalChunks);
        //       uploadVideoChunk(formData);
        //     }
        //     // 重新合并分片
        //     const mergeVo = {
        //       hash: hash,
        //       total: totalChunks,
        //     };
        //     mergeFile(mergeVo);
        //   }
        // });
      }
    })
    .catch((error) => {
      console.error('文件传输时出错:', error);
    });

  // 上传完毕,清除缓存
  chunks = [];
}

// 计算hash,同时生成文件分片   【防止文件过大卡死,采取文件分片计算hash】
//这里是将文件整体读入计算md5，好处是md5碰撞的概率大大降低，缺点是计算时间会长一些；
//如果想计算时间短一些，不追求极致的低碰撞率的话，可以考虑读入第一个切片和最后一个切片,或者读入部分切片进行md5计算。
function fhash(file) {
  return new Promise((resolve, reject) => {
    const spark = new SparkMD5.ArrayBuffer(); // 使用sparkMD5的ArrayBuffer类，读取二进制文件
    // 稿件撞车判定机制: 为保证视频的质量,将根据整个视频内容来计算hash，咕噜咕噜不允许稿件撞车！！！
    // const timestamp = new TextEncoder().encode(Date.now().toString()); // 使用TextEncoder编码成Uint8Array
    // spark.append(timestamp); // 追加时间戳，实现同一视频也可多次上传
    const fileReader = new FileReader();
    let current = 0;
    const total = Math.ceil(file.size / chunkSize);

    function readChunk() {
      const s = current * chunkSize;
      const e = Math.min(s + chunkSize, file.size); // 确保 end 不超出文件大小 ；注意：左闭右开，因为file.slice()是左闭右开的
      const fileChunk = file.slice(s, e); // file.slice()是左闭右开的
      //生成文件分片
      chunks.push(fileChunk);
      //onload是异步操作
      fileReader.onload = (e) => {
        //根据当前文件块，计算hash
        spark.append(e.target.result);
        current++;
        if (current < total) {
          //分片未结束，继续读取下一块文件
          readChunk();
        } else {
          //分片结束，返回最终hash
          resolve(spark.end());
        }
      };

      //读取错误，报错
      fileReader.onerror = (error) => {
        reject(error);
      };

      //fileReader读取二进制文件
      fileReader.readAsArrayBuffer(fileChunk);
    }

    //开始读取第一个文件块
    readChunk();
  });
}

// 上传预热
function warmUp() {
  const spark = new SparkMD5();
  const uuid = uuidv4();
  spark.append(uuid);
  const hash = spark.end();

  // 加载图像并创建 Blob
  const warmImg = require('@/assets/img/warmImg.jpg');
  fetch(warmImg)
    .then((response) => response.blob()) // 将响应转换为 Blob
    .then((blob) => {
      const formData = new FormData();
      formData.append('id', -1); // 这里设置 id = -1，所以不会被上传到阿里云
      formData.append('data', blob, 'img1.jpg'); // 添加文件名
      formData.append('hash', hash);
      formData.append('size', blob.size); // 设置文件大小
      formData.append('total', 1); // 根据需要设置总分片数

      // 上传分片
      uploadVideoChunk(formData);
    })
    .catch((error) => {
      console.error('预热加载图像失败:', error);
    });
}

// 分类树 -> options
function treeToOptions() {
  for (let i = 0; i < tree.value.length; i++) {
    options.value[i] = {
      value: tree.value[i].catryName,
      label: tree.value[i].catryName,
      children: [],
    };
    if (tree.value[i].children) {
      for (let j = 0; j < tree.value[i].children.length; j++) {
        options.value[i].children[j] = {
          value: tree.value[i].children[j].catryName,
          label: tree.value[i].children[j].catryName,
        };
      }
    }
  }
}

// 初始化分区
function initSelectedCatagory() {
  // 默认选中第一个分区
  const defaultParent = options.value[0].value;
  const defaultChild = options.value[0].children[0].value;
  selectedCatagory.value.push(defaultParent);
  selectedCatagory.value.push(defaultChild);
  changeCatagory();
}

onMounted(() => {
  // 上传预热
  warmUp();
  // 绑定分类树
  getCatagoryTree().then(({ data }) => {
    tree.value = data;
    treeToOptions(); // 转换树
    initSelectedCatagory(); // 初始化分区
  });
});
</script>

<style scoped>
.upload-index {
  position: relative;
  width: 100%;
  height: 100%;
}

.errorImg {
  position: absolute;
  z-index: 10;
  top: 2%;
  left: 45%;
  width: 150px;
  height: 200px;
}

.header {
  position: fixed;
  z-index: 2;
  display: flex;
  width: 100%;
  height: 60px;
  box-shadow: 0 2px 10px 0 rgba(0, 0, 0, 0.05);
  background-color: #fff;
}

.left-block {
  flex: 2;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  margin-left: 40px;
}

.main-site {
  padding-left: 40px;
  padding-right: 7px;
}

.create-font {
  font-weight: 600;
  font-size: 20px;
  color: var(--Bl4);
}

.site-font {
  color: #757575;
}

.right-block {
  flex: 3;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.avatar {
  margin-right: 5px;
  width: 40px;
  height: 40px;
  border-radius: 20px;
}

.tips-calendar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40%;
  height: 30px;
  background-color: #fef3ee;
  border-radius: 15px;
  color: #fb9c6d;
  border: 1px solid #fcc7ad;
}

.message {
  margin-left: 40px;
  margin-right: 30px;
  font-size: 24px;
  color: var(--BK2);
}

.upload {
  font-size: 17px;
  color: var(--BK2);
}

.center-block {
  flex: 5;
}

.upload-body {
  display: flex;
  width: 100%;
  height: 100%;
}

.sidebar {
  position: fixed;
  top: 8%;
  z-index: 1;
  display: flex;
  justify-content: center;
  width: 200px;
  height: 750px;
  padding-top: 30px;
  background-color: #fff;
}

.nav-upload-btn {
  width: 136px;
  height: 40px;
}

.nav-upload-btn button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  border: none;
  background-color: var(--Bl1);
  color: #fff;
}

.nav-upload-btn button:hover {
  background-color: var(--Bl3);
}

.btn-icon {
  font-size: 24px;
}

.btn-font {
  padding-left: 5px;
}

.up-wrap {
  margin-top: 60px;
  margin-left: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  padding: 0 20px;
  width: 86%;
  height: 1100px;
  background-color: #fafafa;
}

.up-body {
  position: absolute;
  width: 80%;
  height: 95%;
  background-color: #fff;
}

.up-nav {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 64px;
  /* box-shadow: 0 2px 10px 0 rgba(0, 0, 0, 0.05); */
  border-bottom: 1px solid var(--GR1);
}

.up-nav li {
  margin: 0 30px;
}

.up-nav li:first-child .up-nav-font {
  color: var(--Bl4);
}

.up-nav-font {
  font-size: 18px;
  font-weight: normal;
  color: var(--BK2);
}

.up-main {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 506px;
}

.up-banner {
  padding-top: 20px;
  width: 80%;
  height: 100%;
}

.up-drap-body {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 95%;
  height: 55%;
  border: 2px dashed #ccc;
}

.drap-iconfont {
  font-size: 40px;
  color: var(--GR3);
}

.drap-font {
  font-size: 13px;
  color: var(--GR3);
}

.up-btn {
  margin-top: 20px;
  width: 25%;
  height: 15%;
  background-color: var(--Bl2);
  border: none;
  border-radius: 6px;
  color: #fff;
}

.up-footer {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  width: 95%;
  height: 30%;
  color: var(--GR3);
}

.sub-body {
  display: flex;
  flex-direction: column;
  width: 85%;
  height: 95%;
  background-color: #fff;
}

.sub-title {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 0 32px;
  width: 100%;
  height: 120px;
  box-shadow: 0 1px 0 #e7e7e7;
}

.sub-title-text {
  font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;
  font-size: 16px;
  font-weight: 700;
}

.task-queue {
  width: 95%;
  height: 192px;
  margin: 23px 32px 0;
  padding: 20px 12px;
  background: #f6f7f8;
  border-radius: 10px;
}

.task-list-content {
  width: 100%;
  height: 70px;
}

.task-list-content-item {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 218px;
  height: 100%;
  border-radius: 10px;
  background-color: var(--Bl2);
}

.task-selected {
  padding-left: 10px;
  width: 100%;
  margin-bottom: 15px;
  color: #fff;
  white-space: nowrap; /* 防止文字换行 */
  overflow: hidden; /* 隐藏溢出的内容 */
  text-overflow: ellipsis; /* 超出部分显示省略号 */
}

.task-upload-text {
  position: absolute;
  top: 60%;
  left: 5%;
  font-size: 9px;
  color: #85e3f9;
}

.file-item {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  margin-top: 10px;
  width: 100%;
  height: 76px;
}

.file-item-content {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  width: 800px;
  height: 76px;
  padding: 0 12px;
}

.file-item-content-detail {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  flex: 13;
  padding-top: 20px;
  width: 100%;
  height: 100%;
}

.file-item-text {
  padding-bottom: 3px;
  color: #222;
}

.status-text {
  font-size: 11px;
  color: var(--GR3);
}

.file-item-content-progress {
  flex: 1;
  width: 100%;
  height: 3px;
  border-radius: 4px;
  background-color: #e7e7e7;
}

.file-item-content-bar {
  width: 0%;
  height: 100%;
  border-radius: 4px;
  background-color: var(--Bl4);
  transition: all 0.5s;
}

.file-item-content-operate {
  position: absolute;
  display: flex;
  align-items: center;
  top: 40%;
  right: 0;
  width: 20%;
  height: 45%;
}

.operate-btn {
  margin: 0 10px;
  font-size: 20px;
  color: var(--GR3);
}

.sub-form {
  margin: 30px 30px 0 0;
  background: #fff;
  width: 80%;
  height: 1139px;
  min-width: 900px;
}

.form-title {
  padding: 0 32px;
}

.form-item {
  margin: 24px 0 0 20px;
  padding: 0 32px;
}

.cover {
  display: flex;
  align-items: center;
  width: 100%;
  height: 127px;
}

.section-title {
  display: flex;
  align-items: center;
  width: 134px;
}

.section-title-text {
  font-size: 14px;
  font-family: 'Microsoft YaHei';
  color: #212121;
  line-height: 21px;
  font-weight: 400;
}
.cover-content {
  width: 192px;
  height: 108px;
  border-radius: 4px;
  overflow: hidden; /* 隐藏超出容器的部分 */
}

.cover-content img {
  height: 100%;
  width: 100%;
  object-fit: cover; /* 居中填充容器，不会变形 */
}

.cover-right {
  margin-left: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.cover-right-btn {
  width: 200px;
  height: 30px;
  border: 1px solid var(--GR1);
  border-radius: 6px;
  color: var(--BK2);
  background-color: #fff;
}

.cover-right-btn:hover {
  background-color: var(--GR2);
}

.cover-right-txt {
  margin-top: 5px;
  font-size: 12px;
  color: var(--GR4);
}

.section-title-deg {
  margin-right: 3px;
  font-size: 16px;
  color: #ff3b30;
  line-height: 16px;
}

.title-container {
  display: flex;
}

.video-title-content {
  flex: 1;
}

.input-wrapper {
  display: flex;
  align-items: center;
  width: 100%;
  height: 38px;
  background: hsla(0, 0%, 84.7%, 0);
  border: 1px solid #ccd0d7;
  border-radius: 4px;
}
.input-wrapper:hover {
  outline: none;
  border: 1px solid var(--Bl2);
}

.input-wrapper-active {
  border: 1px solid var(--Bl2);
}

.input-wrapper input {
  padding: 0 12px;
  width: 100%;
  height: 100%;
  border: none;
}
.input-wrapper input::placeholder {
  color: #cbcfd6;
}
.input-wrapper input:focus {
  outline: none;
  /* border: 1px solid var(--Bl2); */
}

.input-max-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 10%;
  height: 100%;
  color: #9ba4ac;
}

.type-container {
  display: flex;
}

.type-content {
  display: flex;
  align-items: center;
  width: 100%;
  height: 30px;
}

.left-type-container,
.right-type-container {
  width: 10%;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.type-box {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 15px;
  width: 20px;
  height: 20px;
  border: 1px solid;
  border-radius: 10px;
  color: #cbcfd6;
}

.type-box-circle {
  display: none;
  width: 14px;
  height: 14px;
  border: 1px solid;
  border-radius: 7px;
}

.type-box-font {
  font-family: 'MicroSoft YaHei';
  color: var(--GR4);
  margin-left: 3px;
}

.type-box-active {
  display: block;
  background-color: var(--Bl2);
}

.type-box-font-active {
  color: var(--Bl4);
}

.section-container {
  display: flex;
}

.tag-container {
  display: flex;
}

.tag-title {
  margin-bottom: 65px;
}

.tag-content {
  display: flex;
  flex-direction: column;
  flex: 1;
  height: 100px;
}

.tag-input-wrap {
  display: flex;
  align-items: center;
  width: 100%;
  height: 30%;
}

.tag-input {
  width: 100%;
  height: 36.4px;
  padding-left: 10px;
  border: 1px solid #ccd0d7;
  border-radius: 4px;
  outline: none;
}
.tag-input:focus {
  border: 1px solid var(--Bl3);
}
.tag-input:hover {
  border: 1px solid var(--Bl3);
}

.recoment-tag-container {
  margin-top: 10px;
  display: flex;
  width: 100%;
  height: 100%;
}

.recoment-tag {
  display: flex;
  padding-bottom: 20px;
  align-items: center;
  font-family: 'Microsoft  YaHei';
}

.recoment-tag-list {
  display: flex;
  flex: 1;
  height: 100%;
  flex-wrap: wrap;
}

.recoment-tag-item {
  font-size: 12px;
  margin: 2px 5px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 12%;
  height: 60%;
  border-radius: 4px;
  font-family: 'MicroSoft YaHei';
  background-color: #f6f6f6;
  cursor: pointer;
}

.desc-container {
  display: flex;
}

.desc-content {
  flex: 1;
  height: 150px;
}

.desc-wrap {
  position: relative;
  width: 100%;
  height: 100%;
}

.desc-textarea {
  width: 100%;
  height: 100%;
  padding: 20px;
  border-radius: 4px;
  font-family: 'microsoft yahei';
  outline: none;
  resize: none;
}

.desc-size {
  position: absolute;
  bottom: 5%;
  right: 5%;
  font-size: 12px;
  color: #757575;
}

.sub-btn-container {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.sub-btn {
  width: 120px;
  height: 40px;
  border-radius: 4px;
  border: none;
  outline: none;
  color: #fff;
  background-color: var(--Bl1);
  transform: translateX(60px);
  transition: all 0.5s;
}
.sub-btn:hover {
  background-color: var(--Bl3);
}
</style>
