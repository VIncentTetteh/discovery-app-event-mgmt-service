package com.discovery.eventservice.util;

import java.time.Instant;
import java.util.UUID;

public final class Uuid7Generator {

    private Uuid7Generator() {}

    public static UUID generate() {
        Instant now = Instant.now();
        long ms = now.toEpochMilli();
        int nanos = now.getNano();

        long mostSigBits = (ms << 16) | (nanos & 0xFFFF);
        long leastSigBits = UUID.randomUUID().getLeastSignificantBits();

        // Set version 7
        mostSigBits &= 0xFFFFFFFFFFFF0FFFL;
        mostSigBits |= 0x0000000000007000L;

        // Set IETF variant
        leastSigBits &= 0x3FFFFFFFFFFFFFFFL;
        leastSigBits |= 0x8000000000000000L;

        return new UUID(mostSigBits, leastSigBits);
    }
}
