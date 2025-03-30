package com.example.lucky7.domain.store.repository;

import com.example.lucky7.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreBulkRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final int BATCH_SIZE = 1000;

    public void bulkInsert(List<Store> stores) {
        String sql = "INSERT INTO stores (name, address, category, latitude, longitude, geo_hash, location) " +
                "VALUES (?, ?, ?, ?, ?, ?, ST_GeomFromText(?, 4326))";

        jdbcTemplate.batchUpdate(sql, stores, BATCH_SIZE, (ps, store) -> {
            ps.setString(1, store.getName());
            ps.setString(2, store.getAddress());
            ps.setString(3, store.getCategory().name());
            ps.setDouble(4, store.getLatitude());
            ps.setDouble(5, store.getLongitude());
            ps.setString(6, store.getGeoHash());
            ps.setString(7, String.format("POINT(%f %f)", store.getLatitude(), store.getLongitude()));
        });
    }

}
