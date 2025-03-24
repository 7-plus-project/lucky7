package com.example.lucky7.domain.review.dto.response;

import com.example.lucky7.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String comments;
    private Long pointValue;
    private String nickname;

    public static ReviewResponse toDto(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getComments(),
                review.getStarPoint().getPointValue(),
                review.getUser().getNickname()
        );
    }
}
