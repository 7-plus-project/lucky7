package com.example.lucky7.domain.store.enums;

import java.util.Arrays;

public enum StoreCategory {
    KOREAN,        // 한식
    JAPANESE,      // 일식
    CHINESE,       // 중식
    WESTERN,       // 양식
    FAST_FOOD,     // 패스트푸드
    CAFE,          // 카페 / 디저트
    BBQ,           // 바비큐 / 고기구이
    SEAFOOD;       // 해산물

    public static StoreCategory of(String category) {
        return Arrays.stream(StoreCategory.values())
                .filter(storecategory -> String.valueOf(storecategory).equalsIgnoreCase(category))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(category + " Doesn't Matches StoreCategory"));
    }
}
