package com.example.lucky7.store.service;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import com.example.lucky7.domain.store.repository.StoreBulkRepository;
import com.example.lucky7.domain.store.repository.StoreRepository;
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

    private static final String[] districts = {
            "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구",
            "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구",
            "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"
    };

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
                String address = generateAddress();
                StoreCategory category = categories[random.nextInt(categories.length)];

                Store store = new Store(name, address, category);

                stores.add(store);
            }
            storeBulkRepository.bulkInsert(stores);
        }
    }

    public static String generateAddress(){
        String district = districts[random.nextInt(districts.length)];
        return "서울시 "+ district;

    }

    public static String generateStoreName(){
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String foodType = foodTypes[random.nextInt(foodTypes.length)];
        int randomNum = random.nextInt(999) + 1;
        return prefix + " " + foodType + " " + randomNum;
    }


}
