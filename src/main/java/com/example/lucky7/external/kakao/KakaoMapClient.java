package com.example.lucky7.external.kakao;

import com.example.lucky7.external.kakao.Coordinate;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoMapClient {
    @Value("${kakao.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://dapi.kakao.com")
            .build();

    public Coordinate geocode(String address) {
        KakaoGeocodeResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/address.json")
                        .queryParam("query", address)
                        .build())
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .bodyToMono(KakaoGeocodeResponse.class)
                .block();

        if (response == null) {
        	throw new IllegalStateException("Kakao API 응답이 null입니다.");
		}
		if (response.getDocuments().isEmpty()) {
    		throw new IllegalArgumentException("주소 검색 결과가 없습니다: " + address);
		}        

        var doc = response.getDocuments().get(0);
        return new Coordinate(Double.parseDouble(doc.getY()), Double.parseDouble(doc.getX()));
    }
}