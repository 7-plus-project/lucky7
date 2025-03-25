package com.example.lucky7.domain.store.dto.response;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
}
