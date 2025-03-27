package com.example.lucky7.domain.store.dto.response;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreResponseKakao {
    private Long id;
    private String name;
    private String address;
    private StoreCategory category;
    private double latitude;
    private double longitude;

    public static StoreResponseKakao toDto(Store store) {
    	return new StoreResponseKakao(
    		    store.getId(),
    		    store.getName(),
    		    store.getAddress(),
    		    store.getCategory(),
    		    store.getLatitudeKakao(),
    		    store.getLongitudeKakao()
    		);
    }
}