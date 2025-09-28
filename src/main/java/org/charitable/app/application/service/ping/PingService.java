package org.charitable.app.application.service.ping;

import org.charitable.app.application.port.inbound.ping.IPingUseCase;
import org.charitable.app.domain.model.ping.Ping;

import jakarta.inject.Singleton;

import java.time.Instant;

@Singleton
class PingService implements IPingUseCase {

    @Override
    public Ping ping() {
         return new Ping("pong", Instant.now());
    }
}
