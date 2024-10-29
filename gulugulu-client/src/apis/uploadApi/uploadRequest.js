import httpRequest from '@/network/httRequest.js';

// 上传视频
export function uploadVideo(formData) {
  return httpRequest
    .postForm('/upload/chunk', formData)
    .then((response) => response.data)
    .catch((error) => {
      console.log('上传视频失败~~~');
      throw error;
    });
}

// 合并文件
export function mergeFile(mergeVo) {
  return httpRequest
    .post('/upload/merge', mergeVo)
    .then((response) => response)
    .catch((error) => {
      console.log('文件合并失败');
      throw error;
    });
}
