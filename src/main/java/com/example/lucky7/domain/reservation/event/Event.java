package com.example.lucky7.domain.reservation.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int availableSpots;
    private LocalDateTime eventDate;

    @Version
    private Integer version; // 낙관적 락을 위한 필드

    public void decreaseAvailableSpots() {
        if (availableSpots > 0) {
            availableSpots--;
        } else {
            throw new RuntimeException("예약이 마감되었습니다");
        }
    }
}
