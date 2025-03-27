package com.example.lucky7.domain.search.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchCount {
    private String keyword;
    private Long count;
}
