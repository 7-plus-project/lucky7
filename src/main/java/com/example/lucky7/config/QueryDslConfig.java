package com.example.lucky7.config;

import jakarta.persistence.EntityManager; // Jakarta 임포트 확인
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;  // Jakarta EntityManager

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);  // 생성자 호환성 해결
    }
}
