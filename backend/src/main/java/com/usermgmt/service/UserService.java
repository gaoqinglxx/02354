package com.usermgmt.service;

import com.usermgmt.common.BusinessException;
import com.usermgmt.dto.UpdateUserRequest;
import com.usermgmt.dto.UserInfoResponse;
import com.usermgmt.entity.User;
import com.usermgmt.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return UserInfoResponse.fromEntity(user);
    }

    @Transactional
    public UserInfoResponse updateUserInfo(Long userId, UpdateUserRequest req) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (req.getGender() != null) user.setGender(req.getGender());
        if (req.getAge() != null) user.setAge(req.getAge());
        if (req.getProfession() != null) user.setProfession(req.getProfession());
        if (req.getAddress() != null) user.setAddress(req.getAddress());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.updateById(user);
        log.info("用户信息更新成功: userId={}", userId);
        return UserInfoResponse.fromEntity(user);
    }
}
