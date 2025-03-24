package com.example.lucky7.domain.user.service;

import com.example.lucky7.domain.common.dto.AuthUser;
import com.example.lucky7.domain.common.exception.InvalidRequestException;
import com.example.lucky7.domain.user.dto.request.UserRoleChangeRequest;
import com.example.lucky7.domain.user.entity.User;
import com.example.lucky7.domain.user.enums.UserRole;
import com.example.lucky7.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

    @Transactional
    public void changeUserRole(long userId, UserRoleChangeRequest userRoleChangeRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        user.updateRole(UserRole.of(userRoleChangeRequest.getRole()));
    }

    public boolean isAdmin(AuthUser authUser) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new InvalidRequestException("User not found"));
        return UserRole.ROLE_ADMIN.equals(user.getUserRole());
    }
}