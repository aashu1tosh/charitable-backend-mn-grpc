package org.charitable.app.common.utils;

import io.micronaut.json.JsonMapper;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PrintUtils {

    // Use Micronaut Serde native mapper
    private static final JsonMapper jsonMapper = JsonMapper.createDefault();

    public static String prettyPrint(Object obj) {
        try {
            if (obj == null) {
                return "null";
            }
            return jsonMapper.writeValueAsString(obj);
        } catch (Exception e) {
            // fallback: simple toString if JSON serialization fails
            return obj.toString();
        }
    }
}


