package com.example.lucky7.domain.search.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "searchs")
public class Search{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;  // 이름 or 카테고리

    private Long count;

    public Search(String keyword, Long count) {
        this.keyword = keyword;
        this.count = count;
    }

}
