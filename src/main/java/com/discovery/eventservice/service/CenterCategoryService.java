package com.discovery.eventservice.service;

import com.discovery.eventservice.dto.request.CenterCategoryRequest;
import com.discovery.eventservice.model.CenterCategory;

import java.util.List;
import java.util.UUID;

public interface CenterCategoryService {
    List<CenterCategory> getAllCenterCategories();
    CenterCategory getCenterCategoryById(UUID id);
    CenterCategory createCenterCategory(CenterCategoryRequest centerCategoryRequest);
    CenterCategory updateCenterCategory(UUID id, CenterCategoryRequest centerCategory);
    void deleteCenterCategory(UUID id);
}
