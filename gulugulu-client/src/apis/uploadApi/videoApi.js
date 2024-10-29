import { uploadVideo } from "./uploadRequest";




export function uploadVideoChunk(formData) {
  if(formData){
    return uploadVideo(formData)
  }
}
