package com.example.lucky7.domain.autocomplete;


import java.util.List;

public interface AutoCompleteRepository {
    List<String> getAutoComplete(String prefix, int limit);
}
