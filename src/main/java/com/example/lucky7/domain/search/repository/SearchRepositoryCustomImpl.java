package com.example.lucky7.domain.search.repository;

import com.example.lucky7.config.RestPage;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.lucky7.domain.store.entity.QStore.store;
import static com.example.lucky7.domain.search.entity.QSearch.search;

@Repository
@AllArgsConstructor
public class SearchRepositoryCustomImpl implements SearchRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Store> findStores(String name, StoreCategory category, Pageable pageable) {
        List<Store> content = jpaQueryFactory
                .selectFrom(store)
                .where(
                        Expressions.booleanTemplate("function('fulltext_match', {0}, {1}) > 0", store.name, name),
                        category != null ? store.category.eq(category) : null
                )
                .orderBy(
                        Expressions.numberTemplate(Double.class,
                                        "function('match_score', {0}, {1})", store.name, name)
                                .desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(store.count())
                .from(store)
                .where(
                        Expressions.booleanTemplate("function('fulltext_match', {0}, {1}) > 0", store.name, name),
                        category != null ? store.category.eq(category) : null
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    @Override
    public Page<String> findTopKeyword(Pageable pageable) {
        List<String> keywords = jpaQueryFactory
                .select(search.keyword)
                .from(search)
                .orderBy(search.count.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(search.count.count())
                .from(search)
                .fetchOne();

        return new PageImpl<>(keywords, pageable, total);
    }


    @Override
    public void increaseKeywordCount(String keyword) {
        long updatedCount = jpaQueryFactory
                .update(search)
                .set(search.count, search.count.add(1))
                .where(search.keyword.eq(keyword))
                .execute();

        if (updatedCount == 0) {
            jpaQueryFactory
                    .insert(search)
                    .columns(search.keyword, search.count)
                    .values(keyword, 1L)
                    .execute();
        }
    }

    @Override
    public void resetAllSearchCounts() {
        jpaQueryFactory
                .update(search)
                .set(search.count, 0L)
                .execute();
    }

    // ---------------------------


    @Override
    public RestPage<Store> findStoresWithCache(String name, StoreCategory category, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (name != null) {
            builder.and(store.name.like("%" + name + "%"));
        }

        if (category != null) {
            builder.and(store.category.eq(category));
        }

        List<Store> stores = jpaQueryFactory
                .selectFrom(store)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .selectFrom(store)
                .where(builder)
                .fetchCount();

        return new RestPage<>(stores, pageable, total);

    }


}
