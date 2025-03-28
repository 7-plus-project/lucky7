package com.example.lucky7.reservation.optimistic.service;

import com.example.lucky7.domain.reservation.event.Event;
import com.example.lucky7.domain.reservation.event.EventRepository;
import com.example.lucky7.domain.reservation.optimistic.service.OptimisticReservationService;
import com.example.lucky7.domain.user.entity.User;
import com.example.lucky7.domain.user.enums.UserRole;
import com.example.lucky7.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OptimisticReservationServiceTest {

    @Autowired
    private OptimisticReservationService optimisticReservationService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    private Event testEvent;

    @BeforeEach
    public void setup() {
        testEvent = new Event(1L, "Test Event", 100, LocalDateTime.now(), 0);
        eventRepository.save(testEvent);
    }

    @Test
    public void testOptimisticLock() throws InterruptedException {
        int testCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(testCount);

        // 예외 발생 횟수를 추적하기 위한 변수
        AtomicInteger optimisticLockExceptionCount = new AtomicInteger(0);
        AtomicInteger successfulReservations = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < testCount; i++) {
            final int userIndex = i;
            executorService.submit(() -> {
                try {
                    User concurrentUser = new User("user"+userIndex+"@example.com","Testpassword1234","user"+userIndex, UserRole.ROLE_USER);
                    userRepository.save(concurrentUser);
                    optimisticReservationService.saveReservation(testEvent, concurrentUser);
                    successfulReservations.incrementAndGet(); //예약 성공 횟수 증가
                } catch (OptimisticLockingFailureException e) {
                    // 낙관적 락 충돌 발생 시 처리 및 예외 카운트 증가
                    optimisticLockExceptionCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 작업을 완료할 때까지 대기
        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        long durationInMillis = endTime - startTime;
        double durationInSeconds = durationInMillis / 1000.0;

        System.out.println("발생한 예외 수: " + optimisticLockExceptionCount.get());
        System.out.println("성공한 예약 수: " + successfulReservations.get());
        assertTrue(optimisticLockExceptionCount.get() > 0, "낙관적 락 충돌이 발생해야 합니다.");
        assertEquals(100, successfulReservations.get(), "정확히 100명만 예약에 성공해야 합니다.");

        // 예약 가능한 인원은 정확히 0이어야 함
//        int finalCount = optimisticReservationService.getReservationById(optimisticReservationId).getAvailableSpots();
        int finalCount = testEvent.getAvailableSpots();
        assertEquals(0, finalCount, "예약 가능 인원은 0이어야 합니다.");

//        System.out.println("남은 예약 가능인원: " + finalCount);
        System.out.println("테스트 실행 시간: " + durationInSeconds + "초");
    }

}
