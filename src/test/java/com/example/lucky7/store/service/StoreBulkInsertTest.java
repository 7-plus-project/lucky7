package com.example.lucky7.store.service;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import com.example.lucky7.domain.store.repository.StoreBulkRepository;
import com.example.lucky7.domain.store.repository.StoreRepository;
import com.example.lucky7.external.kakao.KakaoMapClient;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
@TestConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class StoreBulkInsertTest {

    private static final int TOTAL_STORES = 50_000;
    private static final int BATCH_SIZE = 1000;

    private static final Random random = new Random();

    // 구, 위도, 경도를 담는 클래스
    static class District {
        String name;
        double latitude;
        double longitude;

        District(String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    // 구, 위도, 경도를 매핑한 리스트
    private static final List<District> districts = Arrays.asList(
            new District("서울시 강남구", 37.5186, 127.0476),
            new District("서울시 마포구", 37.5665, 126.9780),
            new District("서울시 종로구", 37.5730, 126.9795),
            new District("서울시 강동구", 37.5300, 127.1200),
            new District("서울시 광진구", 37.5475, 127.0723),
            new District("서울시 서초구", 37.4837, 127.0324),
            new District("서울시 송파구", 37.5145, 127.1060),
            new District("서울시 영등포구", 37.5260, 126.8967),
            new District("서울시 용산구", 37.5384, 126.9653),
            new District("서울시 강서구", 37.5509, 126.8491),
            new District("서울시 구로구", 37.4954, 126.8874),
            new District("서울시 금천구", 37.4566, 126.8959),
            new District("서울시 노원구", 37.6542, 127.0568),
            new District("서울시 도봉구", 37.6688, 127.0471),
            new District("서울시 동대문구", 37.5744, 127.0395),
            new District("서울시 동작구", 37.5121, 126.9395),
            new District("서울시 서대문구", 37.5791, 126.9368),
            new District("서울시 성동구", 37.5636, 127.0375),
            new District("서울시 성북구", 37.6066, 127.0238),
            new District("서울시 양천구", 37.5169, 126.8665),
            new District("서울시 은평구", 37.6026, 126.9291),
            new District("서울시 중구", 37.5640, 126.9975),
            new District("서울시 중랑구", 37.6065, 127.0927),
            new District("서울시 강북구", 37.6396, 127.0257),
            new District("서울시 관악구", 37.4784, 126.9516)
    );


    private static final String[] prefixes = {"사랑", "건강", "행복", "맛나는", "최고", "원조"};
    private static final String[] foodTypes = {"떡볶이", "마라탕", "김밥", "돈까스", "초밥", "치킨", "버거", "국밥", "냉면", "우동"};

    private static final StoreCategory[] categories = StoreCategory.values();

    @Autowired
    private StoreBulkRepository storeBulkRepository;

    @Test
    @Transactional
    @Rollback(false)
    void 대용량_가게_데이터_생성(){

        for(int i = 0; i < TOTAL_STORES; i += BATCH_SIZE){
            List<Store> stores = new ArrayList<>();

            for(int j=0; j< BATCH_SIZE; j++ ){
                String name = generateStoreName();

                District district = getRandomDistrict(); // 랜덤으로 구 선택

                // 구에 맞는 위도, 경도
                double latitude = district.latitude;
                double longitude = district.longitude;

                StoreCategory category = categories[random.nextInt(categories.length)];

                Store store = new Store(name, district.name, category, longitude, latitude);

                stores.add(store);
            }
            storeBulkRepository.bulkInsert(stores);
        }
    }


    public static String generateStoreName(){
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String foodType = foodTypes[random.nextInt(foodTypes.length)];
        int randomNum = random.nextInt(999) + 1;
        return prefix + " " + foodType + " " + randomNum;
    }



    // 랜덤으로 구를 선택하는 메서드
    private static District getRandomDistrict() {
        return districts.get(random.nextInt(districts.size()));
    }


}
