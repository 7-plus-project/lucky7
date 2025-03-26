package com.example.lucky7.domain.store.repository;

import com.example.lucky7.domain.store.entity.Store;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s " +
            "WHERE (s.modifiedAt BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) "+
            "ORDER BY s.modifiedAt DESC")
    Page<Store> findAllByOrderByModifiedAtDesc(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

    /* MYSQL 위치 검색 - 메서드 추가 */
    // 사용자 위치 기반 맛집 조회 목록 - 거리순으로 정렬
    @Query("SELECT s FROM Store s " +
            "WHERE ST_Contains(ST_Buffer(:userLocation, :meterRange), s.location) " + // ST_Buffer와 ST_Contains로 spatial index 활용
            "ORDER BY ST_Distance(s.location, :userLocation) ASC") // 거리가 가까운 가게부터 정렬
    List<Store> findStoresByUserLocationOrderByDistance(@Param("userLocation") Point userLocation, @Param("meterRange") double meterRange);
}
