package com.example.lucky7.domain.search.controller;

import com.example.lucky7.config.RestPage;
import com.example.lucky7.domain.search.dto.response.SearchResponse;
import com.example.lucky7.domain.search.service.SearchService;
import com.example.lucky7.domain.store.enums.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/v1/search")
    public ResponseEntity<Page<SearchResponse>> searchStore(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) StoreCategory category
    ){
        return ResponseEntity.ok(searchService.getStores(page,size,name,category));
    }

    @GetMapping("/v1/search/popular")
    public ResponseEntity<Page<String>> getPopularKeyword(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        return ResponseEntity.ok(searchService.getTopKeywords(pageable));
    }

    // ------------- redis cache 적용한 v2 search -------------

    @GetMapping("/v2/search")
    public ResponseEntity<RestPage<SearchResponse>> searchStoreWithCache(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) StoreCategory category
    ){
        return ResponseEntity.ok(searchService.findStoresWithCache(page,size,name,category));
    }


}
