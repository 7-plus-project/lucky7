//package com.example.lucky7.domain.autocomplete.service;
//
//import com.example.lucky7.domain.autocomplete.dto.response.AutoCompleteResponse;
//import com.example.lucky7.domain.autocomplete.repository.AutoCompleteRepository;
//import com.example.lucky7.domain.autocomplete.repository.RedisAutoCompleteRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ZSetOperations;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.util.List;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class RedisAutoCompleteServiceTest {
//
//    @Mock
//    private AutoCompleteRepository autoCompleteRepository;
//
//    @Mock
//    private RedisAutoCompleteRepository redisAutoCompleteRepository;
//
//    @Mock
//    private RedisTemplate<String, String> redisTemplate;
//
//    @Mock
//    private ZSetOperations<String, String> zSetOperations;
//
//    @InjectMocks
//    private RedisAutoCompleteService redisAutoCompleteService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
////        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        doReturn(zSetOperations).when(redisTemplate).opsForZSet();
//    }
//
//    @Test
//    void keyword_저장시_Redis에_키워드가_1회_저장된다() {
//        // Given
//        String keyword = "test";
//        String prefix = "pre";
//        Long count = 10L;
//
//        // When
//        redisAutoCompleteService.saveKeyword(keyword, prefix, count);
//
//        // Then
//        verify(zSetOperations, times(1)).add(prefix, keyword, count.doubleValue());
//    }
//
//    @Test
//    void 자동완성기능_사용시_Redis에_데이터가_있으면_Redis_데이터를_조회한다() {
//        // Given & When
//        String keyword = "te";
//        String prefix = "pre";
//        int limit = 5;
//        Set<String> redisResults = Set.of("test1", "test2");
//
//        when(redisAutoCompleteRepository.getAutoComplete(keyword, prefix, limit)).thenReturn(redisResults);
//
//        AutoCompleteResponse response = redisAutoCompleteService.getSuggestionsFromRedis(keyword, prefix, limit);
//
//        // Then
//        assertThat(response.suggestions()).isNotNull()
//                .containsExactlyInAnyOrder("test1", "test2");
//        verify(autoCompleteRepository, never()).getAutoComplete(anyString(), anyInt());
//    }
//
//    @Test
//    void 자동완성기능_사용시_Redis에_데이터가_없으면_localDB_데이터를_조회한다() {
//        // Given & When
//        String keyword = "te";
//        String prefix = "pre";
//        int limit = 5;
//        Set<String> redisResults = Set.of();
//        List<String> dbResults = List.of("dbTest1", "dbTest2");
//        when(redisAutoCompleteRepository.getAutoComplete(keyword, prefix, limit)).thenReturn(redisResults);
//        when(autoCompleteRepository.getAutoComplete(prefix, limit)).thenReturn(dbResults);
//
//        AutoCompleteResponse response = redisAutoCompleteService.getSuggestionsFromRedis(keyword, prefix, limit);
//
//        // Then
//        assertThat(response.suggestions()).isNotNull()
//                .containsExactlyInAnyOrder("dbTest1", "dbTest2");
//        verify(zSetOperations, times(2)).add(eq(prefix), anyString(), eq(1.0));
//    }
//}