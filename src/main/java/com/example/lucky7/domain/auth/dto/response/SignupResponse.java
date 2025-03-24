package com.example.lucky7.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {

    private final String bearerToken;

    public SignupResponse(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
