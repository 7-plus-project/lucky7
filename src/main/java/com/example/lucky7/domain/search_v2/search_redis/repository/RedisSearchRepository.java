package com.example.lucky7.domain.search_v2.search_redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class RedisSearchRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // keyword 검색 시 Redis 에 캐싱
    public void incrementSearchCount(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        // sorted set - ZINCRBY
        // 1.0 만큼 count 증가 - 값이 없는 경우 1.0 으로 설정
        redisTemplate.opsForZSet().incrementScore("search", lowerCaseKeyword, 1.0);
        // sorted set - ZADD
        // 자동완성을 위해 저장 : 모든 score 를 같게 저장
        redisTemplate.opsForZSet().add("autocomplete", lowerCaseKeyword, 0);
    }

    // 인기 검색어 limit 개수만큼 조회
    public List<String> getPopularSearches(int limit) {
        // sorted set - ZREVRANGE
        Set<String> results = redisTemplate.opsForZSet().reverseRange("search", 0, limit - 1);
        return new ArrayList<>(results);
    }

    // 가게 검색시 prefix 값으로 시작하는 자동완성 값 limit 조회 조회
    public Set<String> getAutoComplete(String prefix, int limit) {
        // 입력된 prefix 를 소문자로 변환
        String lowerCasePrefix = prefix.toLowerCase();
        // 범위 설정 : prefix ~ 끝 값 (prefix 로 사작하는 모든 단어) / Lexicographical Range
        Range<String> range = Range.closed(lowerCasePrefix, lowerCasePrefix + "\uFFFF");
        // 제한 설정 : 검색어 개수
        Limit redisLimit = Limit.limit().count(limit);
        // key 포함 호출
        return redisTemplate.opsForZSet().rangeByLex("autocomplete", range, redisLimit);
    }
}
