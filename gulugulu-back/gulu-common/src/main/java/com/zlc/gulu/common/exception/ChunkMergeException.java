package com.zlc.gulu.common.exception;

import java.util.List;

public class ChunkMergeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code = 500;     // 默认错误码
    private String chunkHash;   // 存储出错分片的hash
    private List<Integer> list; // 存储出错分片id

    public ChunkMergeException(String msg) {
        super(msg);
    }

    public ChunkMergeException(String msg,String chunkHash,List<Integer> list){
        super(msg);
        this.chunkHash = chunkHash;
        this.list = list;
    }

    public ChunkMergeException(String msg, Throwable cause) {
        super(msg, cause); // 将消息和导致此异常的原因传递给父类
    }

    public ChunkMergeException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getChunkHash() {
        return chunkHash;
    }

    public void setChunkHash(String chunkHash) {
        this.chunkHash = chunkHash;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }
}
