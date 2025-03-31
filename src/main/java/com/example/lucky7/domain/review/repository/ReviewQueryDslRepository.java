package com.example.lucky7.domain.review.repository;

import com.example.lucky7.domain.review.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewQueryDslRepository {
    Page<ReviewResponse> searchReview(Pageable pageable);
}
