package com.example.lucky7.domain.store.service;

import com.example.lucky7.domain.common.exception.InvalidRequestException;
import com.example.lucky7.domain.store.dto.request.StoreCreateRequest;
import com.example.lucky7.domain.store.dto.request.StoreUpdateRequest;
import com.example.lucky7.domain.store.dto.response.StoreListResponse;
import com.example.lucky7.domain.store.dto.response.StoreResponse;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.repository.StoreRepository;
import com.example.lucky7.external.kakao.Coordinate;
import com.example.lucky7.external.kakao.KakaoMapClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final KakaoMapClient kakaoMapClient;

    @Transactional
    public StoreResponse save(StoreCreateRequest request) {
        Coordinate coord = kakaoMapClient.geocode(request.getAddress());

        Store store = new Store(
                request.getName(),
                request.getAddress(),
                request.getCategory(),
                coord.latitude(),
                coord.longitude()
        );

        Store savedStore = storeRepository.save(store);

        return StoreResponse.toDto(savedStore);

    }
    public Page<StoreListResponse> searchStoresByDistance(String address, int distanceKm, int page, int size) {
        Coordinate userLocation = kakaoMapClient.geocode(address);

        List<Store> allStores = storeRepository.findAll();

        List<StoreListResponse> filtered = allStores.stream()
                .filter(store -> calculateDistance(
                        userLocation.latitude(), userLocation.longitude(),
                        store.getLatitude(), store.getLongitude()
                ) <= distanceKm)
                .map(StoreListResponse::from)
                .toList();

        int fromIndex = Math.min((page - 1) * size, filtered.size());
        int toIndex = Math.min(fromIndex + size, filtered.size());
        List<StoreListResponse> pageContent = filtered.subList(fromIndex, toIndex);

        return new PageImpl<>(pageContent, PageRequest.of(page - 1, size), filtered.size());
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

    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int EARTH_RADIUS = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
