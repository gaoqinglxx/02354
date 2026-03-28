package com.usermgmt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.usermgmt.common.BusinessException;
import com.usermgmt.dto.LoginRequest;
import com.usermgmt.dto.RegisterRequest;
import com.usermgmt.entity.User;
import com.usermgmt.mapper.UserMapper;
import com.usermgmt.util.JwtUtil;
import com.usermgmt.util.PasswordUtil;
import com.usermgmt.util.RedisDistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RedisDistributedLock redisDistributedLock;

    @Transactional
    public void register(RegisterRequest req) {
        String lockKey = "register:" + req.getUsername();
        boolean locked = redisDistributedLock.tryLock(
            Math.abs(lockKey.hashCode()) % 1000000L, 
            3, 
            10, 
            TimeUnit.SECONDS
        );
        if (!locked) {
            log.warn("获取注册锁失败: username={}", req.getUsername());
            throw new BusinessException("系统繁忙，请稍后重试");
        }

        try {
            // 检查用户名是否已存在
            Long count = userMapper.selectCount(
                    new LambdaQueryWrapper<User>().eq(User::getUsername, req.getUsername())
            );
            if (count > 0) {
                throw new BusinessException("用户名已存在");
            }

            User user = new User();
            user.setUsername(req.getUsername());
            user.setPassword(PasswordUtil.encode(req.getPassword()));
            user.setGender(req.getGender() != null ? req.getGender() : 1);
            user.setAge(req.getAge());
            user.setProfession(req.getProfession());
            user.setAddress(req.getAddress());
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());

            userMapper.insert(user);
            log.info("用户注册成功: {}", req.getUsername());
        } finally {
            redisDistributedLock.unlock(Math.abs(lockKey.hashCode()) % 1000000L);
        }
    }

    public String login(LoginRequest req) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, req.getUsername())
        );
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (!PasswordUtil.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        log.info("用户登录成功: {}", req.getUsername());
        return jwtUtil.generateToken(user.getId(), user.getUsername());
    }
}
