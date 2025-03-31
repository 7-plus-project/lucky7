package com.example.lucky7.domain.search.dto.response;

import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.enums.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private Long id;
    private String name;
    private StoreCategory category;

    public static SearchResponse toDto(Store store){
        return new SearchResponse(
                store.getId(),
                store.getName(),
                store.getCategory()
        );
    }
}
