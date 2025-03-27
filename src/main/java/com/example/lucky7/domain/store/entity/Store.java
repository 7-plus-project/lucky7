package com.example.lucky7.domain.store.entity;

import ch.hsr.geohash.GeoHash;
import com.example.lucky7.domain.common.entity.Timestamped;
import com.example.lucky7.domain.store.dto.request.StoreUpdateRequest;
import com.example.lucky7.domain.store.enums.StoreCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
/* MYSQL 위치 검색 - 인덱스 추가 */
@Table(name = "stores"
     , indexes = {@Index(name = "idx_store_location", columnList = "location", unique = false)}
)
@Where(clause = "deleted_at IS NULL")
public class Store extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @Column
    private LocalDateTime deletedAt;

    public Store(String name, String address, StoreCategory category) {
        this.name = name;
        this.address = address;
        this.category = category;
    }

    public void deleteStore(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void updateStore(StoreUpdateRequest request) {
        this.name = request.getName();
        this.address = request.getAddress();
        this.category = request.getCategory();
    }


    // ------------------- GeoHash 사용한 위치 기반 검색 시작 ----------------------------

    private Double latitude;

    private Double longitude;

    private String geoHash;


    public Store(String name, String address, StoreCategory category, Double latitude, Double longitude) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoHash = generateGeoHash(latitude, longitude);
    }


    private String generateGeoHash(double lat, double lon) {
        return GeoHash.withCharacterPrecision(lat, lon, 7).toBase32();
    }

    // ------------------- GeoHash 사용한 위치 기반 검색 끝 ----------------------------


    /* MYSQL 위치 검색 - 컬럼 추가 */
    @Column(nullable = false)
    private double storeLon; // 경도
    @Column(nullable = false)
    private double storeLat; // 위도


    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point location; // Point(경도, 위도)

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    // storeLon, storeLat으로 location(Point) 설정
    public void setLocation(double storeLon, double storeLat) {
        this.location = geometryFactory.createPoint(new Coordinate(storeLon, storeLat));
        this.location.setSRID(4326);
    }
    private void ensureLocation() {
        this.setLocation(this.storeLon, this.storeLat);
    }

    // @PrePersist: 엔티티가 처음 저장될 때 location 필드 설정
    @PrePersist
    public void prePersist() {
        ensureLocation();
    }

    // @PreUpdate: 엔티티가 업데이트될 때 (경도/위도 값이 변경되면) 자동으로 location 필드 갱신
    @PreUpdate
    public void preUpdate() {
        ensureLocation();
    }

    public Store(String name, String address, StoreCategory category, double storeLon, double storeLat) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.storeLon = storeLon;
        this.storeLat = storeLat;
    }
    
    // ------------------- Kakao API 사용 ----------------------------
    @Column(nullable = false)
    private double latitudeKakao;

    @Column(nullable = false)
    private double longitudeKakao;
    
    public static Store fromKakao(String name, String address, StoreCategory category, double latitude, double longitude) {
        Store store = new Store(name, address, category);
        store.latitudeKakao = latitude;
        store.longitudeKakao = longitude;
        return store;
    }
}
