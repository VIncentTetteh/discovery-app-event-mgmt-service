package com.discovery.eventservice.repository;

import com.discovery.eventservice.dto.response.CenterDistanceProjection;
import com.discovery.eventservice.model.Center;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CenterRepository extends JpaRepository<Center, UUID>, JpaSpecificationExecutor<Center> {

    List<Center> findByLocationContainingIgnoreCase(String location);

    List<Center> findByOwnerId(UUID ownerId);

    // ✅ Fetch all centers with categories eagerly (avoids ConcurrentModificationException)
    @Query("""
    SELECT DISTINCT c FROM Center c
    LEFT JOIN FETCH c.categories
    """)
    List<Center> findAllWithCategories();


    // ✅ Native query to find centers by category and distance
    @Query(value = """
        SELECT 
            c.id AS id,
            c.name AS name,
            c.description AS description,
            c.location AS location,
            c.owner_id AS ownerId,
            c.latitude AS latitude,
            c.longitude AS longitude,
            ST_Distance(c.coordinates, :point) AS distance
        FROM centers c
        JOIN center_category_map ccm ON ccm.center_id = c.id
        JOIN center_categories cat ON cat.id = ccm.category_id
        WHERE ST_DWithin(c.coordinates, :point, :radius)
          AND (:categories IS NULL OR LOWER(cat.name) = ANY(:categories))
        ORDER BY distance
        """,
            nativeQuery = true)
    List<CenterDistanceProjection> findNearbyCentersByCategories(
            @Param("point") Point point,
            @Param("radius") double radius,
            @Param("categories") String[] categories);
}


