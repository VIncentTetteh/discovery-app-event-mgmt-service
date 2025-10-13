package com.discovery.eventservice.service;

import com.discovery.eventservice.model.CenterCategory;

import java.util.List;
import java.util.UUID;

public interface CenterCategoryService {
    List<CenterCategory> getAllCenterCategories();
    CenterCategory getCenterCategoryById(UUID id);
    CenterCategory createCenterCategory(CenterCategory centerCategory);
    CenterCategory updateCenterCategory(UUID id, CenterCategory centerCategory);
    void deleteCenterCategory(UUID id);
}
