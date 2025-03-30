package com.example.lucky7.domain.store.dto.response;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "StoreResponseDto (맛집 조회 응답 DTO)")
public record StoreGisResponse(Long id, String name,
                               StoreCategory category, String address,
                               double storeLon, double storeLat) {

    public static StoreGisResponse from(Store store) {
        return new StoreGisResponse(
                store.getId(),
                store.getName(),
                store.getCategory(),
                store.getAddress(),
                store.getLongitude(),
                store.getLatitude()
        );
    }
}