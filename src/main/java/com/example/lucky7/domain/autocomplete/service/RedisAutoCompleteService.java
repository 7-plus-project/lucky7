package com.example.lucky7.domain.autocomplete.service;

import com.example.lucky7.domain.autocomplete.repository.AutoCompleteRepository;
import com.example.lucky7.domain.autocomplete.repository.RedisAutoCompleteRepository;
import com.example.lucky7.domain.autocomplete.dto.response.AutoCompleteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RedisAutoCompleteService {

    private final AutoCompleteRepository autoCompleteRepository;
    private final RedisAutoCompleteRepository redisAutoCompleteRepository;
    private final RedisTemplate<String, String> redisTemplate;

    // 검색어 저장 Redis
    @Transactional
    public void saveKeyword(String keyword, String prefix, Long count) {
        redisTemplate.opsForZSet().add(prefix, keyword, count.doubleValue()); // Redis 캐싱
    }

    // 자동완성 추천 검색어 조회 (Redis → DB 캐싱)
    public AutoCompleteResponse getSuggestionsFromRedis(String keyword, String prefix, int limit) {
        Set<String> redisResults = redisAutoCompleteRepository.getAutoComplete(keyword, prefix, limit);
        // Redis 데이터가 없을 경우
        if (!redisResults.isEmpty()) {
            return new AutoCompleteResponse(redisResults);
        }
        // Redis 값이 없으면 DB 조회 후 DB 데이터 Redis 캐싱
        List<String> dbResults = autoCompleteRepository.getAutoComplete(prefix, limit);
        dbResults.forEach(word -> redisTemplate.opsForZSet().add(prefix, word, 1.0));
        return new AutoCompleteResponse(Set.copyOf(dbResults));
    }
}
