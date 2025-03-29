package com.example.lucky7.domain.autocomplete.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@RequiredArgsConstructor
@Repository
public class RedisAutoCompleteRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Set<String> getAutoComplete(String keyword, String prefix, int limit) {
        // 범위 설정 : prefix ~ 끝 값 (prefix 로 사작하는 모든 단어) / Lexicographical Range
        Range<String> range = Range.closed(prefix, prefix + "\uFFFF");
        // 제한 설정 : 검색어 개수
        Limit redisLimit = Limit.limit().count(limit);
        // key 포함 호출
        return redisTemplate.opsForZSet().rangeByLex(keyword, range, redisLimit);
    }
}
