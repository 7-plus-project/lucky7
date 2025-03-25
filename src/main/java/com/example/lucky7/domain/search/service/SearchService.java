package com.example.lucky7.domain.search.service;

import com.example.lucky7.domain.search.dto.response.SearchResponse;
import com.example.lucky7.domain.search.repository.SearchRepository;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final SearchRepository searchRepository;

    public Page<SearchResponse> getStores(int page, int size, String name, StoreCategory category) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        Page<Store> stores = searchRepository.findStoresByQuerydsl(name,category,pageable);

        return stores.map(store -> new SearchResponse(
                store.getId(),
                store.getName(),
                store.getCategory()
        ));
    }
}
