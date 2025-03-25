package com.example.lucky7.domain.search.repository;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchRepositoryCustom {
    Page<Store> findStoresByQuerydsl(String name, StoreCategory category, Pageable pageable);
}
