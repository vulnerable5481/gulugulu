package com.zlc.gulu.server.service.impl;

import com.zlc.gulu.common.constant.ErrorConstant;
import com.zlc.gulu.common.exception.ChunkMergeException;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.common.utils.GuluUtils;
import com.zlc.gulu.pojo.vo.ChunkVo;
import com.zlc.gulu.server.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UploadServiceImp implements UploadService {

    @Value("${directory.video}")
    private String VIDEO_DIRECTORY;     // 投稿视频存储目录
    @Value("${directory.chunk}")
    private String CHUNK_DIRECTORY;     // 分片存储目录

    /*
     * 上传视频分片，本地存储
     * */
    @Override
    public Result uploadChunk(ChunkVo chunkVo) {
        Integer total = chunkVo.getTotal();
        String hash = chunkVo.getHash();
        MultipartFile chunk = chunkVo.getData();

        // 极速秒传 【如果文件已上传则直接返回】
        String dir = VIDEO_DIRECTORY + chunkVo.getHash() + ".mp4";
        Path path = Paths.get(dir);
        if (Files.exists(path)) {
            return Result.success();
        }

        // 构建临时分片存储目录
        String chunkDirectory = CHUNK_DIRECTORY + hash + '/';
        Path cdPath = Paths.get(chunkDirectory);

        try {
            // 判断文件夹是否存在
            if (!Files.exists(cdPath)) {
                Files.createDirectories(cdPath); // 创建文件夹
            }
            // 分片存到临时目录
            String fileName = hash + '-' + chunkVo.getId();
            Path fullPath = cdPath.resolve(fileName); // 追加文件名
            chunk.transferTo(fullPath);
        } catch (IOException e) {
            log.info("创建临时分片文件夹出错: {}", e.getMessage());
            return Result.error(ErrorConstant.ErrorEnum.UPLOAD_FAIL_CREATEFILE.getCode(),
                    ErrorConstant.ErrorEnum.UPLOAD_FAIL_CREATEFILE.getMsg());
        }

        // 合并分片到本地
        try {
            if (chunkVo.getId() == total - 1) {
                mergeChunk(hash, total);
            }
        } catch (ChunkMergeException e) {
            List<Integer> list = e.getList();
            String s = list.stream().map(String::valueOf) // 将每个Integer转换String
                    .collect(Collectors.joining(","));// 使用逗号连接成一个字符串
            clearChunk(hash, true);       // 清除错码视频
            return Result.error(
                    ErrorConstant.ErrorEnum.UPLOADS_FAIL_MERGEFILE.getCode(),
                    ErrorConstant.ErrorEnum.UPLOADS_FAIL_MERGEFILE.getMsg(),
                    s);
        }

        // 返回结果
        return Result.success();
    }

    /*
     *  上传视频分片，阿里云OSS存储
     * */
    public Result uploadChunkToAliOss(ChunkVo chunkVo) {
        Integer id = chunkVo.getId();
        String hash = chunkVo.getHash();
        Integer total = chunkVo.getTotal();
        MultipartFile chunk = chunkVo.getData();

        // 是否极速秒传 【即阿里云OSS里已保存相同视频】

        // 构建临时分片存储目录
        String chunkDirectory = CHUNK_DIRECTORY + hash + '/';
        Path cdPath = Paths.get(chunkDirectory);
        try {
            // 判断文件夹是否存在
            if (!Files.exists(cdPath)) {
                Files.createDirectories(cdPath); // 创建多层文件夹
            }
            // 分片存到临时目录
            String fileName = hash + '-' + chunkVo.getId();
            Path fullPath = cdPath.resolve(fileName); // 追加文件名
            chunk.transferTo(fullPath);
        } catch (IOException e) {
            log.info("创建临时分片文件夹出错: {}", e.getMessage());
            return Result.error(ErrorConstant.ErrorEnum.UPLOAD_FAIL_CREATEFILE.getCode(),
                    ErrorConstant.ErrorEnum.UPLOAD_FAIL_CREATEFILE.getMsg());
        }

        // 合并分片
        if(id == total - 1){

        }
    }

    /*
     * 合并文件
     * */
    //TODO: 使用 RandomAccessFile 配合线程池可以实现并发文件合并,有时间可以优化一下，不过多线程就意味着可能造成数据污染,要用到锁,是很大的挑战
    public boolean mergeChunk(String hash, int total) {
        // 创建目标文件路径
        String targetPath = VIDEO_DIRECTORY + hash + ".mp4";
        try {
            // 创建目标目录
            Path path = Paths.get(VIDEO_DIRECTORY);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream fos = new FileOutputStream(targetPath);
             FileChannel destChannel = fos.getChannel()) {

            List<Integer> list = new ArrayList<>(); // 存储出错分片

            for (int i = 0; i < total; i++) {
                String chunkPath = CHUNK_DIRECTORY + hash + '/' + hash + '-' + i; // 分片路径
                File chunkFile = new File(chunkPath);

                if (chunkFile.exists()) {
                    try (RandomAccessFile raf = new RandomAccessFile(chunkFile, "r"); // 只读
                         FileChannel srcChannel = raf.getChannel()) {

                        // 手动设置缓存，手动读取和写入
//                        ByteBuffer buf = ByteBuffer.allocate(1024 * 1024); // 1MB缓存
//                        while (srcChannel.read(buf) != -1) {
//                            buf.flip(); // 切换到读取模式
//                            destChannel.write(buf); // 将数据写入目标文件
//                            buf.clear(); // 清空缓冲区以便重用
//                        }

                        // 使用 channel.transferTo 提高性能,同时免去手动管理缓存
                        long position = 0;
                        long size = srcChannel.size();
                        while (position < size) {
                            position += srcChannel.transferTo(position, size - position, destChannel);
                        }
                    }
                } else {
                    list.add(i);
                }
            }

            // 分片合并失败
            if (list.size() != 0) {
                clearChunk(hash, true);  // 清除错码视频
                throw new ChunkMergeException("分片文件不存在", hash, list);
            }

            // 合并成功，清除临时分片
            clearChunk(hash, false);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     *  清除临时分片存储
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


























