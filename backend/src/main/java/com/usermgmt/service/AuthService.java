package com.usermgmt.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.usermgmt.common.BusinessException;
import com.usermgmt.dto.LoginRequest;
import com.usermgmt.dto.RegisterRequest;
import com.usermgmt.entity.User;
import com.usermgmt.mapper.UserMapper;
import com.usermgmt.util.JwtUtil;
import com.usermgmt.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Transactional
    public void register(RegisterRequest req) {
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
