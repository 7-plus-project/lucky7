package com.example.lucky7.domain.store.dto.response;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreResponseLocation {
    private Long id;
    private String name;
    private String address;
    private StoreCategory category;
    private double latitude;
    private double longitude;

    public static StoreResponseLocation toDto(Store store) {
    	return new StoreResponseLocation(
    		    store.getId(),
    		    store.getName(),
    		    store.getAddress(),
    		    store.getCategory(),
    		    store.getLatitude(),
    		    store.getLongitude()
    		);
    }
}