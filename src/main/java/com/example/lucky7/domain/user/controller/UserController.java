package com.example.lucky7.domain.user.controller;

import com.example.lucky7.domain.common.dto.AuthUser;
import com.example.lucky7.domain.user.dto.request.UserChangePasswordRequest;
import com.example.lucky7.domain.user.dto.response.UserResponse;
import com.example.lucky7.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "회원 관리 API", description = "회원 전체 조회, 단건 조회, 비밀번호 변경이 가능합니다.")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(
            @RequestParam String nickname
    ) {
        return ResponseEntity.ok(userService.getUsers(nickname));
    }

    @PutMapping("/users")
    public void changePassword(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getId(), userChangePasswordRequest);
    }

}
