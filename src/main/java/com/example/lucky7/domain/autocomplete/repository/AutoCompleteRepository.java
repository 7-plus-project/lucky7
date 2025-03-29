package com.example.lucky7.domain.autocomplete.repository;


import java.util.List;

public interface AutoCompleteRepository {
    List<String> getAutoComplete(String prefix, int limit);
}
