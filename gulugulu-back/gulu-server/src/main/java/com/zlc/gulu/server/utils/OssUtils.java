package com.zlc.gulu.server.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.zlc.gulu.common.utils.GuluUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
    private ThreadPoolExecutor threadPoolExecutor;

    /*
     *  分片串行上传,最终在阿里云合并分片 【速度：2~3mb】
     * */
    public String mergeChunkToAliOss(String hash, int total) {
        // 确保使用新阿里云连接，因为旧阿里云连接已被关闭
        OSS ossClient = applicationContext.getBean(OSS.class);

        // 视频url
        String url = "";
        // 设置合并文件存储目录及文件名
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).replace("-", "");
        String newObjectName = objectName  + date + "/" + hash + ".mp4";

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
                    UploadPartRequest uploadPartRequest = new UploadPartRequest();
                    //设置基本属性
                    uploadPartRequest.setBucketName(bucketName);
                    uploadPartRequest.setKey(newObjectName);
                    uploadPartRequest.setUploadId(uploadId);
                    uploadPartRequest.setPartNumber(i + 1);
                    uploadPartRequest.setInputStream(inputStream);
                    uploadPartRequest.setPartSize(chunkFile.length());
                    PartETag partETag = ossClient.uploadPart(uploadPartRequest).getPartETag();
                    partETags.add(partETag);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 合并分片
            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, newObjectName, uploadId, partETags);
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
     *  分片并行上传,最终在阿里云合并分片 【速度：2mb】 TODO:并行上传有待优化！
     * */
    public String mergeChunkToAliOssPlus(String hash, int total) {
        OSS ossClient = applicationContext.getBean(OSS.class);

        // 视频url
        String url = "";
        // 设置合并文件存储目录及文件名
        objectName += hash + ".mp4";

        // 创建InitiateMultipartUploadRequest对象
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);

        // 设置文件类型为 MP4
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("video/mp4");
        request.setObjectMetadata(metadata);

        // 初始化分片
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        // 返回uploadId
        String uploadId = upresult.getUploadId();

        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成
        List<PartETag> partETags = Collections.synchronizedList(new ArrayList<>());

        try {
            List<Runnable> tasks = new ArrayList<>();

            // 封装分片上传的逻辑
            for (int i = 0; i < total; i++) {
                final int partNumber = i; // 使用 final 变量
                tasks.add(() -> {
                    String fileName = CHUNK_DIRECTORY + hash + "/" + hash + "-" + partNumber;
                    File chunkFile = new File(fileName);
                    try (FileInputStream inputStream = new FileInputStream(chunkFile)) {
                        UploadPartRequest uploadPartRequest = new UploadPartRequest();
                        // 设置基本属性
                        uploadPartRequest.setBucketName(bucketName);
                        uploadPartRequest.setKey(objectName);
                        uploadPartRequest.setUploadId(uploadId);
                        uploadPartRequest.setPartNumber(partNumber + 1);
                        uploadPartRequest.setInputStream(inputStream);
                        uploadPartRequest.setPartSize(chunkFile.length());
                        PartETag partETag = ossClient.uploadPart(uploadPartRequest).getPartETag();
                        partETags.add(partETag);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            // 提交任务到线程池
            CountDownLatch latch = new CountDownLatch(total);

            for (Runnable task : tasks) {
                threadPoolExecutor.execute(() -> {
                    try {
                        task.run();
                    } finally {
                        latch.countDown(); // 每个任务完成后减少计数
                    }
                });
            }

            // 等待所有任务完成
            latch.await();

            // 合并分片
            CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);
            ossClient.completeMultipartUpload(completeMultipartUploadRequest);

            // 清理临时分片目录
            clearChunk(hash, false);

        } catch (Exception e) {
            ossClient.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, objectName, uploadId));
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        // 返回url
        url = bucketUrl + objectName + hash + ".mp4";
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
}
