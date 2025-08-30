package org.charitable.app.application.dto.response;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

@Data
@Serdeable
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String timestamp;
    private T data;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.timestamp = nowUtc();
        this.data = data;

    }

    private static String nowUtc() {
        String nowUtc = DateTimeFormatter.ISO_INSTANT
                .format(Instant.now().atZone(ZoneOffset.UTC));
        return nowUtc;
    }
}
