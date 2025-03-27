package com.example.lucky7.domain.reservation.pessimistic.repository;

import com.example.lucky7.domain.reservation.pessimistic.entity.PessimisticReservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PessimisticReservationRepository extends JpaRepository<PessimisticReservation, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from PessimisticReservation c where c.id = :id")
    Optional<PessimisticReservation> findByIdWithPessimisticLock(Long id);
}