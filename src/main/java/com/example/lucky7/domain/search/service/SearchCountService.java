package com.example.lucky7.domain.search.service;

import com.example.lucky7.domain.popularsearch.PopularSearchRepository;
import com.example.lucky7.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchCountService {

    private final SearchRepository searchRepository;
    private final PopularSearchRepository popularSearchRepository;

    @Async
    @Transactional
    public void increaseSearchCount(String name, String category) {
        if (name != null) {
            searchRepository.increaseKeywordCount(name);
            popularSearchRepository.incrementSearchCount(name);
        }

        if (category != null) {
            searchRepository.increaseKeywordCount(category);
            popularSearchRepository.incrementSearchCount(category);
        }
    }

}
