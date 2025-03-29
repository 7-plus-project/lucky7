package com.example.lucky7.domain.autocomplete.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RedisAutoCompleteRepositoryTest {

    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisAutoCompleteRepository redisAutoCompleteRepository;

    private ZSetOperations<String, String> zSetOperations;

    private static final String KEYWORD = "autocomplete";

    @BeforeEach
    void setUp() {
        zSetOperations = redisTemplate.opsForZSet();
        // Redis 초기화 (flushDB)
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
    }

    @Test
    void prefix_값으로_시작하는_사전순_결과가_limit값만큼_나온다() {
        // Given: Redis에 자동완성 데이터를 저장
        zSetOperations.add(KEYWORD, "apple", 1);
        zSetOperations.add(KEYWORD, "apricot", 1);
        zSetOperations.add(KEYWORD, "banana", 1);
        zSetOperations.add(KEYWORD, "application", 1);
        zSetOperations.add(KEYWORD, "apartment", 1);

        // When: "ap"로 시작하는 검색어 조회
        Set<String> results = redisAutoCompleteRepository.getAutoComplete(KEYWORD, "ap", 3);

        // Then: "ap"로 시작하는 단어 3개가 반환되어야 함 (사전순 정렬)
        assertThat(results).isNotNull()
                .hasSize(3)
                .containsExactly("apartment", "apple", "application");
    }

    @Test
    void prefix에_해당하는_redis_데이터가_없을_경우_빈값을_리턴한다() {
        // Given: Redis에 저장된 검색어가 없을 경우
        zSetOperations.add(KEYWORD, "banana", 1);
        zSetOperations.add(KEYWORD, "cherry", 1);

        // When: "ap"로 시작하는 검색어 조회
        Set<String> results = redisAutoCompleteRepository.getAutoComplete(KEYWORD, "ap", 5);

        // Then: 결과가 비어 있어야 함
        assertThat(results).isEmpty();
    }
}