package com.example.lucky7.domain.review.repository;

import com.example.lucky7.domain.review.dto.response.QReviewResponse;
import com.example.lucky7.domain.review.dto.response.ReviewResponse;
import com.example.lucky7.domain.review.enums.ReviewState;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.example.lucky7.domain.review.entity.QReview.review;

@RequiredArgsConstructor
@Repository
public class ReviewQueryDslRepositoryImpl implements ReviewQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 리뷰 검색
    public Page<ReviewResponse> searchReview(Pageable pageable) {
        List<ReviewResponse> results = jpaQueryFactory.select(new QReviewResponse(
                        review.id, review.comments, review.starPoint, review.user, review.store
                ))
                .from(review)
                .where(review.reviewState.eq(ReviewState.ACTIVE))
                .orderBy(review.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Objects.requireNonNullElse(jpaQueryFactory.select(review.count())
                .from(review)
                .where(review.reviewState.eq(ReviewState.ACTIVE))
                .orderBy(review.modifiedAt.desc())
                // default 값 0 지정
                .fetchOne(), 0L);

        return new PageImpl<>(results, pageable, total);
    }

}
