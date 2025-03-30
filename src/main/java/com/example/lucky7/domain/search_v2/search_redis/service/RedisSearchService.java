package com.example.lucky7.domain.search_v2.search_redis.service;

import com.example.lucky7.domain.search.entity.Search;
import com.example.lucky7.domain.search.repository.SearchRepositoryCustom;
import com.example.lucky7.domain.search_v2.search_redis.repository.RedisSearchRepository;
import com.example.lucky7.domain.search_v2.autocomplete.dto.response.AutoCompleteResponse;
import com.example.lucky7.domain.search_v2.autocomplete.repository.AutoCompleteRepository;
import com.example.lucky7.domain.search_v2.search_redis.dto.response.PopularSearchResponse;
import com.example.lucky7.domain.store.dto.response.StoreResponse;
import com.example.lucky7.domain.store.enums.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RedisSearchService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisSearchRepository redisSearchRepository;
    private final SearchRepositoryCustom searchRepository;
    private final AutoCompleteRepository autoCompleteRepository;

    // 가게 조회시 redis sorted set 에 저장하기
    public Page<StoreResponse> searchStores(String name, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // category 값 null 일 경우 처리
        StoreCategory storeCategory = (category != null && !category.isEmpty()) ? StoreCategory.of(category) : null;
        // 가게 조회
        Page<StoreResponse> results = searchRepository.findStoresByDto(name, storeCategory, pageable);
        // keyword 와 count sorted set 에 저장하기
        if (category != null && category.isEmpty() ) { // name 값이 없을 경우
            redisSearchRepository.incrementSearchCount(category);
        }
        if (name != null && name.isEmpty()) { // category 값이 없을 경우
            redisSearchRepository.incrementSearchCount(name);
        }
        return results;
    }

    // 가게 인기검색어 조회 기능
    public PopularSearchResponse getPopularSearches(String keyword, int limit) {
        return new PopularSearchResponse(redisSearchRepository.getPopularSearches(keyword, limit));
    }

    // 자동완성 추천 검색어 조회 (Redis → DB 캐싱)
    public AutoCompleteResponse getSuggestions(String keyword, String prefix, int limit) {
        Set<String> redisResults = redisSearchRepository.getAutoComplete(keyword, prefix, limit);
        // Redis 데이터가 없을 경우
        if (!redisResults.isEmpty()) {
            return new AutoCompleteResponse(redisResults);
        }
        // Redis 값이 없으면 DB 조회 후 DB 데이터 Redis 캐싱
        List<Search> dbResults = autoCompleteRepository.getAutoComplete(prefix, limit);
        dbResults.forEach(search -> redisTemplate.opsForZSet()
                // sorted set - ZADD - 캐싱 값 없으므로 keyword 값과 count 값 score로 등록
                .add(search.getKeyword(), search.getKeyword(), search.getCount().doubleValue()));
        return new AutoCompleteResponse(dbResults.stream()
                .map(Search::getKeyword)
                .collect(Collectors.toSet()));
    }
}

