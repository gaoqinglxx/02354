package com.usermgmt.controller;

import com.usermgmt.common.Result;
import com.usermgmt.dto.LoginRequest;
import com.usermgmt.dto.RegisterRequest;
import com.usermgmt.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req);
        return Result.success(Map.of("token", token));
    }
}
