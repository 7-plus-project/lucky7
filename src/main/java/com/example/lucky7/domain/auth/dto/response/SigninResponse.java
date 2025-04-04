package com.example.lucky7.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SigninResponse {

    private final String bearerToken;

    public SigninResponse(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
