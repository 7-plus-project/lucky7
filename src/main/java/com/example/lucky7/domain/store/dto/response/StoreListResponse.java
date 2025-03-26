package com.example.lucky7.domain.store.dto.response;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreListResponse {

    private final Long id;
    private final String name;
    private final String address;
    private final StoreCategory category;

    /* 범서
    public StoreListResponse(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.category = store.getCategory();
    }
    */
}
