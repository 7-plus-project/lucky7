package com.example.lucky7.domain.review.service;

import com.example.lucky7.domain.common.dto.AuthUser;
import com.example.lucky7.domain.review.dto.request.ReviewCreateRequest;
import com.example.lucky7.domain.review.dto.response.ReviewCreateResponse;
import com.example.lucky7.domain.review.dto.response.ReviewResponse;
import com.example.lucky7.domain.review.entity.Review;
import com.example.lucky7.domain.review.enums.ReviewState;
import com.example.lucky7.domain.review.repository.ReviewRepository;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.repository.StoreRepository;
import com.example.lucky7.domain.user.entity.User;
import com.example.lucky7.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    // 리뷰 생성
    @Transactional
    public ReviewCreateResponse createReview(AuthUser authUser, ReviewCreateRequest reviewCreateRequest) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        Store store = storeRepository.findById(reviewCreateRequest.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException("Store Not Found"));
        Review review = new Review(reviewCreateRequest.getComments(), reviewCreateRequest.getPointValue(), user, store);
        Review savedReview = reviewRepository.save(review);
        return ReviewCreateResponse.toDto(savedReview);
    }

    // 리뷰 전체 조회
    @Transactional(readOnly = true)
    public Page<ReviewResponse> findReviews(int size, int page) {
        int adjust = page <= 0 ? 1 : page--;
        Pageable pageable = PageRequest.of(adjust, size, Sort.by("modifiedAt").descending());
        return reviewRepository.findByReviewState(pageable, ReviewState.ACTIVE)
                .map(ReviewResponse::toDto);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long id) {
     Review review = reviewRepository.findById(id)
             .orElseThrow(() -> new EntityNotFoundException("Review Not Found"));
     Review.deleteReview(review);
    }
}
