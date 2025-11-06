package org.charitable.app.common.utils;

import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
public class ValueUtils {

        // --- String ---
        public static boolean checkNullOrEmpty(String value) {
            return value == null || value.trim().isEmpty();
        }

        public static boolean checkNullOrEmpty(UUID value) {
            return value == null;
        }

        // --- Collection (List, Set, etc.) ---
        public static boolean checkNullOrEmpty(Collection<?> value) {
            return value == null || value.isEmpty();
        }

        // --- Map ---
        public static boolean checkNullOrEmpty(Map<?, ?> value) {
            return value == null || value.isEmpty();
        }

        // --- Array ---
        public static boolean checkNullOrEmpty(Object[] value) {
            return value == null || value.length == 0;
        }

        // --- Number (Integer, Float, Double, Long, etc.) ---
        public static boolean checkNullOrEmpty(Number value) {
            // Only null is considered empty for numbers
            // You could add custom logic if you want 0 to also count as "empty"
            return value == null;
        }
}


