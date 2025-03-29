package com.example.lucky7.domain.store.service;

import com.example.lucky7.domain.common.exception.InvalidRequestException;
import com.example.lucky7.domain.store.dto.request.StoreCreateRequest;
import com.example.lucky7.domain.store.dto.request.StoreCreateRequestKakao;
import com.example.lucky7.domain.store.dto.request.StoreUpdateRequest;
import com.example.lucky7.domain.store.dto.response.StoreListResponse;
import com.example.lucky7.domain.store.dto.response.StoreResponse;
import com.example.lucky7.domain.store.dto.response.StoreResponseKakao;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.repository.StoreRepository;
import com.example.lucky7.external.kakao.KakaoMapClient;
import com.example.lucky7.external.kakao.Coordinate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.*;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    private final RedisTemplate<String, String> redisTemplate;


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




    /* 위도,경도, GeoHash, Location 포함한 가게 생성 (저장) */
    @Transactional
    public StoreResponse saveWithGeoHash(StoreCreateRequest request){

        Store store = new Store(
                request.getName(),
                request.getAddress(),
                request.getCategory(),
                request.getLongitude(),
                request.getLatitude()
        );

        Store savedStore = storeRepository.save(store);
        log.info("저장된 가게 아이디 : "+savedStore.getId().toString());

        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        String redisKey = "storeLocations"; // Geo 데이터 저장 키
        String storeKey = "store:" + savedStore.getId(); // 개별 가게 정보 저장 키

        // 가게의 위치 정보 저장 (Geo)
        geoOps.add(redisKey, new Point(savedStore.getLongitude(), savedStore.getLatitude()), String.valueOf(savedStore.getId()));

        // json 형태로 가게 정보 저장
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);


        try {

            Map<String, Object> storeMap = new HashMap<>();
            storeMap.put("id", String.valueOf(savedStore.getId()));
            storeMap.put("name", savedStore.getName());
            storeMap.put("address", savedStore.getAddress());
            storeMap.put("category", savedStore.getCategory());
            storeMap.put("latitude", savedStore.getLatitude());
            storeMap.put("longitude", savedStore.getLongitude());

            String storeJson = objectMapper.writeValueAsString(storeMap);

            valueOps.set(storeKey, storeJson);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return StoreResponse.toDto(savedStore);
    }

//     ------------------- kakao API ----------------------------

    private final KakaoMapClient kakaoMapClient;

    @Transactional
    public StoreResponseKakao saveKakao(StoreCreateRequestKakao request) {
        Coordinate coord = kakaoMapClient.geocode(request.getAddress());

        Store store = Store.fromKakao(
                request.getName(),
                request.getAddress(),
                request.getCategory(),
                coord.latitude(),
                coord.longitude()
        );

        Store savedStore = storeRepository.save(store);
        return StoreResponseKakao.toDto(savedStore);
    }



}