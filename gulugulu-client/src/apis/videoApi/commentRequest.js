import httpRequest from '@/network/httRequest';

// 加载评论区
export function getCommentTree(videoId, offset, type) {
  return httpRequest
    .get(`/comment/tree?videoId=${videoId}&offset=${offset}&type=${type}`)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      throw error;
    });
}

// 发送评论
export function sendComment(comment) {
  return httpRequest
    .post('/comment/send', comment)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log('发送信息失败:', error);
      throw error;
    });
}
