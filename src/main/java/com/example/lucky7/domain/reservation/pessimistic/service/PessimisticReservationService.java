package com.example.lucky7.domain.reservation.pessimistic.service;

import com.example.lucky7.domain.reservation.pessimistic.entity.PessimisticReservation;
import com.example.lucky7.domain.reservation.pessimistic.repository.PessimisticReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PessimisticReservationService {

    private final PessimisticReservationRepository pessimisticReservationRepository;

    @Transactional
    public PessimisticReservation saveReservation(PessimisticReservation pessimisticReservation) {
        return pessimisticReservationRepository.save(pessimisticReservation);
    }

    @Transactional
    public void lockReservationAndHold(Long reservationId) throws InterruptedException {
        PessimisticReservation reservation = pessimisticReservationRepository.findByIdWithPessimisticLock(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
        System.out.println("락을 획득했습니다. 5초 동안 유지합니다...");
        Thread.sleep(5000);
        reservation.decreaseAvailableSpots();
        pessimisticReservationRepository.save(reservation);
    }

    @Transactional
    public void decrementAvailableSpotstWithPessimisticLock(Long counterId) {
        PessimisticReservation reservation = pessimisticReservationRepository.findByIdWithPessimisticLock(counterId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
        reservation.decreaseAvailableSpots();
        pessimisticReservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public PessimisticReservation getReservationById(Long reservationId) {
        return pessimisticReservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
    }
}