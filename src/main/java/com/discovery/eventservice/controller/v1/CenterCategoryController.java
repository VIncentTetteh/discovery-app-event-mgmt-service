package com.discovery.eventservice.controller.v1;

import com.discovery.eventservice.model.CenterCategory;
import com.discovery.eventservice.service.CenterCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/center-categories")
@RequiredArgsConstructor
public class CenterCategoryController {

    private final CenterCategoryService centerCategoryService;

    // ✅ Get all categories
    @GetMapping
    public ResponseEntity<List<CenterCategory>> getAllCategories() {
        return ResponseEntity.ok(centerCategoryService.getAllCenterCategories());
    }

    // ✅ Get a category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CenterCategory> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(centerCategoryService.getCenterCategoryById(id));
    }

    // ✅ Create a new category
    @PostMapping
    public ResponseEntity<CenterCategory> createCategory(@RequestBody CenterCategory category) {
        CenterCategory created = centerCategoryService.createCenterCategory(category);
        return ResponseEntity.ok(created);
    }

    // ✅ Update existing category
    @PutMapping("/{id}")
    public ResponseEntity<CenterCategory> updateCategory(
            @PathVariable UUID id,
            @RequestBody CenterCategory updatedCategory) {

        CenterCategory category = centerCategoryService.updateCenterCategory(id, updatedCategory);
        return ResponseEntity.ok(category);
    }

    // ✅ Delete a category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        centerCategoryService.deleteCenterCategory(id);
        return ResponseEntity.noContent().build();
    }
}
