import httpRequest from '@/network/httRequest';

// 上传投稿视频
export function saveVideo(video) {
  return httpRequest
    .post('/video/save', video)
    .then((response) => {
      return response;
    })
    .catch((error) => {
      throw error;
    });
}

// 查询视频详情
export function queryVideo(videoId) {
  return httpRequest
    .get(`/video/queryVideo?videoId=${videoId}`)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      throw error;
    });
}
