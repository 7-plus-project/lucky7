package com.example.lucky7.domain.search.dto.response;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchResponse {
    private final Long id;
    private final String name;
    private final StoreCategory category;

    public static SearchResponse toDto(Store store){
        return new SearchResponse(
                store.getId(),
                store.getName(),
                store.getCategory()
        );
    }
}
