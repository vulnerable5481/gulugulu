<template>
  <div>
    <input type="file" ref="fileInput" @change="handleupload" />
  </div>
</template>

<script setup>
import { uploadVideoChunk } from '@/apis/uploadApi/videoApi';
import SparkMD5 from 'spark-md5';
import { onMounted, ref } from 'vue';
import { v4 as uuidv4 } from 'uuid';
import { mergeFile } from '@/apis/uploadApi/uploadRequest';

// 获取DOM
const fileInput = ref();
// 定义分片大小
const chunkSize = 1024 * 1024 * 8; //8MB
// 分片集合
const chunks = [];

// 触发上传事件
function handleupload() {
  const file = fileInput.value.files[0];
  const totalChunks = Math.ceil(file.size / chunkSize);
  //计算hash,生成文件分片
  fhash(file)
    .then((hash) => {
      //分片单独上传到服务器
      for (let i = 0; i < totalChunks; i++) {
        const chunk = chunks[i];
        const formData = new FormData();
        formData.append('id', i);
        formData.append('data', chunk);
        formData.append('hash', hash);
        formData.append('size', chunkSize);
        formData.append('total', totalChunks);
        //上传服务器
        // 仅在分片 i 为 20 时不上传
        if (i === 20 || i === 22 || i === 24) {
          console.warn(`分片 ${i} 被跳过`);
          continue; // 跳过该分片
        }
        uploadVideoChunk(formData).then((response) => {
          // 出错，断点续传
          if (i == totalChunks - 1) {
            const errorIds = response.data.split(',').map(Number); // 转换成数字数组
            //  重传错误分片
            for (let j = 0; j < errorIds.length; j++) {
              const id = errorIds[j];
              console.log('重传分片:', id);
              const chunk = chunks[id];
              const formData = new FormData();
              formData.append('id', id);
              formData.append('data', chunk);
              formData.append('hash', hash);
              formData.append('size', chunkSize);
              formData.append('total', totalChunks);
              uploadVideoChunk(formData);
            }
            // 重新合并分片
            const mergeVo = {
              hash: hash,
              total: totalChunks,
            };
            mergeFile(mergeVo);
          }
        });
      }
    })
    .catch((error) => {
      console.error('计算哈希值时出错:', error);
    });
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
      formData.append('id', 1);
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

<style scoped></style>
