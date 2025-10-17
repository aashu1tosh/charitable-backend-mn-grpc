package org.charitable.app.application.service.ping;

import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.application.port.inbound.ping.PingUseCase;

import jakarta.inject.Singleton;

@Singleton
class PingService implements PingUseCase {

    @Override
    public AppResponse<String> ping() {
         return new AppResponse<>(true, "Pong", "");
    }
}
