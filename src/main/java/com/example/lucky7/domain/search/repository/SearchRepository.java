package com.example.lucky7.domain.search.repository;

import com.example.lucky7.domain.search.entity.Search;
import com.example.lucky7.domain.store.enums.StoreCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long>, SearchRepositoryCustom {
    //JPQL 사용 버전
//    @Query("SELECT s FROM Store s " +
//            "WHERE (:name IS NOT NULL AND s.name LIKE %:name%) " +
//            "OR (:category IS NOT NULL AND s.category = :category)")
//    Page<Store> findStores(
//            @Param("name") String name,
//            @Param("category") StoreCategory category,
//            Pageable pageable
//    );
}
