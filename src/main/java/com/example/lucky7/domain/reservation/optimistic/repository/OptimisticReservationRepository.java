package com.example.lucky7.domain.reservation.optimistic.repository;

import com.example.lucky7.domain.reservation.optimistic.entity.OptimisticReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptimisticReservationRepository extends JpaRepository<OptimisticReservation, Long> {
}