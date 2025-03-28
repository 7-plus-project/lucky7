package com.example.lucky7.domain.reservation.optimistic.repository;

import com.example.lucky7.domain.reservation.event.Event;
import com.example.lucky7.domain.reservation.optimistic.entity.OptimisticReservation;
import com.example.lucky7.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OptimisticReservationRepository extends JpaRepository<OptimisticReservation, Long> {
//    @Query("SELECT COUNT(r) FROM OptimisticReservation r WHERE r.event = :event")
//    int countReservationsByEvent(@Param("event") Event event);

    boolean existsByEventAndUser(Event event, User user);
}