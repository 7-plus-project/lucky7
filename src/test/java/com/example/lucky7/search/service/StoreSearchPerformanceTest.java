package com.example.lucky7.search.service;

import com.example.lucky7.domain.search_v2.search_redis.service.RedisSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class StoreSearchPerformanceTest {

    @Autowired
    private RedisSearchService redisSearchService;

    private static final int TOTAL_REQUESTS = 50_000;
    private static final int THREAD_COUNT = 10;

    private static final String[] testNames = {"사랑 떡볶이", "건강 김밥", "행복 초밥", "최고 치킨", "원조 국밥"};
    private static final String[] testCategories = {"KOREAN", "CHINESE", "JAPANESE", "FASTFOOD", "CHICKEN"};

    @Test
    @Rollback(value = false)
    public void testSearchStoresPerformance() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(TOTAL_REQUESTS);

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            executorService.submit(() -> {
                try {
                    String name = testNames[new Random().nextInt(testNames.length)];
                    String category = testCategories[new Random().nextInt(testCategories.length)];
                    redisSearchService.searchStores(name, category, 0, 10);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }
}