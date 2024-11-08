<template>
  <!-- <div>
    <input type="file" ref="fileInput" @change="handleupload" />
  </div> -->
  <div class="upload-index">
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
        <div class="up-body" v-if="!isEdited">
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
        <div class="sub-body" v-if="isEdited">
          <div class="sub-title">
            <h3 class="sub-title-text">发布视频</h3>
          </div>
          <div class="task-queue">
            <div class="task-list-content">
              <div class="task-list-content-item">
                <span class="task-selected">{{ fileTransfer.fileName }}</span>
                <span class="task-upload-text">上传中...</span>
              </div>
            </div>
            <div class="file-item">
              <i class="gulu-leimujianying iconfont" style="font-size: 40px; color: #13227a"></i>
              <div class="file-item-content">
                <div class="file-item-content-detail">
                  <div class="file-item-text">{{ fileTransfer.fileName }}</div>
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
          <div class="sub-form"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { uploadVideoChunk } from '@/apis/uploadApi/videoApi';
import SparkMD5 from 'spark-md5';
import { computed, onMounted, reactive, ref } from 'vue';
import { v4 as uuidv4 } from 'uuid';
import { useUserStore } from '@/store';

// pinia
const store = useUserStore();
// 是否编辑投稿视频
let isEdited = ref(false);

// 文件传输信息实体
let fileTransfer = reactive({
  fileName: '默认文件名字',
  fileSize: 0, // 文件大小
  remainingTime: 0, // 剩余时间
  trsfRate: 0, // 已上传文件大小的百分比 【注意：计算需/100】
  trsfSize: 0, // 已上传文件大小
  speed: 3, // 传输速率
});

// 文件input DOM
const fileInput = ref();
// 定义分片大小
const chunkSize = 1024 * 1024 * 8; //8MB
// 分片集合
let chunks = [];
// sessionId
let sessionId = ref('');
// 用户头像
const userAvatar = computed(() => {
  return store.userInfo.avatar;
});

// 视频ref
const videoElement = ref();
function play() {
  if (videoElement.value) {
    videoElement.value.muted = true; // 可选：设置为静音
    videoElement.value.play();
  }
}
function close() {
  if (videoElement.value) {
    videoElement.value.pause();
    videoElement.value.currentTime = 0; // 重置播放时间
  }
}

// websocket
const websocket = new WebSocket('ws://localhost:9090/api/processWs');
websocket.onopen = function (event) {
  console.log('websocket通信建立');
};
websocket.onclose = function (event) {
  console.log('websocket通信关闭');
  websocket.close();
};
websocket.onmessage = function (event) {
  console.log(event);
  if (event.data.length > 10) {
    sessionId.value = event.data; // 如果长度超过10，保存 sessionId
  } else {
    fileTransfer.trsfRate = event.data; // 否则保存 trsfRate
    // 计算剩余上传时间  parseFloat是为了将 保留两位小数的字符串再变回数字,未来可能需要计算真实的上传速度
    fileTransfer.remainingTime = parseFloat(((fileTransfer.fileSize * (1 - fileTransfer.trsfRate / 100)) / 3).toFixed(2));
    // 计算已上传文件大小
    fileTransfer.trsfSize = parseFloat((fileTransfer.trsfRate / 100) * fileTransfer.fileSize).toFixed(1);
  }
};

// 触发上传事件
function triggerFileInput() {
  fileInput.value.click();
}

// 上传文件
async function handleUpload() {
  // 切换内嵌页
  isEdited.value = true;
  const file = fileInput.value.files[0];
  const fileSizeInMB = (file.size / (1024 * 1024)).toFixed(2); // 转换为 MB，保留两位小数
  //保存文件信息
  fileTransfer.fileName = file.name;
  fileTransfer.fileSize = fileSizeInMB;
  const totalChunks = Math.ceil(file.size / chunkSize);
  //计算hash,生成文件分片
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
        uploadVideoChunk(formData);
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
      console.error('计算哈希值时出错:', error);
    });

  // 上传完毕,清除缓存
  chunks = [];
}

// 计算hash,同时生成文件分片   【防止文件过大卡死,采取文件分片计算hash】
//这里是将文件整体读入计算md5，好处是md5碰撞的概率大大降低，缺点是计算时间会长一些；
//如果想计算时间短一些，不追求极致的低碰撞率的话，可以考虑读入第一个切片和最后一个切片进行md5计算。这里可以根据实际情况酌情考虑。
function fhash(file) {
  return new Promise((resolve, reject) => {
    const spark = new SparkMD5.ArrayBuffer(); // 使用sparkMD5的ArrayBuffer类，读取二进制文件
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

onMounted(() => {
  warmUp();
});
</script>

<style scoped>
.upload-index {
  width: 100%;
  height: 100%;
}

.header {
  display: flex;
  width: 100%;
  height: 60px;
  box-shadow: 0 2px 10px 0 rgba(0, 0, 0, 0.05);
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
  display: flex;
  justify-content: center;
  width: 200px;
  height: 600px;
  padding-top: 30px;
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
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  padding: 0 20px;
  width: 100%;
  height: 600px;
  background-color: var(--GR2);
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
  align-items: center;
  position: absolute;
  width: 80%;
  height: 95%;
  padding: 0 32px;
  background-color: #fff;
}

.sub-title {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
  height: 70px;
  box-shadow: 0 1px 0 #e7e7e7;
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
</style>
