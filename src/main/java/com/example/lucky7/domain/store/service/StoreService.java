package com.example.lucky7.domain.store.service;

import com.example.lucky7.domain.common.exception.InvalidRequestException;
import com.example.lucky7.domain.store.dto.request.StoreCreateRequest;
import com.example.lucky7.domain.store.dto.request.StoreUpdateRequest;
import com.example.lucky7.domain.store.dto.response.StoreListResponse;
import com.example.lucky7.domain.store.dto.response.StoreLocationListResponse;
import com.example.lucky7.domain.store.dto.response.StoreResponse;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public StoreResponse save(StoreCreateRequest request) {

        Store store = new Store(
                request.getName(),
                request.getAddress(),
                request.getCategory()
        );

        Store savedStore = storeRepository.save(store);

        return StoreResponse.toDto(savedStore);

    }

    public Page<StoreListResponse> findStores(int page, int size, LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Store> stores = storeRepository.findAllByOrderByModifiedAtDesc(pageable, startDate, endDate);

        return stores.map(store -> new StoreListResponse(
                store.getId(),
                store.getName()
        ));
    }

    public StoreResponse findStore(Long storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new InvalidRequestException("해당 id의 가게가 존재하지 않습니다."));

        return StoreResponse.toDto(store);

    }

    @Transactional
    public StoreResponse update(Long storeId, StoreUpdateRequest request) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new InvalidRequestException("해당 id의 가게가 존재하지 않습니다."));

        store.updateStore(request);

        return StoreResponse.toDto(store);

    }

    @Transactional
    public void delete(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new InvalidRequestException("해당 id의 가게가 존재하지 않습니다."));

        store.deleteStore(LocalDateTime.now());
    }

    /* MYSQL 위치 검색 - 메서드 추가 */
    public StoreLocationListResponse findStoresLocation(double lon, double lat, double range) {
        // 경도, 위도의 계산을 위해 km를 m로 변환
        double meterRange = range * 1000;
        // 경도, 위도에서 0.01도는 1100m인 것을 사용해 몇 m는 위도, 경도로 어느 정도인지 계산
        double meterToDegree = meterRange * 0.01 / 1100; // 0.01 : 1100 = meterToDegree : meterRange(몇 m)
        // 사용자의 경도, 위도를 Point 데이터 타입으로 변경
        Point userLocation = geometryFactory.createPoint(new Coordinate(lon, lat));
        userLocation.setSRID(4326); // SRID 4326 (WGS 84 좌표계)로 설정

        List<Store> storeList = storeRepository.findStoresByUserLocationOrderByDistance(userLocation, meterRange);
        return StoreLocationListResponse.fromStoreList(storeList);
    }
}
