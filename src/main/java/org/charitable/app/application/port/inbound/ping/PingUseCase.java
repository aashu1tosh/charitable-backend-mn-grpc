package org.charitable.app.application.port.inbound.ping;

import org.charitable.app.application.dto.response.AppResponse;

public interface PingUseCase {
    AppResponse<String> ping();
}
