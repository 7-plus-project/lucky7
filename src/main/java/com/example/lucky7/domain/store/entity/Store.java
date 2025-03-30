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
//        , indexes = {@Index(name = "idx_store_location", columnList = "location", unique = false)}
)
@Where(clause = "deleted_at IS NULL")
public class Store extends Timestamped {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

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


    // ------------------- 위치 기반 검색을 위한 컬럼 ----------------------------

    private Double longitude; //경도 : 127. ...

    private Double latitude; //위도 : 37. ...

    private String geoHash;

    @Column(columnDefinition = "POINT SRID 4326")
    private Point location; // Point(경도, 위도)


    public Store(String name, String address, StoreCategory category, Double longitude, Double latitude) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.longitude = longitude;
        this.latitude = latitude;
        this.geoHash = generateGeoHash(latitude, longitude);
    }


    private String generateGeoHash(double lat, double lon) {
        return GeoHash.withCharacterPrecision(lat, lon, 7).toBase32();
    }


    // longitude, latitude 로 location(Point) 설정
    public void setLocation(double longitude, double latitude) {
        this.location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        this.location.setSRID(4326);
    }
    private void ensureLocation() {
        this.setLocation(this.longitude, this.latitude);
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

    // ------------------- Kakao API 사용 ----------------------------
    public static Store fromKakao(String name, String address, StoreCategory category, double latitude, double longitude) {
        Store store = new Store(name, address, category);
        store.latitude = latitude;
        store.longitude = longitude;
        return store;
    }
}
