package org.charitable.app.domain;
    
import java.time.Instant;

public class Ping {
    private final String message;
    private final Instant timestamp;

    public Ping(String message, Instant timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
