package com.example.lucky7.domain.search.repository;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.lucky7.domain.store.entity.QStore.store;

@Repository
@AllArgsConstructor
public class SearchRepositoryCustomImpl implements SearchRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Store> findStoresByQuerydsl(String name, StoreCategory category, Pageable pageable) {
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

        return new PageImpl<>(stores, pageable, total);
    }
}
