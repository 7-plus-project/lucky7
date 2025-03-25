package com.example.lucky7.domain.store.repository;

import com.example.lucky7.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s " +
            "WHERE (s.modifiedAt BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) "+
            "ORDER BY s.modifiedAt DESC")
    Page<Store> findAllByOrderByModifiedAtDesc(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);
}
