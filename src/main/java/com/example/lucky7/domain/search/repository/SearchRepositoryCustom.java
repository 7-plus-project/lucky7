package com.example.lucky7.domain.search.repository;

import com.example.lucky7.config.RestPage;
import com.example.lucky7.domain.store.dto.response.StoreResponse;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchRepositoryCustom {
    Page<Store> findStores(String name, StoreCategory category, Pageable pageable);
    Page<String> findTopKeyword(Pageable pageable);
    void increaseKeywordCount(String keyword);
    void resetAllSearchCounts();

    RestPage<Store> findStoresWithCache(String name, StoreCategory category, Pageable pageable);

    Page<StoreResponse> findStoresByDto(String name, StoreCategory category, Pageable pageable);
}
