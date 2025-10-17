package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.dto.request.CenterCategoryRequest;
import com.discovery.eventservice.model.CenterCategory;
import com.discovery.eventservice.repository.CenterCategoryRepository;
import com.discovery.eventservice.service.CenterCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CenterCategoryServiceImpl implements CenterCategoryService {

    private final CenterCategoryRepository centerCategoryRepository;

    @Override
    public List<CenterCategory> getAllCenterCategories() {
        return centerCategoryRepository.findAll();
    }

    @Override
    public CenterCategory getCenterCategoryById(UUID id) {
        return centerCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Center category not found with id: " + id));
    }

    @Override
    public CenterCategory createCenterCategory(CenterCategoryRequest centerCategoryRequest) {
        // Prevent duplicates by name
        centerCategoryRepository.findByNameIgnoreCase(centerCategoryRequest.name())
                .ifPresent(existing -> {
                    throw new RuntimeException("Center category already exists: " + existing.getName());
                });
        CenterCategory centerCategory = CenterCategory.builder()
                .name(centerCategoryRequest.name())
                .description(centerCategoryRequest.description())
                .build();

        return centerCategoryRepository.save(centerCategory);
    }

    @Override
    public CenterCategory updateCenterCategory(UUID id, CenterCategoryRequest updatedCategory) {
        CenterCategory existing = getCenterCategoryById(id);

        existing.setName(updatedCategory.name());
        existing.setDescription(updatedCategory.description());

        return centerCategoryRepository.save(existing);
    }

    @Override
    public void deleteCenterCategory(UUID id) {
        CenterCategory category = getCenterCategoryById(id);
        centerCategoryRepository.delete(category);
    }
}