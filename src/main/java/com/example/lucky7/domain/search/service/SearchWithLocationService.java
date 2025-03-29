package com.example.lucky7.domain.search.service;

import com.example.lucky7.domain.store.dto.response.StoreGisListResponse;
import com.example.lucky7.domain.store.dto.response.StoreListResponseKakao;
import com.example.lucky7.domain.store.dto.response.StoreResponse;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.repository.StoreRepository;
import com.example.lucky7.external.kakao.KakaoMapClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import org.locationtech.jts.geom.Coordinate;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SearchWithLocationService {

    private final StoreRepository storeRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    private final KakaoMapClient kakaoMapClient;


    // ------------------- GeoHash 사용한 위치 기반 검색  ----------------------------

    /* GeoHash 사용한 위치 기반 가게 검색 */
    public List<StoreResponse> findNearbyGeoHash(double longitude, double latitude, double distance) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();

        String key = "storeLocations";

        org.springframework.data.geo.Point point = new Point(longitude, latitude); // 사용자의 현재 위치
        GeoReference reference = GeoReference.fromCoordinate(point); // 좌표 기반 검색 기준 설정


        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeDistance()
                .includeCoordinates()
                .sortAscending()
                .limit(10);

        Distance radius = new Distance(distance, RedisGeoCommands.DistanceUnit.KILOMETERS); // distance(km) 범위 설정

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOps.search(key, reference, radius, args);

        ObjectMapper objectMapper = new ObjectMapper();

        if (results == null) return Collections.emptyList();

        List<StoreResponse> storeResponses = new ArrayList<>();

        for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results){
            String storeId = result.getContent().getName();

            String storeKey = "store:" + storeId;
            String storeJson = valueOps.get(storeKey);

            if (storeJson != null) {
                try {
                    // JSON을 Store 객체로 변환
                    Store store = objectMapper.readValue(storeJson, Store.class);

                    // StoreResponse DTO로 변환하여 저장
                    StoreResponse storeResponse = StoreResponse.toDto(store);
                    storeResponses.add(storeResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return storeResponses;
    }

    // ------------------- Mysql - GIS 사용한 위치 기반 검색 (예인)  ----------------------------

    /* MYSQL 위치 검색 - 메서드 추가 */
    public StoreGisListResponse findNearByGis(double lon, double lat, double range) {
        // 경도, 위도의 계산을 위해 km를 m로 변환
        double meterRange = range * 1000;
        // 경도, 위도에서 0.01도는 1100m인 것을 사용해 몇 m는 위도, 경도로 어느 정도인지 계산
        double meterToDegree = meterRange * 0.01 / 1100; // 0.01 : 1100 = meterToDegree : meterRange(몇 m)
        // 사용자의 경도, 위도를 Point 데이터 타입으로 변경
        org.locationtech.jts.geom.Point userLocation = geometryFactory.createPoint(new Coordinate(lon, lat));
        userLocation.setSRID(4326); // SRID 4326 (WGS 84 좌표계)로 설정

        List<Store> storeList = storeRepository.findStoresByUserLocationOrderByDistance(userLocation, meterRange);
        return StoreGisListResponse.fromStoreList(storeList);
    }

    // ------------------- Kakao 위치 사용한 위치 기반 검색 (범서) ----------------------------

    public Page<StoreListResponseKakao> searchStoresByDistanceKaKao(String address, int distanceKm, int page, int size) {
        com.example.lucky7.external.kakao.Coordinate userLocation = kakaoMapClient.geocode(address);
        List<Store> allStores = storeRepository.findAll();

        List<StoreListResponseKakao> filtered = allStores.stream()
                .filter(store -> calculateDistance(
                        userLocation.latitude(), userLocation.longitude(),
                        store.getLatitude(), store.getLongitude()
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
