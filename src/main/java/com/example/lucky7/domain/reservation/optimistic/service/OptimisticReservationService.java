package com.example.lucky7.domain.reservation.optimistic.service;

import com.example.lucky7.domain.reservation.event.Event;
import com.example.lucky7.domain.reservation.event.EventRepository;
import com.example.lucky7.domain.reservation.optimistic.entity.OptimisticReservation;
import com.example.lucky7.domain.reservation.optimistic.repository.OptimisticReservationRepository;
import com.example.lucky7.domain.user.entity.User;
import com.example.lucky7.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OptimisticReservationService {

    private final OptimisticReservationRepository optimisticReservationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

//    @Transactional
//    public OptimisticReservation saveReservation(OptimisticReservation optimisticReservation) {
//        return optimisticReservationRepository.save(optimisticReservation);
//    }

    @Transactional
    public void saveReservation(Event event, User user) {
        for (int i = 0; i < 3; i++) {
            try {
//                if (!isReservationAvailable(event)) {
//                    throw new IllegalStateException("예약이 마감되었습니다.");
//                }

                if (optimisticReservationRepository.existsByEventAndUser(event, user)) {
                    throw new IllegalStateException("이미 예약하셨습니다.");
                }

                Event foundEvent = eventRepository.findById(event.getId())
                        .orElseThrow(() -> new IllegalStateException("이벤트를 찾을 수 없습니다."));

                User foundUser = userRepository.findById(user.getId())
                        .orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));

                OptimisticReservation reservation = OptimisticReservation.builder()
                        .event(foundEvent)
                        .user(foundUser)
                        .reservationAt(LocalDateTime.now())
                        .build();

                event.decreaseAvailableSpots();
                optimisticReservationRepository.save(reservation);
                return;
            } catch (OptimisticLockingFailureException e) {
                if (i == 2) {
                    throw e;
                }
            }
        }
    }

//    public Boolean isReservationAvailable(Event event) {
//        int currentReservationCount = getCurrentReservationCount(event);
//        return currentReservationCount < event.getTotalCapacity();
//    }
//
//    @Transactional(readOnly = true)
//    public int getCurrentReservationCount(Event event) {
//        return optimisticReservationRepository.countReservationsByEvent(event);
//    }

    @Transactional(readOnly = true)
    public OptimisticReservation getReservationById(Long reservationId) {
        return optimisticReservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
    }

//    @Transactional
//    public void decrementAvailableSpots(Long reservationId) {
//        for (int i = 0; i < 3; i++) {
//            try {
//                OptimisticReservation optimisticReservation = optimisticReservationRepository.findById(reservationId)
//                        .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
//                optimisticReservation.decreaseAvailableSpots();
//                optimisticReservationRepository.save(optimisticReservation);
//                return;
//            } catch (OptimisticLockingFailureException e) {
//                if (i == 2) {
//                    throw e;
//                }
//            }
//        }
//
//    }
}