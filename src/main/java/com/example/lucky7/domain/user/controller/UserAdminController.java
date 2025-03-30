package com.example.lucky7.domain.user.controller;

import com.example.lucky7.domain.common.dto.AuthUser;
import com.example.lucky7.domain.common.exception.ServerException;
import com.example.lucky7.domain.user.dto.request.UserRoleChangeRequest;
import com.example.lucky7.domain.user.service.UserAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "사용자 권한 API")
public class UserAdminController {

    private final UserAdminService userAdminService;

    @PatchMapping("/api/v1/admin/users/{userId}")
    @Operation(summary = "UserRole 변경", description = "UserRole 변경은 관리자만 가능합니다.")
    public void changeUserRole(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long userId,
            @RequestBody UserRoleChangeRequest userRoleChangeRequest
    ) {
        if (!userAdminService.isAdmin(authUser)) {
            throw new ServerException("권한이 없습니다.");
        }

        userAdminService.changeUserRole(userId, userRoleChangeRequest);
    }
}
