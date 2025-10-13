package com.discovery.eventservice.repository;

import com.discovery.eventservice.model.CenterCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CenterCategoryRepository extends JpaRepository<CenterCategory, UUID> {
    Optional<CenterCategory> findByNameIgnoreCase(String name);
}
