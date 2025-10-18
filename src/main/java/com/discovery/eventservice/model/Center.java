package com.discovery.eventservice.model;

import com.discovery.eventservice.util.Uuid7Type;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
        name = "centers",
        indexes = {
                @Index(name = "idx_centers_coordinates", columnList = "coordinates"),
                @Index(name = "idx_centers_name", columnList = "name")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE centers SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Center extends BaseEntity{

    @Id
    @GeneratedValue(generator = "uuid7")
    @GenericGenerator(name = "uuid7", type = Uuid7Type.class)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 150, unique = true)
    private String name;

    private String description;

    @Column(length = 255)
    private String location; // e.g., "Osu, Accra"

    /**
     * ✅ Many-to-Many: A center can belong to multiple categories,
     * and a category can include multiple centers.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "center_category_map", // must match DB table name
            joinColumns = @JoinColumn(name = "center_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CenterCategory> categories = new HashSet<>();

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId; // from user-service (via gRPC, API, etc.)

    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point coordinates;

    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();


    // Keep coordinates synced with lat/lon
    public void setCoordinates(Point point) {
        this.coordinates = point;
        if (point != null) {
            this.latitude = point.getY();  // latitude
            this.longitude = point.getX(); // longitude
        }
    }
}