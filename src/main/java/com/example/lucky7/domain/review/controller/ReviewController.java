package com.example.lucky7.domain.review.controller;

import com.example.lucky7.domain.common.dto.AuthUser;
import com.example.lucky7.domain.review.dto.request.ReviewCreateRequest;
import com.example.lucky7.domain.review.dto.response.ReviewCreateResponse;
import com.example.lucky7.domain.review.dto.response.ReviewResponse;
import com.example.lucky7.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<ReviewCreateResponse> createReview(@AuthenticationPrincipal AuthUser authUser,
                                                             @RequestBody @Valid ReviewCreateRequest reviewCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(authUser, reviewCreateRequest));
    }
    // 리뷰 조회
    @GetMapping
    public ResponseEntity<Page<ReviewResponse>> getReviews(@RequestParam(defaultValue = "1") int size,
                                                           @RequestParam(defaultValue = "10") int page) {
        return ResponseEntity.ok(reviewService.findReviews(size, page));
    }
    // 리뷰 삭제
    @PutMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
}
