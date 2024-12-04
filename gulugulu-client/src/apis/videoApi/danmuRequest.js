import httpRequest from '@/network/httRequest';

/* 发送弹幕 */
export function sendDanmu(danmu) {
  return httpRequest
    .post('/danmu/send', danmu)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      throw error;
    });
}

/* 获取弹幕列表 */
export function listDanmu(videoId) {
  return httpRequest
    .get(`/danmu/list?videoId=${videoId}`)
    .catch((response) => {
      return response.data;
    })
    .catch((error) => {
      throw error;
    });
}
