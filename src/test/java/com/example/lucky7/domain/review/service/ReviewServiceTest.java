//package com.example.lucky7.domain.review.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import com.example.lucky7.domain.common.dto.AuthUser;
//import com.example.lucky7.domain.review.dto.request.ReviewCreateRequest;
//import com.example.lucky7.domain.review.dto.response.ReviewCreateResponse;
//import com.example.lucky7.domain.review.dto.response.ReviewResponse;
//import com.example.lucky7.domain.review.entity.Review;
//import com.example.lucky7.domain.review.enums.StarPoint;
//import com.example.lucky7.domain.review.repository.ReviewQueryDslRepository;
//import com.example.lucky7.domain.review.repository.ReviewRepository;
//import com.example.lucky7.domain.store.entity.Store;
//import com.example.lucky7.domain.store.enums.StoreCategory;
//import com.example.lucky7.domain.store.repository.StoreRepository;
//import com.example.lucky7.domain.user.entity.User;
//import com.example.lucky7.domain.user.enums.UserRole;
//import com.example.lucky7.domain.user.repository.UserRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//class ReviewServiceTest {
//
//    @Mock
//    private ReviewRepository reviewRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private StoreRepository storeRepository;
//
//    @Mock
//    private ReviewQueryDslRepository reviewQueryDslRepository;
//
//    @InjectMocks
//    ReviewService reviewService;
//
//    private User user;
//    private Store store;
//    private Review review;
//
//    @BeforeEach
//    void setUp() {
//        user = new User("test@example.com", "password", "Test User", UserRole.ROLE_USER);
//        store = new Store("Test Store", "address", StoreCategory.CAFE);
//        review = new Review("Great place!", StarPoint.NORMAL.getPointValue(), user, store);
//    }
//
//    @Test
//    void createReview_ShouldReturnReviewCreateResponse() {
//        // given
//        AuthUser authUser = new AuthUser(1L, "test@example.com", "TestUser", UserRole.ROLE_USER);
//        ReviewCreateRequest request = new ReviewCreateRequest("Nice store!", 3L, 1L);
//        when(userRepository.findByEmail(authUser.getEmail())).thenReturn(Optional.of(user));
//        when(storeRepository.findById(request.getStoreId())).thenReturn(Optional.of(store));
//        when(reviewRepository.save(any(Review.class))).thenReturn(review);
//
//        // when
//        ReviewCreateResponse response = reviewService.createReview(authUser, request);
//
//        // then
//        assertThat(response).isNotNull();
//        assertThat(response.getComments()).isEqualTo("Great place!");
//        assertThat(response.getStarPoint().getPointValue()).isEqualTo(3);
//    }
//
//    @Test
//    void createReview_ShouldThrowException_WhenUserNotFound() {
//        // given
//        AuthUser authUser = new AuthUser("notfound@example.com");
//        ReviewCreateRequest request = new ReviewCreateRequest("Nice store!", 4, 1L);
//        when(userRepository.findByEmail(authUser.getEmail())).thenReturn(Optional.empty());
//
//        // when & then
//        assertThrows(EntityNotFoundException.class, () -> reviewService.createReview(authUser, request));
//    }
//
//    @Test
//    void findReviews_ShouldReturnPagedReviews() {
//        // given
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("modifiedAt").descending());
//        List<ReviewResponse> reviewList = List.of(new ReviewResponse(review));
//        Page<ReviewResponse> reviewPage = new PageImpl<>(reviewList, pageable, reviewList.size());
//        when(reviewQueryDslRepository.searchReview(any(Pageable.class))).thenReturn(reviewPage);
//
//        // when
//        Page<ReviewResponse> result = reviewService.findReviews(10, 1);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.getTotalElements()).isEqualTo(1);
//    }
//
//    @Test
//    void deleteReview_ShouldDeleteReview_WhenReviewExists() {
//        // given
//        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
//
//        // when
//        reviewService.deleteReview(1L);
//
//        // then
//        verify(reviewRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void deleteReview_ShouldThrowException_WhenReviewNotFound() {
//        // given
//        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // when & then
//        assertThrows(EntityNotFoundException.class, () -> reviewService.deleteReview(1L));
//    }
//}