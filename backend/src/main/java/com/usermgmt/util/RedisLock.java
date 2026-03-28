package com.usermgmt.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLock {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LOCK_PREFIX = "lock:";
    private static final long DEFAULT_WAIT_TIME = 3; // 默认等待时间，单位：秒
    private static final long DEFAULT_EXPIRE_TIME = 10; // 默认过期时间，单位：秒

    /**
     * 尝试获取分布式锁
     * @param key 锁的key
     * @return 是否获取成功
     */
    public boolean tryLock(String key) {
        return tryLock(key, DEFAULT_WAIT_TIME, DEFAULT_EXPIRE_TIME);
    }

    /**
     * 尝试获取分布式锁
     * @param key 锁的key
     * @param waitTime 等待时间（秒）
     * @param expireTime 过期时间（秒）
     * @return 是否获取成功
     */
    public boolean tryLock(String key, long waitTime, long expireTime) {
        String lockKey = LOCK_PREFIX + key;
        String lockValue = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        try {
            while (System.currentTimeMillis() - startTime < waitTime * 1000) {
                // 使用setIfAbsent尝试获取锁
                Boolean success = redisTemplate.opsForValue().setIfAbsent(
                        lockKey,
                        lockValue,
                        expireTime,
                        TimeUnit.SECONDS
                );

                if (Boolean.TRUE.equals(success)) {
                    log.info("获取锁成功: {}", lockKey);
                    return true;
                }

                // 等待一小段时间后重试
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取锁时被中断: {}", lockKey, e);
        }

        log.warn("获取锁超时: {}", lockKey);
        return false;
    }

    /**
     * 释放分布式锁
     * @param key 锁的key
     */
    public void unlock(String key) {
        String lockKey = LOCK_PREFIX + key;
        // 使用Lua脚本保证原子性
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else " +
                "return 0 " +
                "end";

        try {
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptText(script);
            redisScript.setResultType(Long.class);

            String currentValue = (String) redisTemplate.opsForValue().get(lockKey);
            if (currentValue != null) {
                Long result = redisTemplate.execute(
                        redisScript,
                        Collections.singletonList(lockKey),
                        currentValue
                );

                if (result != null && result > 0) {
                    log.info("释放锁成功: {}", lockKey);
                } else {
                    log.warn("锁已过期或不属于当前线程: {}", lockKey);
                }
            }
        } catch (Exception e) {
            log.error("释放锁失败: {}", lockKey, e);
        }
    }
}
