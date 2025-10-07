package com.discovery.eventservice.repository;

import com.discovery.eventservice.model.Center;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CenterRepository extends JpaRepository<Center, UUID>, JpaSpecificationExecutor<Center> {
    List<Center> findByCategory(String category);
    List<Center> findByLocationContainingIgnoreCase(String location);
    List<Center> findByOwnerId(UUID ownerId);
    // Find centers within a certain radius (in meters) from a given point
    @Query(value = """
            SELECT c.*
            FROM centers c
            WHERE ST_DWithin(c.coordinates, :point, :radius)
            ORDER BY ST_Distance(c.coordinates, :point)
            """, nativeQuery = true)
    List<Center> findNearbyCenters(Point point, double radius);
}

