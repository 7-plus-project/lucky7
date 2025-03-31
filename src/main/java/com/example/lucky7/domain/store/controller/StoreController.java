package com.example.lucky7.domain.store.controller;

import com.example.lucky7.domain.store.dto.request.StoreCreateRequest;
import com.example.lucky7.domain.store.dto.request.StoreCreateRequestLocation;
import com.example.lucky7.domain.store.dto.request.StoreUpdateRequest;
import com.example.lucky7.domain.store.dto.response.StoreListResponse;
import com.example.lucky7.domain.store.dto.response.StoreResponse;
import com.example.lucky7.domain.store.dto.response.StoreResponseLocation;
import com.example.lucky7.domain.store.service.StoreService;
import com.example.lucky7.domain.user.enums.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
@Tag(name = "식당 CRUD API")
public class StoreController {

    private final StoreService storeService;

    @Secured(UserRole.Authority.ADMIN)
    @PostMapping
    @Operation(summary = "식당 생성", description = "식당 생성 기능입니다. 관리자만 접근 가능합니다.")
    public ResponseEntity<StoreResponse> save(@RequestBody StoreCreateRequest request){
        return ResponseEntity.ok(storeService.save(request));
    }

    @GetMapping
    @Operation(summary = "식당 전체 조회", description = "식당 전체 조회 기능입니다.")
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
    @Operation(summary = "식당 단건 조회", description = "식당 단건 조회 기능입니다.")
    public ResponseEntity<StoreResponse> getStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.findStore(storeId));
    }

    @Secured(UserRole.Authority.ADMIN)
    @PatchMapping("/{storeId}")
    @Operation(summary = "식당 정보 수정", description = "식당 정보 수정 기능입니다. 관리자만 접근 가능합니다.")
    public ResponseEntity<StoreResponse> updateStore(
            @PathVariable Long storeId,
            @RequestBody StoreUpdateRequest request) {
        return ResponseEntity.ok(storeService.update(storeId, request));
    }

    @Secured(UserRole.Authority.ADMIN)
    @DeleteMapping("/{storeId}")
    @Operation(summary = "식당 삭제", description = "식당 삭제 기능입니다. 관리자만 접근 가능합니다.")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId) {
        storeService.delete(storeId);
        return ResponseEntity.noContent().build();
    }

    @Secured(UserRole.Authority.ADMIN)
    @PostMapping("/save-location")
    @Operation(summary = "위치를 포함한 식당 생성 API", description = "입력된 주소를 위도, 경도, GeoHash, Location로 자동 변환하여 식당을 생성합니다.")
    public ResponseEntity<StoreResponseLocation> saveWithLocation(@RequestBody StoreCreateRequestLocation request) {
        return ResponseEntity.ok(storeService.saveWithLocation(request));
    }



}
