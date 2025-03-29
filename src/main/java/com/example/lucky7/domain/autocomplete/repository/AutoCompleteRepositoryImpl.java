package com.example.lucky7.domain.autocomplete.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.lucky7.domain.search.entity.QSearch.search;

@RequiredArgsConstructor
@Repository
public class AutoCompleteRepositoryImpl implements AutoCompleteRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<String> getAutoComplete(String prefix, int limit) {
        return jpaQueryFactory.select(search.keyword)
                .from(search)
                .where(search.keyword.startsWith(prefix))
                .orderBy(search.keyword.asc()) // 사전 순으로 조회
                .limit(limit)
                .fetch();
    }
}
