package com.discovery.eventservice.service;

import java.time.Duration;

public interface S3Service {

    String uploadQrCode(String qrValue);
    String generatePresignedUrl(String objectKey, Duration expiry);
}
