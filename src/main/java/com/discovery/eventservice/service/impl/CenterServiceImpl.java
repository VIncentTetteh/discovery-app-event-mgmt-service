package com.discovery.eventservice.service.impl;


import com.discovery.eventservice.dto.request.CenterRequest;
import com.discovery.eventservice.dto.response.CenterResponse;
import com.discovery.eventservice.exception.CenterAlreadyExistsException;
import com.discovery.eventservice.mapper.CenterMapper;
import com.discovery.eventservice.model.Center;
import com.discovery.eventservice.repository.CenterRepository;
import com.discovery.eventservice.service.CenterService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CenterServiceImpl implements CenterService {

    private final CenterRepository centerRepository;
    private final CenterMapper centerMapper;

    @Override
    public CenterResponse createCenter(CenterRequest request, UUID ownerId) {
        // check if center with same name and location already exists
        if (centerRepository.findAll().stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(request.name()) &&
                        c.getLocation().equalsIgnoreCase(request.location()))) {
            throw new CenterAlreadyExistsException("Center with same name and location already exists.");
        }
        Center center = centerMapper.toEntity(request);
        center.setOwnerId(ownerId);
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

    @Override
    @Transactional
    public List<CenterResponse> getAllCenters() {
        return centerRepository.findAll()
                .stream()
                .map(centerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<CenterResponse> getCentersByCategory(String category) {
        return centerRepository.findByCategory(category)
                .stream()
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


}

