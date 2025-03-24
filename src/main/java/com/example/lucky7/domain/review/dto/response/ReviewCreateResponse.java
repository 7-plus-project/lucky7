package com.example.lucky7.domain.review.dto.response;

import com.example.lucky7.domain.review.entity.Review;
import com.example.lucky7.domain.review.enums.StarPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateResponse {
    private Long id;
    private String comments;
    private Long userId;
    private StarPoint starPoint;

    public static ReviewCreateResponse toDto(Review review) {
        return new ReviewCreateResponse(
                review.getId(),
                review.getComments(),
                review.getUser().getId(),
                review.getStarPoint()
        );
    }
}
