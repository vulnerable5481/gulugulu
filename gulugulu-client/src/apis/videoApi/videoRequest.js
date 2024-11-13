import httpRequest from '@/network/httRequest';

// 上传投稿视频
export function saveVideo(video) {
  httpRequest
    .post('/video/save', video)
    .then((response) => {
      return response;
    })
    .catch((error) => {
      throw error;
    });
}
