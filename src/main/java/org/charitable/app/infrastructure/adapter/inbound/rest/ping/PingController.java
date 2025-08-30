package org.charitable.app.infrastructure.adapter.inbound.rest.ping;

import org.charitable.app.application.dto.response.ApiResponse;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/api/v1/ping")
public class PingController {

    @Get()
    public ApiResponse<String> ping() {
        return new ApiResponse<>(true, "Ping successful", "pong");
    }
}
