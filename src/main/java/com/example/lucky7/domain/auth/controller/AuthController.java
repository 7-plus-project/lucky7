package com.example.lucky7.domain.auth.controller;

import com.example.lucky7.domain.auth.dto.request.SigninRequest;
import com.example.lucky7.domain.auth.dto.request.SignupRequest;
import com.example.lucky7.domain.auth.dto.response.SigninResponse;
import com.example.lucky7.domain.auth.dto.response.SignupResponse;
import com.example.lucky7.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "회원가입 및 로그인 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "관리자와 일반 회원으로 가입이 가능합니다.")
    public SignupResponse signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/signin")
    @Operation(summary = "로그인", description = "관리자와 일반 회원으로 로그인이 가능합니다.")
    public SigninResponse signin(@Valid @RequestBody SigninRequest signinRequest) {
        return authService.signin(signinRequest);
    }
}
