package com.example.lucky7.domain.store.dto.request;

import com.example.lucky7.domain.store.enums.StoreCategory;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreCreateRequest {

    private String name;

    private String address;

    private StoreCategory category;

    /* MYSQL 위치 검색 - 팰드 추가 */
    private double storeLon; // 경도

    private double storeLat; // 위도

}
