package com.example.lucky7.domain.search.repository;

import com.example.lucky7.domain.search.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long>, SearchRepositoryCustom {
}
