package org.charitable.app.application.service.ping;

import java.time.Instant;

import org.charitable.app.domain.Ping;

public class PingService {

    public Ping ping() {
        return new Ping("pong", Instant.now());
    }
}
