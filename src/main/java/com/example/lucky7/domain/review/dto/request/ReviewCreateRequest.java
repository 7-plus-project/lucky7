package com.example.lucky7.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewCreateRequest {
    @NotBlank(message = "내용 값 입력은 필수입니다")
    private String comments;
    @NotNull(message = "별점 값 입력은 필수입니다")
    private Long pointValue;
}
