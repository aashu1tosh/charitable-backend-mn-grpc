package org.charitable.app.application.port.inbound.ping;

import org.charitable.app.domain.model.ping.Ping;

public interface IPingUseCase {
    Ping ping();
}
