package com.example.lucky7.domain.store.repository;

import com.example.lucky7.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {}