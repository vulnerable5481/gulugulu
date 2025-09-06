package com.zlc.gulu.server.service.impl;

import com.zlc.gulu.common.constant.ErrorConstant;
import com.zlc.gulu.common.constant.VideoConstant;
import com.zlc.gulu.common.exception.ChunkMergeException;
import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.common.utils.GuluUtils;
import com.zlc.gulu.pojo.vo.ChunkVo;
import com.zlc.gulu.server.service.UploadService;
import com.zlc.gulu.server.service.VideoService;
import com.zlc.gulu.server.utils.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UploadServiceImp implements UploadService {
    @Value("${directory.video}")
    private String VIDEO_DIRECTORY;     // 本地投稿视频存储目录 【已弃用】
    @Value("${directory.chunk}")
    private String CHUNK_DIRECTORY;     // 分片存储目录

    @Resource
    private OssUtils ossUtils;
    @Resource
    private VideoService videoService;

    /*
     * 上传视频分片-阿里云存储
     * */
    @Override
    public Result uploadChunk(ChunkVo chunkVo) {
        // 视频url
        String url = "";

        Integer id = chunkVo.getId();
        Integer total = chunkVo.getTotal();
        String hash = chunkVo.getHash();
        MultipartFile chunk = chunkVo.getData();

        // 秒传 【如果文件已上传则直接返回】
        // 这样就会产生一个问题，无法重复上传同一个文件
        // 允许用户反复上传同一个文件
//        if(videoService.isVideoExists(hash)){
//            return Result.success();
//        }

        // 分片预热后，直接清除并返回
        if (id == -1) {
//            ossUtils.clearChunk(hash, false);
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
            String fileName = hash + '-' + id;
            Path fullPath = cdPath.resolve(fileName); // 使用 Path 来拼接路径
            chunk.transferTo(fullPath);
        } catch (IOException e) {
            log.info("创建临时分片文件夹出错: {}", e.getMessage());
            return Result.error(ErrorConstant.ErrorEnum.UPLOAD_FAIL_CREATEFILE.getCode(),
                    ErrorConstant.ErrorEnum.UPLOAD_FAIL_CREATEFILE.getMsg());
        }

//         合并分片到本地
//        try {
//            if (chunkVo.getId() == total - 1) {
//                mergeChunk(hash, total);
//            }
//        } catch (ChunkMergeException e) {
//            List<Integer> list = e.getList();
//            String s = list.stream().map(String::valueOf) // 将每个Integer转换String
//                    .collect(Collectors.joining(","));// 使用逗号连接成一个字符串
//            ossUtils.clearChunk(hash, true);       // 清除错码视频
//            return Result.error(ErrorConstant.ErrorEnum.UPLOADS_FAIL_MERGEFILE.getCode(), ErrorConstant.ErrorEnum.UPLOADS_FAIL_MERGEFILE.getMsg(), s);
//        }

        // 如果不是最后一个分片，则直接返回
        if (id != total - 1) {
            return Result.success();
        }

        // 合并分片到阿里云OSS
        url = ossUtils.mergeChunkAppendToAliOss(chunkVo);

        // 检验合并分片结果
        if (GuluUtils.isEmpty(url)) {
            return Result.error(ErrorConstant.ErrorEnum.UPLOADS_FAIL_MERGEFILE.getCode(),
                    ErrorConstant.ErrorEnum.UPLOADS_FAIL_MERGEFILE.getMsg());
        }

        // 返回结果
        return Result.success(url);
    }

    /*
     * 文件合并-本地存储 【已弃用】
     * */
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

        try (FileOutputStream fos = new FileOutputStream(targetPath); FileChannel destChannel = fos.getChannel()) {

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
                ossUtils.clearChunk(hash, true);  // 清除错码视频
                throw new ChunkMergeException("分片文件不存在", hash, list);
            }

            // 合并成功，清除临时分片
            ossUtils.clearChunk(hash, false);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    * 上传视频封面
    * */
    @Override
    public Result uploadCover(MultipartFile cover) {
        String url = ossUtils.uploadImg(cover);
        if (GuluUtils.isEmpty(url)) {
            return Result.error(VideoConstant.VideoEnum.VIDEO_UPLOAD_COVER_FAIL.getCode()
                    , VideoConstant.VideoEnum.VIDEO_UPLOAD_COVER_FAIL.getMsg());
        } else {
            return Result.success(url);
        }

    }


}



























