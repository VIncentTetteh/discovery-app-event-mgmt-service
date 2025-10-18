package com.discovery.eventservice.service.impl;


import com.beust.jcommander.internal.Nullable;
import com.discovery.eventservice.dto.request.CenterRequest;
import com.discovery.eventservice.dto.response.CenterDistanceProjection;
import com.discovery.eventservice.dto.response.CenterResponse;
import com.discovery.eventservice.exception.CenterAlreadyExistsException;
import com.discovery.eventservice.mapper.CenterMapper;
import com.discovery.eventservice.model.Center;
import com.discovery.eventservice.model.CenterCategory;
import com.discovery.eventservice.repository.CenterCategoryRepository;
import com.discovery.eventservice.repository.CenterRepository;
import com.discovery.eventservice.service.CenterService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CenterServiceImpl implements CenterService {

    private final CenterRepository centerRepository;
    private final CenterMapper centerMapper;
    private final CenterCategoryRepository centerCategoryRepository;
    private final GeometryFactory geometryFactory;

    @Override
    public CenterResponse createCenter(CenterRequest request, UUID ownerId) {
        boolean exists = centerRepository.findAll().stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(request.name())
                        && c.getLocation().equalsIgnoreCase(request.location()));

        if (exists) {
            throw new CenterAlreadyExistsException("Center with the same name and location already exists.");
        }

        Set<CenterCategory> categories = new HashSet<>(
                centerCategoryRepository.findAllById(request.categoryIds())
        );

        if (categories.isEmpty()) {
            throw new IllegalArgumentException("At least one valid category is required.");
        }

        // ✅ Build geometry point with SRID
        Point coordinates = geometryFactory.createPoint(new Coordinate(
                request.longitude(),
                request.latitude()
        ));
        coordinates.setSRID(4326);

        Center center = Center.builder()
                .name(request.name())
                .description(request.description())
                .location(request.location())
                .categories(categories)
                .coordinates(coordinates)
                .latitude(request.latitude())
                .longitude(request.longitude())
                .ownerId(ownerId)
                .build();

        Center saved = centerRepository.save(center);
        return centerMapper.toResponse(saved);
    }



    @Override
    @Transactional
    public CenterResponse getCenter(UUID id) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Center not found with id: " + id));
        return centerMapper.toResponse(center);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CenterResponse> getAllCenters() {
        List<Center> centers = centerRepository.findAllWithCategories();
        return centers.stream()
                .map(centerMapper::toResponse)
                .toList();
    }



    @Override
    @Transactional
    public List<CenterResponse> getCentersByLocation(String location) {
        return centerRepository.findByLocationContainingIgnoreCase(location)
                .stream()
                .map(centerMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteCenter(UUID id, UUID ownerId) throws AccessDeniedException {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Center not found with id: " + id));

        if (!center.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException("You are not allowed to delete this center.");
        }

        centerRepository.delete(center);
    }

    @Override
    public List<CenterResponse> getAllCentersByOwnerID(UUID ownerId) {
        return centerRepository.findByOwnerId(ownerId)
                .stream()
                .map(centerMapper::toResponse)
                .toList();
    }


    @Override
    public List<CenterResponse> findCentersNearby(
            double latitude,
            double longitude,
            double radiusMeters,
            @Nullable List<String> categories) {

        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        point.setSRID(4326);

        String[] categoryArray = (categories == null || categories.isEmpty())
                ? null
                : categories.stream().map(String::toLowerCase).toArray(String[]::new);

        List<CenterDistanceProjection> results =
                centerRepository.findNearbyCentersByCategories(point, radiusMeters, categoryArray);

        return results.stream()
                .map(p -> CenterResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .location(p.getLocation())
                        .ownerId(p.getOwnerId())
                        .latitude(p.getLatitude())
                        .longitude(p.getLongitude())
                        .distanceMeters(p.getDistance())
                        .build())
                .toList();
    }





}

