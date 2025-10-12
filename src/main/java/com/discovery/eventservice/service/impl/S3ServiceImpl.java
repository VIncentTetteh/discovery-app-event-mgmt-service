package com.discovery.eventservice.service.impl;

import com.discovery.eventservice.service.S3Service;
import com.discovery.eventservice.util.QrCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${app.aws.bucket}")
    private String bucketName;

    /**
     * Uploads QR code image to private S3 bucket.
     * Stores object key (not public URL).
     */
    @Override
    public String uploadQrCode(String qrValue) {
        try {
            // Generate QR code image
            BufferedImage qrImage = QrCodeGenerator.generate(qrValue);

            // Convert to bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            byte[] bytes = baos.toByteArray();

            // Object key in S3
            String objectKey = "qrcodes/" + qrValue + ".png";

            // Upload private object
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .contentType("image/png")
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(bytes));

            return objectKey; // store key in DB, not URL

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate/upload QR code", e);
        }
    }

    /**
     * Generates a temporary presigned URL for accessing the QR code.
     */
    @Override
    public String generatePresignedUrl(String objectKey, Duration expiry) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(expiry)
                .getObjectRequest(getRequest)
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }
}
