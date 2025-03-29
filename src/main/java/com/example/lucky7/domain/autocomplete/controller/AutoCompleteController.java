package com.example.lucky7.domain.autocomplete.controller;

import com.example.lucky7.domain.autocomplete.dto.response.AutoCompleteResponse;
import com.example.lucky7.domain.autocomplete.service.RedisAutoCompleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/autocomplete")
public class AutoCompleteController {

    private final RedisAutoCompleteService autoCompleteService;

    // 검색어 저장 API
    @PostMapping("/save")
    public ResponseEntity<Void> saveKeyword(@RequestParam String prefix, @RequestParam String keyword, @RequestParam Long count) {
        autoCompleteService.saveKeyword(keyword, prefix, count);
        return ResponseEntity.ok().build();
    }

    // 자동완성 추천 검색어 조회 API
    @GetMapping("/suggestions")
    public ResponseEntity<AutoCompleteResponse> getSuggestions(@RequestParam String keyword,
                                                               @RequestParam String prefix,
                                                               // 가져올 자동완성 값 개수
                                                               @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(autoCompleteService.getSuggestionsFromRedis(keyword, prefix, limit));
    }
}
