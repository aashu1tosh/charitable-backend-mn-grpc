package org.charitable.app.domain.model.ping;
    
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Ping {
    private final String message;
    private final Instant timestamp;
}
