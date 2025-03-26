package com.example.lucky7.domain.store.controller;

import com.example.lucky7.domain.common.dto.AuthUser;
import com.example.lucky7.domain.store.dto.request.StoreCreateRequest;
import com.example.lucky7.domain.store.dto.request.StoreUpdateRequest;
import com.example.lucky7.domain.store.dto.response.StoreListResponse;
import com.example.lucky7.domain.store.dto.response.StoreResponse;
import com.example.lucky7.domain.store.service.StoreService;
import com.example.lucky7.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {

    private final StoreService storeService;

    @Secured(UserRole.Authority.ADMIN)
    @PostMapping
    public ResponseEntity<StoreResponse> save(@RequestBody StoreCreateRequest request){
        return ResponseEntity.ok(storeService.save(request));
    }

    @GetMapping
    public ResponseEntity<Page<StoreListResponse>> getStores(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
            // todo 검색 키워드 추가하기
    ){
        return ResponseEntity.ok(storeService.findStores(page,size, startDate, endDate));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponse> getStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.findStore(storeId));
    }

    @Secured(UserRole.Authority.ADMIN)
    @PatchMapping("/{storeId}")
    public ResponseEntity<StoreResponse> updateStore(
            @PathVariable Long storeId,
            @RequestBody StoreUpdateRequest request) {
        return ResponseEntity.ok(storeService.update(storeId, request));
    }

    @Secured(UserRole.Authority.ADMIN)
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId) {
        storeService.delete(storeId);
        return ResponseEntity.noContent().build();
    }

    // ------------------- GeoHash 사용한 위치 기반 검색 시작 ----------------------------

    @PostMapping("/geohash")
    public ResponseEntity<StoreResponse> saveWithGeoHash(@RequestBody StoreCreateRequest request) {
        return ResponseEntity.ok(storeService.saveWithGeoHash(request));
    }

    @GetMapping("/geohash")
    public ResponseEntity<List<StoreResponse>> getNearByGeoHash(
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "distance") double distance) {
        List<StoreResponse> nearbyStores = storeService.findNearbyGeoHash(longitude, latitude, distance);
        return ResponseEntity.ok(nearbyStores);
    }

    // ------------------- GeoHash 사용한 위치 기반 검색 끝 ----------------------------

}
