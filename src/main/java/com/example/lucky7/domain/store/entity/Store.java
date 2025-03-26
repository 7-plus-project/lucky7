package com.example.lucky7.domain.store.entity;

import com.example.lucky7.domain.common.entity.Timestamped;
import com.example.lucky7.domain.store.dto.request.StoreUpdateRequest;
import com.example.lucky7.domain.store.enums.StoreCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "stores")
@Where(clause = "deleted_at IS NULL")
public class Store extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @Column
    private LocalDateTime deletedAt;

    public Store(String name, String address, StoreCategory category, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void deleteStore(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void updateStore(StoreUpdateRequest request) {
        this.name = request.getName();
        this.address = request.getAddress();
        this.category = request.getCategory();
    }

    /* 범서
    public Store(String name, String address, StoreCategory category) {
        this.name = name;
        this.address = address;
        this.category = category;
    }
    */
}