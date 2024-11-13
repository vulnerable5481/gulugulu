import httpRequest from '@/network/httRequest';

// 获取分类
export function getCatagoryTree() {
  return httpRequest
    .get('/catagory/list/tree')
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      throw error;
    });
}
