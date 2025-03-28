package com.example.lucky7.domain.reservation.optimistic.entity;

import com.example.lucky7.domain.reservation.event.Event;
import com.example.lucky7.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OptimisticReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private int availableSpots;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime reservationAt;

//    @Version // 낙관적 락을 위한 버전 관리
//    private int version;

    @Builder
    public static OptimisticReservation create(
            Event event,
            User user,
            LocalDateTime reservationAt
    ) {
        return new OptimisticReservation(
                (Long) null,
                event,
                user,
                reservationAt
        );
    }

}