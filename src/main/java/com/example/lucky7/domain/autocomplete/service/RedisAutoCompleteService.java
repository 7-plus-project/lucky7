package com.example.lucky7.domain.autocomplete.service;

import com.example.lucky7.domain.autocomplete.dto.response.AutoCompleteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@RequiredArgsConstructor
@Service
public class RedisAutoCompleteService {

    private final StringRedisTemplate redisTemplate;
    private static final String AUTO_COMPLETE_KEY = "autocomplete";

    // 검색어 추가
    public void addWord(String word) {
        redisTemplate.opsForZSet().add(AUTO_COMPLETE_KEY, word, System.currentTimeMillis());
    }

    // 특정 prefix로 시작하는 자동완성 검색어 조회
//    public AutoCompleteResponse getAutoCompleteSuggestions(String prefix, int limit) {
//
//        redisTemplate.
//        HashSet<String> results = new HashSet<>();
//
//        return new AutoCompleteResponse(results);
//    }
}
