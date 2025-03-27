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

    // 검색어 추가
    @PostMapping
    public ResponseEntity<AutoCompleteResponse> getSuggestions(@RequestParam String prefix, @RequestParam int limit) {
        return ResponseEntity.ok(autoCompleteService.getAutoCompleteSuggestions(prefix, limit));
    }

    // 자동 완섬 검색어 조회
    @GetMapping
    public ResponseEntity<Void> addWord(@RequestParam String word) {
        autoCompleteService.addWord(word);
        return ResponseEntity.ok().build();
    }
}
