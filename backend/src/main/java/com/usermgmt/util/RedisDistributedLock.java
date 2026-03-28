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
public class RedisDistributedLock {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LOCK_PREFIX = "lock:user:";
    private static final long DEFAULT_WAIT_TIME = 3;
    private static final long DEFAULT_LEASE_TIME = 10;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    private static final String UNLOCK_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "return redis.call('del', KEYS[1]) else return 0 end";

    private static final DefaultRedisScript<Long> unlockScript = new DefaultRedisScript<>();

    static {
        unlockScript.setScriptText(UNLOCK_SCRIPT);
        unlockScript.setResultType(Long.class);
    }

    private final ThreadLocal<String> lockValue = new ThreadLocal<>();

    public boolean tryLock(Long userId) {
        String key = LOCK_PREFIX + userId;
        String value = UUID.randomUUID().toString();
        lockValue.set(value);
        
        Boolean success = redisTemplate.opsForValue()
            .setIfAbsent(key, value, DEFAULT_LEASE_TIME, DEFAULT_TIME_UNIT);
        
        return Boolean.TRUE.equals(success);
    }

    public boolean tryLock(Long userId, long waitTime, long leaseTime, TimeUnit timeUnit) {
        String key = LOCK_PREFIX + userId;
        String value = UUID.randomUUID().toString();
        lockValue.set(value);
        
        long startTime = System.currentTimeMillis();
        long timeout = timeUnit.toMillis(waitTime);
        
        while (System.currentTimeMillis() - startTime < timeout) {
            Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, value, leaseTime, timeUnit);
            
            if (Boolean.TRUE.equals(success)) {
                return true;
            }
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        
        return false;
    }

    public void unlock(Long userId) {
        String key = LOCK_PREFIX + userId;
        String value = lockValue.get();
        if (value != null) {
            try {
                redisTemplate.execute(unlockScript, Collections.singletonList(key), value);
            } finally {
                lockValue.remove();
            }
        }
    }
}
