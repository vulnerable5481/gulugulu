import { uploadVideo, randomViews } from './uploadRequest';

// 上传文件分片
export function uploadVideoChunk(formData) {
  if (formData) {
    return uploadVideo(formData);
  }
}