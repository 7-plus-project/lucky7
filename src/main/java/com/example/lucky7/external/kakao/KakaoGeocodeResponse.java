package com.example.lucky7.external.kakao;

import lombok.Data;

import java.util.List;

@Data
public class KakaoGeocodeResponse {
    private List<Document> documents;

    @Data
    public static class Document {
        private String address_name;
        private String y; // latitude
        private String x; // longitude
    }
}