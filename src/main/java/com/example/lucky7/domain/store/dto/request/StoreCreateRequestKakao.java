package com.example.lucky7.domain.store.dto.request;

import com.example.lucky7.domain.store.enums.StoreCategory;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreCreateRequestKakao {

    private String name;

    private String address;

    private StoreCategory category;
}
