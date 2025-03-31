package com.example.lucky7.domain.review.dto.response;

import com.example.lucky7.domain.review.entity.Review;
import com.example.lucky7.domain.review.enums.StarPoint;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
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
    private String storeName;

    @QueryProjection
    public ReviewResponse(Long id, String comments, StarPoint starPoint, User user, Store store) {
        this.id = id;
        this.comments = comments;
        this.pointValue = starPoint.getPointValue();
        this.nickname = user.getNickname();
        this.storeName = store.getName();
    }
}
