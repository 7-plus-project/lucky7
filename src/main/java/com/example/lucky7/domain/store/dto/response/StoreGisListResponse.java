//package com.example.lucky7.domain.store.dto.response;
//
//import com.example.lucky7.domain.store.entity.Store;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import io.swagger.v3.oas.annotations.media.Schema;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Schema(title = "StoreGisListResponse (맛집 목록 응답 DTO)")
//public record StoreGisListResponse(@Schema(description = "조회된 맛집 개수") int totalCount,
//                                   @Schema(description = "조회된 맛집 목록") List<StoreGisResponse> stores) {
//
//    public static StoreGisListResponse fromStoreList(List<Store> storeList) {
//        List<StoreGisResponse> storeGisRespons = storeList.stream()
//                .map(StoreGisResponse::from)
//                .collect(Collectors.toList());
//        return new StoreGisListResponse(storeList.size(), storeGisRespons);
//    }
//
//    // record 타입에 메서드를 추가했을 때 Jackson 라이브러리가 record의 모든 메서드를 직렬화 대상 필드로 간주하기 때문에
//    @JsonIgnore // 직렬화 과정에서 무시하기위해 사용함
//    public boolean isEmpty() {
//        return stores.isEmpty();
//    }
//}
