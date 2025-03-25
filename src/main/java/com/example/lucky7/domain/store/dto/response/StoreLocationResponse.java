package com.example.lucky7.domain.store.dto.response;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "StoreResponseDto (맛집 조회 응답 DTO)")
public record StoreLocationResponse(Long id, String name,
                                StoreCategory category, String address,
                                double storeLon, double storeLat) {

    public static StoreLocationResponse from(Store store) {
        return new StoreLocationResponse(
                store.getId(),
                store.getName(),
                store.getCategory(),
                store.getAddress(),
                store.getStoreLon(),
                store.getStoreLat()
        );
    }
}