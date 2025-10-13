package com.discovery.eventservice.dto.response;

import java.util.UUID;

public interface CenterDistanceProjection {
    UUID getId();
    String getName();
    String getDescription();
    String getLocation();
    UUID getOwnerId();
    Double getLatitude();
    Double getLongitude();
    Double getDistance();  // from ST_Distance
}
