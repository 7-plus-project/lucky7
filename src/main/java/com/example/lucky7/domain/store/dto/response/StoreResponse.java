package com.example.lucky7.domain.store.dto.response;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class StoreResponse {
    private Long id;
    private String name;
    private String address;
    private StoreCategory category;

    public static StoreResponse toDto(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getCategory()
        );

    }

    @QueryProjection
    public StoreResponse(Long id, String name, String address, StoreCategory category) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.category = category;
    }
}
