import httpRequest from '@/network/httRequest.js';

// 上传视频
export function uploadVideo(formData) {
  return httpRequest
    .postForm('/upload/chunk', formData)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
}

// 上传图片
export function uploadImg(img) {
  return httpRequest
    .postForm ('/upload/img', img)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      throw error;
    });
}

// 合并文件
export function mergeFile(mergeVo) {
  return httpRequest
    .post('/upload/merge', mergeVo)
    .then((response) => response)
    .catch((error) => {
      throw error;
    });
}

// 获取随机视频
export function getRandomViews() {
  return httpRequest
    .get('/video/randomViews')
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log('获取随机视频失败:', error);
    });
}
