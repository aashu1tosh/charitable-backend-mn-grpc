package org.charitable.app.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public final class UUIDUtils {

    /**
     * Generate a random UUID as a string.
     */
    public static String randomUUIDString() {
        return UUID.randomUUID().toString();
    }

    /**
     * Convert a String to UUID.
     *
     * @param id String representation of UUID
     * @return UUID object
     * @throws IllegalArgumentException if the string is not a valid UUID
     */
    public static UUID stringToUUID(String id) {
        return UUID.fromString(id);
    }

    /**
     * Convert a UUID to String.
     *
     * @param uuid UUID object
     * @return String representation of UUID
     */
    public static String uuidToString(UUID uuid) {
        return uuid.toString();
    }
}
