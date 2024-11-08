package com.zlc.gulu.server.Runnable;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class OssUploadRunnable implements Runnable {

    private String fileName;
    private String bucketName;
    private String newObjectName;
    private String uploadId;
    private int i;
    private CountDownLatch latch;
    private OSS ossClient;
    List<PartETag> partETags;

    public OssUploadRunnable(String fileName, String bucketName, String newObjectName, String uploadId, int i,
                             CountDownLatch latch, OSS ossClient, List<PartETag> partETags) {
        this.fileName = fileName;
        this.bucketName = bucketName;
        this.newObjectName = newObjectName;
        this.uploadId = uploadId;
        this.i = i;
        this.latch = latch;
        this.ossClient = ossClient;
        this.partETags = partETags;
    }

    @Override
    public void run() {
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
        } finally {
            // 上传完毕，调用countDown(),确保等待所有分片完成
            latch.countDown();
        }
    }
}
