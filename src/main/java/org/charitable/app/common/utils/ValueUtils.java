package org.charitable.app.common.utils;

import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
public class ValueUtils {


    /**
     * Safely returns the given value or a default one if null.
     *
     * @param value        the input value
     * @param defaultValue the fallback value if input is null
     * @return the value or defaultValue
     */
    public static <T> T safe(T value, T defaultValue) {
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    // If no default is given, just return null-safe value as-is
    public static <T> T safe(T value) {
        return value;
    }

    /**
     * Safely converts a value to String.
     *
     * @param value the input value
     * @return the string value or empty string if null
     */
    public static String safeString(Object value) {
        return Optional.ofNullable(value)
                .map(Object::toString)
                .orElse("");
    }
}
