package com.example.lucky7.domain.search_v2.search_redis.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RedisSearchResponse {
    Long id;
    String keyword;
    Long count;

    @QueryProjection
    public RedisSearchResponse(Long id, String keyword, Long count) {
        this.id = id;
        this.keyword = keyword;
        this.count = count;
    }
}
