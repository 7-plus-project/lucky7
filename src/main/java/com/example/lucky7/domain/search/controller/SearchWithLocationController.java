package com.example.lucky7.domain.search.controller;

import com.example.lucky7.domain.store.dto.response.StoreGisListResponse;
import com.example.lucky7.domain.search.service.SearchWithLocationService;
import com.example.lucky7.domain.store.dto.response.StoreListResponseKakao;
import com.example.lucky7.domain.store.dto.response.StoreResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchWithLocationController {

    private final SearchWithLocationService searchWithLocationService;


    // ------------------- GeoHash 사용한 위치 기반 검색 (민정) ----------------------------

    @GetMapping("/geohash")
    public ResponseEntity<List<StoreResponse>> getNearByGeoHash(
            @RequestParam String address,
            @RequestParam(value = "distance") double distance) {
        List<StoreResponse> nearbyStores = searchWithLocationService.findNearbyGeoHash(address, distance);
        return ResponseEntity.ok(nearbyStores);
    }



    // ------------------- Mysql - GIS 사용한 위치 기반 검색 (예인)  ----------------------------

    @GetMapping("/gis")
    public ResponseEntity<StoreGisListResponse> getNearByGis(
            @RequestParam String address,
            @RequestParam(value = "range") double range) {
        StoreGisListResponse storeList = searchWithLocationService.findNearByGis(address, range);
        if (storeList.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(storeList);
    }

    // ------------------- Kakao 위치 사용한 위치 기반 검색 (범서) ----------------------------


    @GetMapping("/kakao")
    public ResponseEntity<Page<StoreListResponseKakao>> searchStoresKakao(
            @RequestParam String address,
            @RequestParam int distance,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(searchWithLocationService.searchStoresByDistanceKaKao(address, distance, page, size));
    }

}
