package com.example.lucky7.domain.review.enums;

import lombok.Getter;

import java.util.Arrays;

public enum StarPoint {
    VERY_GOOD(5L),
    GOOD(4L),
    NORMAL(3L),
    BAD(2L),
    VERY_BAD(1L);

    @Getter
    private final Long pointValue;

    StarPoint(Long pointValue) {
        this.pointValue = pointValue;
    }

    public static StarPoint of(Long starPoint) {
        return Arrays.stream(StarPoint.values()).filter(sp -> sp.pointValue.equals(starPoint))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("StarPoint Not Found"));
    }
}
