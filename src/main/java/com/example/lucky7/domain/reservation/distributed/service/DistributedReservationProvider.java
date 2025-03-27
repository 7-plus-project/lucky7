package com.example.lucky7.domain.reservation.distributed.service;

import com.example.lucky7.domain.reservation.distributed.entity.DistributedReservation;
import com.example.lucky7.domain.reservation.distributed.repository.DistributedReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DistributedReservationProvider {

    private final DistributedReservationRepository distributedReservationRepository;

    @Transactional
    public void save(DistributedReservation distributedReservation) {
        distributedReservationRepository.save(distributedReservation);
    }

    @Transactional
    public void decrementAvailableSpots(Long reservationId) {
        DistributedReservation distributedReservation = distributedReservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));

        distributedReservation.decreaseAvailableSpots();
        distributedReservationRepository.save(distributedReservation);
    }

    public DistributedReservation getReservationById(Long reservationId) {
        return distributedReservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
    }
}
