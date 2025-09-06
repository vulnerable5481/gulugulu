package com.zlc.gulu.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zlc.gulu.common.constant.CommentConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
 *  redis相关工具包
 * @需要的额外包:
 * @Writter: zlc
 * 开始制作时间：2024.11.21
 * */
@Slf4j
@Component
public class RedisUtils {

    private final StringRedisTemplate redisTemplate;

    // 构造器注入
    public RedisUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 定义ZSetObject类，表示需要写入到ZSet中的数据
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ZObjTime {
        private Object member;
        private long time;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ZObjScore {
        private Object member;
        private Double score;
    }

    //  通用相关操作 begin ----------------------------------------------------------------------------------------------
    /*
     * 设置过期时间
     * */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        return redisTemplate.expire(key, time, timeUnit);
    }


    // 通用相关操作 begin ----------------------------------------------------------------------------------------------


    // String 相关操作 begin ----------------------------------------------------------------------------------------------

    /**
     * 添加一条Object数据到 String
     *
     * @param v 特指Object
     * @return -1 表示发生异常
     */
    public long setObjectValue(String k, Object v) {
        ObjectMapper objectMapper = new ObjectMapper();
        String s = null;
        try {
            s = objectMapper.writeValueAsString(v);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return -1;  // 发生异常
        }
        return redisTemplate.opsForSet().add(k, s);
    }

    /**
     * 添加一条String数据到 String
     *
     * @param v 特指 String类型
     * @return
     */
    public long set(String k, String v) {
        return redisTemplate.opsForSet().add(k, v);
    }

    // String 相关操作 end ----------------------------------------------------------------------------------------------

    // ZSET 相关操作 begin ----------------------------------------------------------------------------------------------

    /**
     * 添加一条数据到 sorted set 【默认使用时间戳作为评分】
     *
     * @return
     */
    public boolean zset(String key, String value) {
        long now = System.currentTimeMillis();
        return this.zsetWithScore(key, value, now);
    }

    /**
     * 添加一条数据到sorted set
     *
     * @param score 必须规定一个属性，用来计算评分
     * @return
     */
    public boolean zsetWithScore(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 批量添加数据到 Redis 的 Sorted Set 【默认使用时间戳作为评分】
     *
     * @param key        Redis 中有序集合的键
     * @param collection 需要添加的自定义集合，包含成员和时间
     * @return 成功添加的元素个数
     */
    public long zsetBatch(String key, Collection<ZObjTime> collection) {
        // 将 collection 转换为 Redis 需要的 TypedTuple 格式，并批量添加
        Set<ZSetOperations.TypedTuple<String>> tuples = convertToTypedTupleSetByTime(collection);
        Long add = redisTemplate.opsForZSet().add(key, tuples);
        // 设置过期时间
        redisTemplate.expire(key, CommentConstant.COMMENT_VIDEO_EXPIRE, CommentConstant.COMMENT_VIDEO_TIMEUNIT);
        return add;
    }

    public Set<ZSetOperations.TypedTuple<String>> convertToTypedTupleSetByTime(Collection<ZObjTime> collection) {
        return collection.stream()
                .map(zObjTime -> new DefaultTypedTuple<>(zObjTime.getMember().toString(),
                        (double) zObjTime.getTime()))
                .collect(Collectors.toSet());
    }

    /**
     * 批量添加数据到Redis的Sorted Set
     *
     * @param key
     * @return collection   需要添加的自定义集合，包含成员和平分
     */
    public long zsetBatchByScore(String key, Collection<ZObjScore> collection) {
        // 将collection 转换为 Redis需要的 TypeTuple格式，并批量添加
        Set<ZSetOperations.TypedTuple<String>> typedTuples = convertToTypedTupleSetByScore(collection);
        Long add = redisTemplate.opsForZSet().add(key, typedTuples);
        return add;
    }

    public Set<ZSetOperations.TypedTuple<String>> convertToTypedTupleSetByScore(Collection<ZObjScore> collection) {
        return collection.stream()
                .map(zObjScore -> new DefaultTypedTuple<>(zObjScore.getMember().toString(),
                        zObjScore.getScore()))
                .collect(Collectors.toSet());
    }


    /**
     * 根据得分从高到低,范围查询
     *
     * @param
     * @return
     */
    public Set<String> zReverange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 根据得分从低到高,范围查询
     *
     * @param
     * @return
     */
    public Set<String> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }
    // ZSET 相关操作 end ----------------------------------------------------------------------------------------------

    // HyperLogLog 相关操作 start ----------------------------------------------------------------------------------------------










    // HyperLogLog 相关操作 end ----------------------------------------------------------------------------------------------
}


















