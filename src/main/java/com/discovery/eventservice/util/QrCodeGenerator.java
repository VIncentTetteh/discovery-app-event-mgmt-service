package com.discovery.eventservice.util;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;

public class QrCodeGenerator {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    private QrCodeGenerator() {
        // Utility class
    }

    public static BufferedImage generate(String qrValue) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrValue,
                    BarcodeFormat.QR_CODE,
                    DEFAULT_WIDTH,
                    DEFAULT_HEIGHT
            );
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}

