package org.charitable.app.application.dto.response;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;

@Getter
@Serdeable
public class AppResponse<T> {
    private final boolean success;
    private final String message;
    private final String timestamp;
    private final T data;

    public AppResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.timestamp = nowUtc();
        this.data = data;

    }

    private static String nowUtc() {
        return DateTimeFormatter.ISO_INSTANT
                .format(Instant.now().atZone(ZoneOffset.UTC));
    }
}
