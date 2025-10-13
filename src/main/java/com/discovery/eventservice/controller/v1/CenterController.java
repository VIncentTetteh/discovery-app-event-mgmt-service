package com.discovery.eventservice.controller.v1;

import com.discovery.eventservice.dto.request.CenterRequest;
import com.discovery.eventservice.dto.response.CenterResponse;
import com.discovery.eventservice.model.CustomUserPrincipal;
import com.discovery.eventservice.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/centers")
@RequiredArgsConstructor
public class CenterController {

    private final CenterService centerService;

    // -------------------- PUBLIC / USER ENDPOINTS --------------------

    @GetMapping
    public List<CenterResponse> getAllCenters() {
        return centerService.getAllCenters();
    }

    @GetMapping("/{id}")
    public CenterResponse getCenter(@PathVariable UUID id) {
        return centerService.getCenter(id);
    }

    @GetMapping("/search")
    public List<CenterResponse> searchCenters(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location
    ) {
//        if (category != null) return centerService.getCentersByCategory(category);
        if (location != null) return centerService.getCentersByLocation(location);
        return centerService.getAllCenters();
    }

    // -------------------- ADMIN / OWNER ENDPOINTS --------------------

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CenterResponse createCenter(@RequestBody CenterRequest request,
                                       @AuthenticationPrincipal String ownerId) {
//        log.info("Creating center for ownerId: {}", ownerId);

        return centerService.createCenter(request, UUID.fromString(ownerId));
    }

    @DeleteMapping("/{id}")

    public void deleteCenter(@PathVariable UUID id,
                             @AuthenticationPrincipal String ownerId) throws AccessDeniedException {
        centerService.deleteCenter(id,UUID.fromString(ownerId));
    }

    @GetMapping("/owner")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CenterResponse> getMyCenters(@AuthenticationPrincipal String ownerId) {
        return centerService.getAllCentersByOwnerID(UUID.fromString(ownerId));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<CenterResponse>> getNearbyCenters(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radiusMeters,
            @RequestParam(required = false) List<String> categories) {

        return ResponseEntity.ok(
                centerService.findCentersNearby(latitude, longitude, radiusMeters, categories)
        );
    }


}

