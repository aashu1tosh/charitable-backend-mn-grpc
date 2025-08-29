package org.charitable.app.application;

import java.time.Instant;

import org.charitable.app.domain.Ping;

public class PingService {

    public Ping ping() {
        return new Ping("pong", Instant.now());
    }
}
