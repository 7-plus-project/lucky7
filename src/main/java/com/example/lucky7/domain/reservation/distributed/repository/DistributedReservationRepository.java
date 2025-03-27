package com.example.lucky7.domain.reservation.distributed.repository;

import com.example.lucky7.domain.reservation.distributed.entity.DistributedReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributedReservationRepository extends JpaRepository<DistributedReservation, Long> {
}