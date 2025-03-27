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

    private Double score; // sorted set - score

    public Search(String keyword, Long count, Double score) {
        this.keyword = keyword;
        this.count = count;
        this.score = score;
    }

    // score 수정 메서드
    public void updateScore(Double newScore) {
        this.score = newScore;
    }

}
