package com.discovery.eventservice.service;

import com.beust.jcommander.internal.Nullable;
import com.discovery.eventservice.dto.request.CenterRequest;
import com.discovery.eventservice.dto.response.CenterResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public interface CenterService {
    CenterResponse createCenter(CenterRequest request, UUID ownerId);
    CenterResponse getCenter(UUID id);
    List<CenterResponse> getAllCenters();
//    List<CenterResponse> getCentersByCategory(UUID categoryId);
    List<CenterResponse> getCentersByLocation(String location);
    void deleteCenter(UUID id, UUID ownerId) throws AccessDeniedException;

    List<CenterResponse> getAllCentersByOwnerID(UUID ownerId);
//    List<CenterResponse> findCentersNearby(double latitude, double longitude, double radiusMeters);

//    List<CenterResponse> findCentersNearby(
//            double latitude,
//            double longitude,
//            double radiusMeters,
//            @Nullable String category);

    List<CenterResponse> findCentersNearby(
            double latitude,
            double longitude,
            double radiusMeters,
            @Nullable List<String> categories);
}

