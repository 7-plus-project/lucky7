package com.example.lucky7.domain.search_v2.autocomplete.repository;


import com.example.lucky7.domain.search.entity.Search;

import java.util.List;

public interface AutoCompleteRepository {
    List<Search> getAutoComplete(String prefix, int limit);
}
