package com.example.lucky7.domain.review.repository;

import com.example.lucky7.domain.review.entity.Review;
import com.example.lucky7.domain.review.enums.ReviewState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.example.lucky7.domain.review.enums.ReviewState.INACTIVE;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByReviewState(Pageable pageable, ReviewState reviewState);
}
