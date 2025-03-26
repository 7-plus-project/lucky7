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

    // Mysql 위치기반 request 필드 추가 부분 제거(추후 KAKAO MAP API 사용 예정)
//    private Double latitude;
//    private Double longitude;

}
