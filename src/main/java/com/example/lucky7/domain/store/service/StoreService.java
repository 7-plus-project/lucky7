package com.example.lucky7.domain.store.service;

import com.example.lucky7.domain.common.exception.InvalidRequestException;
import com.example.lucky7.domain.store.dto.request.StoreCreateRequest;
import com.example.lucky7.domain.store.dto.request.StoreCreateRequestKakao;
import com.example.lucky7.domain.store.dto.request.StoreUpdateRequest;
import com.example.lucky7.domain.store.dto.response.StoreListResponse;
//import com.example.lucky7.domain.store.dto.response.StoreGisListResponse;
import com.example.lucky7.domain.store.dto.response.StoreListResponseKakao;
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

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    private final RedisTemplate<String, String> redisTemplate;

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


//    // ------------------- GeoHash 사용한 위치 기반 검색 시작 ----------------------------
//
//    /* GeoHash 사용한 가게 생성 (저장) */
//    @Transactional
//    public StoreResponse saveWithGeoHash(StoreCreateRequest request){
//
//        Store store = new Store(
//                request.getName(),
//                request.getAddress(),
//                request.getCategory(),
//                request.getLatitude(),
//                request.getLongitude()
//        );
//
//        Store savedStore = storeRepository.save(store);
//        log.info("저장된 가게 아이디 : "+savedStore.getId().toString());
//
//        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
//        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
//
//        String redisKey = "storeLocations"; // Geo 데이터 저장 키
//        String storeKey = "store:" + savedStore.getId(); // 개별 가게 정보 저장 키
//
//        // 가게의 위치 정보 저장 (Geo)
//        geoOps.add(redisKey, new Point(savedStore.getLongitude(), savedStore.getLatitude()), String.valueOf(savedStore.getId()));
//
//        // json 형태로 가게 정보 저장
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//
//
//        try {
//
//            Map<String, Object> storeMap = new HashMap<>();
//            storeMap.put("id", String.valueOf(savedStore.getId()));
//            storeMap.put("name", savedStore.getName());
//            storeMap.put("address", savedStore.getAddress());
//            storeMap.put("category", savedStore.getCategory());
//            storeMap.put("latitude", savedStore.getLatitude());
//            storeMap.put("longitude", savedStore.getLongitude());
//
//            String storeJson = objectMapper.writeValueAsString(storeMap);
//
//            valueOps.set(storeKey, storeJson);
//
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        return StoreResponse.toDto(savedStore);
//    }
//
//
//    /* GeoHash 사용한 위치 기반 가게 검색 */
//    public List<StoreResponse> findNearbyGeoHash(double longitude, double latitude, double distance) {
//        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
//        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
//
//        String key = "storeLocations";
//
//        Point point = new Point(longitude, latitude); // 사용자의 현재 위치
//        GeoReference reference = GeoReference.fromCoordinate(point); // 좌표 기반 검색 기준 설정
//
//
//        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
//                .newGeoRadiusArgs()
//                .includeDistance()
//                .includeCoordinates()
//                .sortAscending()
//                .limit(10);
//
//        Distance radius = new Distance(distance, RedisGeoCommands.DistanceUnit.KILOMETERS); // distance(km) 범위 설정
//
//        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOps.search(key, reference, radius, args);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        if (results == null) return Collections.emptyList();
//
//        List<StoreResponse> storeResponses = new ArrayList<>();
//
//        for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results){
//            String storeId = result.getContent().getName();
//
//            String storeKey = "store:" + storeId;
//            String storeJson = valueOps.get(storeKey);
//
//            if (storeJson != null) {
//                try {
//                    // JSON을 Store 객체로 변환
//                    Store store = objectMapper.readValue(storeJson, Store.class);
//
//                    // StoreResponse DTO로 변환하여 저장
//                    StoreResponse storeResponse = StoreResponse.toDto(store);
//                    storeResponses.add(storeResponse);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//
//        return storeResponses;
//    }
//
//    // ------------------- GeoHash 사용한 위치 기반 검색 끝 ----------------------------


//    /* MYSQL 위치 검색 - 메서드 추가 */
//    public StoreGisListResponse findNearByGis(double lon, double lat, double range) {
//        // 경도, 위도의 계산을 위해 km를 m로 변환
//        double meterRange = range * 1000;
//        // 경도, 위도에서 0.01도는 1100m인 것을 사용해 몇 m는 위도, 경도로 어느 정도인지 계산
//        double meterToDegree = meterRange * 0.01 / 1100; // 0.01 : 1100 = meterToDegree : meterRange(몇 m)
//        // 사용자의 경도, 위도를 Point 데이터 타입으로 변경
//        Point userLocation = geometryFactory.createPoint(new Coordinate(lon, lat));
//        userLocation.setSRID(4326); // SRID 4326 (WGS 84 좌표계)로 설정
//
//        List<Store> storeList = storeRepository.findStoresByUserLocationOrderByDistance(userLocation, meterRange);
//        return StoreGisListResponse.fromStoreList(storeList);
//    }

    // ------------------- kakao API ----------------------------
//    private final KakaoMapClient kakaoMapClient;
//
//    @Transactional
//    public StoreResponseKakao saveKakao(StoreCreateRequestKakao request) {
//        Coordinate coord = kakaoMapClient.geocode(request.getAddress());
//
//        Store store = Store.fromKakao(
//                request.getName(),
//                request.getAddress(),
//                request.getCategory(),
//                coord.latitude(),
//                coord.longitude()
//        );
//
//        Store savedStore = storeRepository.save(store);
//        return StoreResponseKakao.toDto(savedStore);
//    }
//
//
//    public Page<StoreListResponseKakao> searchStoresByDistanceKaKao(String address, int distanceKm, int page, int size) {
//        Coordinate userLocation = kakaoMapClient.geocode(address);
//        List<Store> allStores = storeRepository.findAll();
//
//        List<StoreListResponseKakao> filtered = allStores.stream()
//                .filter(store -> calculateDistance(
//                        userLocation.latitude(), userLocation.longitude(),
//                        store.getLatitudeKakao(), store.getLongitudeKakao()
//                ) <= distanceKm)
//                .map(StoreListResponseKakao::from)
//                .toList();
//
//
//        int fromIndex = Math.min((page - 1) * size, filtered.size());
//        int toIndex = Math.min(fromIndex + size, filtered.size());
//        List<StoreListResponseKakao> pageContent = filtered.subList(fromIndex, toIndex);
//
//        return new PageImpl<>(pageContent, PageRequest.of(page - 1, size), filtered.size());
//    }
//
//
//    // 사용 거리 계산
//    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
//        final int EARTH_RADIUS = 6371; // km
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLng = Math.toRadians(lng2 - lng1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        return EARTH_RADIUS * c;
//    }

        Store savedStore = storeRepository.save(store);
        return StoreResponseKakao.toDto(savedStore);
    }


    public Page<StoreListResponseKakao> searchStoresByDistanceKaKao(String address, int distanceKm, int page, int size) {
        Coordinate userLocation = kakaoMapClient.geocode(address);
        List<Store> allStores = storeRepository.findAll();

        List<StoreListResponseKakao> filtered = allStores.stream()
                .filter(store -> calculateDistance(
                        userLocation.latitude(), userLocation.longitude(),
                        store.getLatitudeKakao(), store.getLongitudeKakao()
                ) <= distanceKm)
                .map(StoreListResponseKakao::from)
                .toList();


        int fromIndex = Math.min((page - 1) * size, filtered.size());
        int toIndex = Math.min(fromIndex + size, filtered.size());
        List<StoreListResponseKakao> pageContent = filtered.subList(fromIndex, toIndex);

        return new PageImpl<>(pageContent, PageRequest.of(page - 1, size), filtered.size());
    }


    // 사용 거리 계산
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
