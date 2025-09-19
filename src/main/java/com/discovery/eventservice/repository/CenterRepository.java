package com.discovery.eventservice.repository;

import com.discovery.eventservice.model.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CenterRepository extends JpaRepository<Center, UUID>, JpaSpecificationExecutor<Center> {
    List<Center> findByCategory(String category);
    List<Center> findByLocationContainingIgnoreCase(String location);
    List<Center> findByOwnerId(UUID ownerId);
}

