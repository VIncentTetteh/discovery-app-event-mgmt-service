package com.discovery.eventservice.repository;

import com.discovery.eventservice.model.Event;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID>, JpaSpecificationExecutor<Event> {
    List<Event> findByCenterId(UUID centerId);
    List<Event> findByIsPrivateFalse();
    List<Event> findByTitleContainingIgnoreCase(String title);
    @Query(value = """
            SELECT e.*
            FROM events e
            JOIN centers c ON e.center_id = c.id
            WHERE ST_DWithin(c.coordinates, :point, :radius)
            ORDER BY ST_Distance(c.coordinates, :point)
            """, nativeQuery = true)
    List<Event> findEventsByNearbyCenters(Point point, double radius);

}

