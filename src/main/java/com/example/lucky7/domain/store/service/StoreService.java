package com.example.lucky7.domain.store.service;

import com.example.lucky7.domain.common.exception.InvalidRequestException;
import com.example.lucky7.domain.store.dto.request.StoreCreateRequest;
import com.example.lucky7.domain.store.dto.request.StoreUpdateRequest;
import com.example.lucky7.domain.store.dto.response.StoreListResponse;
import com.example.lucky7.domain.store.dto.response.StoreResponse;
import com.example.lucky7.domain.store.entity.Store;
import com.example.lucky7.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public StoreResponse save(StoreCreateRequest request) {

        Store store = new Store(
                request.getName(),
                request.getAddress(),
                request.getCategory()
        );

        Store savedStore = storeRepository.save(store);

        return StoreResponse.toDto(savedStore);

    }

    public Page<StoreListResponse> findStores(int page, int size, LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Store> stores = storeRepository.findAllByOrderByModifiedAtDesc(pageable, startDate, endDate);

        return stores.map(store -> new StoreListResponse(
                store.getId(),
                store.getName()
        ));
    }

    public StoreResponse findStore(Long storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new InvalidRequestException("해당 id의 가게가 존재하지 않습니다."));

        return StoreResponse.toDto(store);

    }

    @Transactional
    public StoreResponse update(Long storeId, StoreUpdateRequest request) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new InvalidRequestException("해당 id의 가게가 존재하지 않습니다."));

        store.updateStore(request);

        return StoreResponse.toDto(store);

    }

    @Transactional
    public void delete(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new InvalidRequestException("해당 id의 가게가 존재하지 않습니다."));

        store.deleteStore(LocalDateTime.now());
    }

}
