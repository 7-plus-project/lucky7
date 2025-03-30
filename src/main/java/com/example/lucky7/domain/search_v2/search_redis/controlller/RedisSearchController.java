package com.example.lucky7.domain.search_v2.search_redis.controlller;

import com.example.lucky7.domain.search_v2.autocomplete.dto.response.AutoCompleteResponse;
import com.example.lucky7.domain.search_v2.search_redis.dto.response.PopularSearchResponse;
import com.example.lucky7.domain.search_v2.search_redis.service.RedisSearchService;
import com.example.lucky7.domain.store.dto.response.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v2/search-redis")
public class RedisSearchController {

    private final RedisSearchService redisSearchService;
    // 가게 조회 기능
    @GetMapping
    public ResponseEntity<Page<StoreResponse>> searchStores(@RequestParam(required = false) String name,
                                                           @RequestParam(required = false) String category,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(redisSearchService.searchStores(name, category, page, size));
    }

    // 가게 인기검색어 조회 기능
    @GetMapping("/popular")
    public ResponseEntity<PopularSearchResponse> getPopularKeywordWithRedis(@RequestParam String keyword,
                                                                            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(redisSearchService.getPopularSearches(keyword, limit));
    }

    // 가게 검색 시 자동완성 기능
    @GetMapping("/autocomplete")
    public ResponseEntity<AutoCompleteResponse> getAutoCompleteSearches(@RequestParam String keyword,
                                                                        @RequestParam String prefix,
                                                                        @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(redisSearchService.getSuggestions(keyword, prefix, limit));
    }
}
