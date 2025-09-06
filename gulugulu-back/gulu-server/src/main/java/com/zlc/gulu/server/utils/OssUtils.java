package com.zlc.gulu.server.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.*;
import com.zlc.gulu.common.utils.GuluUtils;
import com.zlc.gulu.pojo.vo.ChunkVo;
import com.zlc.gulu.server.Runnable.OssUploadRunnable;
import com.zlc.gulu.server.handler.ProcessWsHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
public class OssUtils {
    @Value("${directory.video}")
    private String VIDEO_DIRECTORY;     // 本地投稿视频存储目录 【已弃用】
    @Value("${directory.chunk}")
    private String CHUNK_DIRECTORY;     // 分片存储目录
    @Value("${oss.bucketName}")
    String bucketName;
    @Value("${oss.bucketUrl}")
    String bucketUrl;
    @Value("${oss.objectName}")
    String objectName;

    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private ProcessWsHandle processWsHandle;

    /*
     *  追加上传 【速度：2.8~3mb/s】
     * */
    public String mergeChunkAppendToAliOss(ChunkVo chunkVo) {
        String sessionId = chunkVo.getSessionId();
        String hash = chunkVo.getHash();
        Integer total = chunkVo.getTotal();
        // 确保使用新阿里云连接，因为旧阿里云连接已被关闭
        OSS ossClient = applicationContext.getBean(OSS.class);

        // 视频url
        String url = "";
        // 设置合并文件存储目录及文件名
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).replace("-", "");
        String newObjectName = objectName + date + "/" + hash + ".mp4";

        String fileName = CHUNK_DIRECTORY + hash + "/" + hash + "-";
        FileInputStream inputStream = null;
        try {
            // 追加上传文件
            File chunkFile = new File(fileName + 0);
            inputStream = new FileInputStream(chunkFile);
            // 创建阿里云追加上传请求,第一次追加
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType("video/mp4");  // 指定上传的内容类型
            AppendObjectRequest request = new AppendObjectRequest(bucketName, newObjectName, inputStream, meta);
            request.setPosition(0L);
            AppendObjectResult result = ossClient.appendObject(request);

            // 推送第一次进度
            processWsHandle.sendProcess(1, total, sessionId);

            for (int i = 1; i < total; i++) {
                inputStream.close();  // 关闭上一个流
                File file = new File(fileName + i); // 获取下一个文件
                inputStream = new FileInputStream(file); // 创建新的输入流
                // 设置追加位置 、 输入流
                request.setPosition(result.getNextPosition());
                request.setInputStream(inputStream);
                result = ossClient.appendObject(request); // 上传分片

                // 推送进度
                processWsHandle.sendProcess(i + 1, total, sessionId);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 清理临时分片
            clearChunk(hash, false);
            // 确保输入流在完成后关闭
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 返回url
        url = bucketUrl + newObjectName;
        return url;
    }

    /*
     *  分片上传  【进度条没有处理完,暂未启用】
     * */
    public String mergeChunkToAliOss(ChunkVo chunkVo) {
        Integer total = chunkVo.getTotal();
        String hash = chunkVo.getHash();
//        double size = chunkVo.getSize();
        // 确保使用新阿里云连接，因为旧阿里云连接已被关闭
        OSS ossClient = applicationContext.getBean(OSS.class);

        // 视频url
        String url = "";
        // 设置合并文件存储目录及文件名
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).replace("-", "");
        String newObjectName = objectName + date + "/" + hash + ".mp4";

        // 创建InitiateMultipartUploadRequest对象
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, newObjectName);

        // 设置文件类型为 MP4
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("video/mp4");
        request.setObjectMetadata(metadata);

        // 初始化分片
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        // 返回uploadId
        String uploadId = upresult.getUploadId();

        try {
            // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成
            List<PartETag> partETags = new ArrayList<>();
            // 分片逐一上传到阿里云
            for (int i = 0; i < total; i++) {
                String fileName = CHUNK_DIRECTORY + hash + "/" + hash + "-" + i;
                File chunkFile = new File(fileName);
                try (FileInputStream inputStream = new FileInputStream(chunkFile)) {  // 自动关闭inputStream
//                    // 视频是否上传成功
//                    final boolean[] isSuccess = {true};
//                    // 视频上传字节数
//                    final long[] bytesWritten = {0};
//                    ProgressListener progressListener = new ProgressListener() {
//                        @Override
//                        public void progressChanged(ProgressEvent progressEvent) {
//                            double bytes = progressEvent.getBytes(); // 当前分片大小
//                            ProgressEventType eventType = progressEvent.getEventType();
//                            switch (eventType) {
////                                case TRANSFER_STARTED_EVENT: // 开始传输
////                                    break;
////                                case REQUEST_CONTENT_LENGTH_EVENT: // 获取文件分片大小
////                                    break;
//                                case REQUEST_BYTE_TRANSFER_EVENT: // 分片上传中
//                                    bytesWritten[0] += bytes;
//                                    // 控制更新频率，避免过于频繁
//                                    double d = 1024 * 1024 * 8;
//                                    if (bytesWritten[0] % d == 0) {  // 每上传8MB更新一次
//                                        double res = (bytesWritten[0] / size) * 100;
//                                        log.info("当前分片大小" + bytes);
//                                        log.info("已上传大小" + bytesWritten[0]);
//                                        log.info("上传进度:{}", res + "%");
//                                    }
//                                    break;
////                                case TRANSFER_COMPLETED_EVENT: // 单个分片上传完毕
////                                    bytesWritten[0] += bytes;
//                                // 利用websocket 推送信息
////                                    break;
//                                case TRANSFER_FAILED_EVENT: // 上传失败
//                                    isSuccess[0] = false;
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    };

                    UploadPartRequest uploadPartRequest = new UploadPartRequest();
                    //设置基本属性
                    uploadPartRequest.setBucketName(bucketName);
                    uploadPartRequest.setKey(newObjectName);
                    uploadPartRequest.setUploadId(uploadId);
                    uploadPartRequest.setPartNumber(i + 1);
                    uploadPartRequest.setInputStream(inputStream);
                    uploadPartRequest.setPartSize(chunkFile.length());
//                    uploadPartRequest.setProgressListener(progressListener); // 设置进度条监视器
                    PartETag partETag = ossClient.uploadPart(uploadPartRequest).getPartETag();
                    partETags.add(partETag);
                    // 分片上传是否成功
//                    if (isSuccess[0] == false) {
//                        //  重传机制
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 合并分片
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, newObjectName, uploadId, partETags);
            ossClient.completeMultipartUpload(completeMultipartUploadRequest);

            // 清理临时分片目录
            clearChunk(hash, false);

        } catch (Exception e) {
            ossClient.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, newObjectName, uploadId));
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        // 返回url
        url = bucketUrl + newObjectName;
        return url;
    }

    /*
     *  分片并发上传
     * */
    public String mergeChunkToAliOssPlus(String hash, int total) {
        // 确保使用新阿里云连接
        OSS ossClient = applicationContext.getBean(OSS.class);
        // 确保使用新连接池
        ThreadPoolExecutor threadPoolExecutor = applicationContext.getBean(ThreadPoolExecutor.class);

        // 视频url
        String url = "";
        // 设置合并文件存储目录及文件名
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).replace("-", "");
        String newObjectName = objectName + date + "/" + hash + ".mp4";

        // 创建InitiateMultipartUploadRequest对象
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, newObjectName);

        // 设置文件类型为 MP4
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("video/mp4");
        request.setObjectMetadata(metadata);

        // 初始化分片
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        // 返回uploadId
        String uploadId = upresult.getUploadId();
        try {
            // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成
            List<PartETag> partETags = new ArrayList<>();
            // 初始化计数器，等待 totalParts 个分片上传完成
            CountDownLatch latch = new CountDownLatch(total);

            // 分片并发上传到阿里云
            for (int i = 0; i < total; i++) {
                String fileName = CHUNK_DIRECTORY + hash + "/" + hash + "-" + i;
                OssUploadRunnable task = new OssUploadRunnable(fileName, bucketName, newObjectName, uploadId, i,
                        latch, ossClient, partETags);
                threadPoolExecutor.execute(task);
            }

            // 等待所有分片上传完成
            latch.await();

            // 合并分片
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, newObjectName, uploadId, partETags);
            ossClient.completeMultipartUpload(completeMultipartUploadRequest);

            // 清理临时分片目录
            clearChunk(hash, false);

        } catch (Exception e) {
            ossClient.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, newObjectName, uploadId));
        } finally {
            if (ossClient != null) {
                ossClient.shutdown(); // 关闭阿里云连接
            }
            if (threadPoolExecutor != null) {
                threadPoolExecutor.shutdown(); // 关闭线程池
            }
        }
        // 返回url
        url = bucketUrl + newObjectName;
        return url;
    }

    /*
     *  阿里云碎片管理
     * */

    /*
     *  清除本地临时分片存储
     * */
    public void clearChunk(String hash, boolean isVideo) {
        try {
            String dir = isVideo ? VIDEO_DIRECTORY + hash + ".mp4" : CHUNK_DIRECTORY + hash; // 判断清除目录是Video 还是 临时chunk

            Path path = Paths.get(dir);

            if (Files.exists(path)) {
                GuluUtils.deleteDir(dir);
                log.info("清除成功:" + dir);
            }

        } catch (Exception e) {
            e.printStackTrace(); // 即使清除失败也不做处理 todo:后期我会做一个定时清理chunk的功能,既可以在java实现，也可以使用shell脚本,图简单就redis
        }
    }

    /*
     *  上传图片
     * */
    public String uploadImg(MultipartFile img) {
        if (img.isEmpty()) {
            return "";
        }
        OSS ossClient = applicationContext.getBean(OSS.class);
        try (InputStream inputStream = img.getInputStream()) {
            // 创建ObjectName
            String fileName = img.getOriginalFilename(); // 获取图片名称
            int index = fileName.lastIndexOf(".");
            String prefix = fileName.substring(0, index); // 提取文件名字
            String fileExtension = fileName.substring(index); // 提取文件扩展名
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String suffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")); //不固定图片后缀名,防止重复
            String objectName = "cover/" + time + "/" + prefix + "_" + suffix + fileExtension;
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 返回结果
            String url = bucketUrl + objectName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
