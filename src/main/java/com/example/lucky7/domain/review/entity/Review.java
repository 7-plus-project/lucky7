package com.example.lucky7.domain.review.entity;

import com.example.lucky7.domain.common.entity.Timestamped;
import com.example.lucky7.domain.review.enums.ReviewState;
import com.example.lucky7.domain.review.enums.StarPoint;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "reviews")
@Entity
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StarPoint starPoint;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private ReviewState reviewState;

    @JoinColumn(name = "store_id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Store store;

    public Review(String comments, Long pointValue, User user, Store store) {
        this.comments = comments;
        this.starPoint = StarPoint.of(pointValue);
        this.user = user;
        this.store = store;
        this.reviewState = ReviewState.ACTIVE;
    }

    public static void deleteReview(Review review) {
        review.reviewState = ReviewState.INACTIVE;
    }
}
