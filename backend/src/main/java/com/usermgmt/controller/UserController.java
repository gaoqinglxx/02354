package com.usermgmt.controller;

import com.usermgmt.common.Result;
import com.usermgmt.dto.UpdateUserRequest;
import com.usermgmt.dto.UserInfoResponse;
import com.usermgmt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public Result<UserInfoResponse> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userService.getUserInfo(userId));
    }

    @PutMapping("/info")
    public Result<UserInfoResponse> updateUserInfo(HttpServletRequest request,
                                                   @Valid @RequestBody UpdateUserRequest req) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userService.updateUserInfo(userId, req));
    }
}
