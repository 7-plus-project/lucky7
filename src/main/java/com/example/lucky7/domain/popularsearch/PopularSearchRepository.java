package com.example.lucky7.domain.popularsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class PopularSearchRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // keyword 검색 시 Redis 에 캐싱
    public void incrementSearchCount(String keyword) {
        // sorted set - ZINCRBY
        // 1.0 만큼 count 증가 - 값이 없는 경우 1.0 으로 설정
        redisTemplate.opsForZSet().incrementScore(keyword, keyword, 1.0);
    }

    // 인기 검색어 limit 개수만큼 조회
    public List<String> getPopularSearches(String keyword, int limit) {
        // sorted set - ZREVRANGE
        Set<String> results = redisTemplate.opsForZSet().reverseRange(keyword, 0, limit - 1);
        return new ArrayList<>(results);
    }
}
