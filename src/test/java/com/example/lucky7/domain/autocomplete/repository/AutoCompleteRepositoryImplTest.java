package com.example.lucky7.domain.autocomplete.repository;

import com.example.lucky7.domain.search.entity.Search;
import com.example.lucky7.domain.search.repository.SearchRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AutoCompleteRepositoryImpl.class)
class AutoCompleteRepositoryImplTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AutoCompleteRepositoryImpl autoCompleteRepository;

    @Autowired
    private SearchRepository searchRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
            return new JPAQueryFactory(entityManager);
        }
    }

    @BeforeEach
    void setUp() {
        // 테스트 데이터 삽입
        searchRepository.save(new Search("apple", 10L));
        searchRepository.save(new Search("apricot", 5L));
        searchRepository.save(new Search("application", 8L));
        searchRepository.save(new Search("banana", 7L));
    }

    @Test
    void prefix로_시작하는_검색어를_limit만큼_조회한다() {
        // When
        List<String> results = autoCompleteRepository.getAutoComplete("ap", 3);

        // Then
        assertThat(results).isNotNull()
                .hasSize(3)
                .containsExactly("apple", "application", "apricot");
    }

    @Test
    void 일치하는_키워드가_없을_경우_빈_리스트를_반환한다() {
        // When
        List<String> results = autoCompleteRepository.getAutoComplete("zz", 3);

        // Then
        assertThat(results).isEmpty();
    }
}