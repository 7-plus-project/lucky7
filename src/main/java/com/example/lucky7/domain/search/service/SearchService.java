package com.example.lucky7.domain.search.service;

import com.example.lucky7.config.RestPage;
import com.example.lucky7.domain.search.dto.response.SearchResponse;
import com.example.lucky7.domain.search.repository.SearchRepository;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final SearchRepository searchRepository;
    private final SearchCountService searchCountService;

    @Transactional
    public Page<SearchResponse> getStores(int page, int size, String name, StoreCategory category) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        searchCountService.increaseSearchCount(name, category != null ? category.toString() : null);
        Page<Store> stores = searchRepository.findStores(name, category, pageable);
        return stores.map(store -> new SearchResponse(
                store.getId(),
                store.getName(),
                store.getCategory()
        ));
    }



    public Page<String> getTopKeywords(Pageable pageable) {
        return searchRepository.findTopKeyword(pageable);
    }

    @Transactional
    @Scheduled(cron = "0 0 * * * *") // 정각마다 실행
    public void resetSearchCounts() {
        searchRepository.resetAllSearchCounts();
        System.out.println("카운트 초기화");
    }

    // ------------- redis cache 적용한 v2 search -------------

    @Cacheable(value = "storeSearch", key = "#name != null ? #name : 'all' + ':' + (#category != null ? #category : 'all') + ':' + #page")
    public RestPage<SearchResponse> findStoresWithCache(int page, int size, String name, StoreCategory category) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);

        RestPage<Store> stores = searchRepository.findStoresWithCache(name, category, pageable);

        List<SearchResponse> responses = stores.getContent().stream()
                .map(SearchResponse::toDto)
                .toList();

        return new RestPage<>(responses, pageable, stores.getTotalElements());
    }
}
