package com.abhisek.challenge_maker.challenge_maker.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Utilities {
    public static UUID convertToUUID(Object obj) {
        if (obj instanceof byte[]) {
            byte[] bytes = (byte[]) obj;
            if (bytes.length == 16) {
                ByteBuffer bb = ByteBuffer.wrap(bytes);
                long mostSigBits = bb.getLong();
                long leastSigBits = bb.getLong();
                return new UUID(mostSigBits, leastSigBits);
            }
        } else if (obj instanceof String) {
            return UUID.fromString((String) obj);
        } else if (obj instanceof UUID) {
            return (UUID) obj;
        }
        return null;
    }
    // Helper method to convert HEX string to UUID
    public static UUID hexToUUID(String hex) {
        if (hex == null || hex.length() != 32) {
            return null;
        }
        // Insert hyphens to make it a proper UUID format
        String uuidString = hex.substring(0, 8) + "-" +
                hex.substring(8, 12) + "-" +
                hex.substring(12, 16) + "-" +
                hex.substring(16, 20) + "-" +
                hex.substring(20, 32);
        return UUID.fromString(uuidString);
    }

}
