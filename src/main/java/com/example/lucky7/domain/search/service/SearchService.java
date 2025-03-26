package com.example.lucky7.domain.search.service;

import com.example.lucky7.domain.search.dto.response.SearchResponse;
import com.example.lucky7.domain.search.repository.SearchRepository;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final SearchRepository searchRepository;

    @Transactional
    public void increaseSearchCount(String name, String category) {
        if (name != null) {
            searchRepository.increaseKeywordCount(name);
        }

        if (category != null) {
            searchRepository.increaseKeywordCount(category);
        }
    }

    public Page<String> getTopKeywords(Pageable pageable) {
        return searchRepository.findTopKeyword(pageable);
    }

    public Page<SearchResponse> getStores(int page, int size, String name, StoreCategory category) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        increaseSearchCount(name, category != null ? category.toString() : null);

        Page<Store> stores = searchRepository.findStores(name, category, pageable);

        return stores.map(store -> new SearchResponse(
                store.getId(),
                store.getName(),
                store.getCategory()
        ));
    }

    @Transactional
    @Scheduled(cron = "0 0 * * * *") // 정각마다 실행
    public void resetSearchCounts() {
        searchRepository.resetAllSearchCounts();
        System.out.println("카운트 초기화");
    }
}
