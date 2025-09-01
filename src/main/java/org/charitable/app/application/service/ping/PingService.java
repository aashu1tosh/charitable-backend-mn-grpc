package org.charitable.app.application.service.ping;

import org.charitable.app.application.port.inbound.ping.IPingUseCase;
import org.charitable.app.domain.model.ping.Ping;

import jakarta.inject.Singleton;

@Singleton
class PingService implements IPingUseCase {

    @Override
    public Ping ping() {
        throw new RuntimeException("JPT Error occurred");
        // return new Ping("pong", Instant.now());
    }
}
