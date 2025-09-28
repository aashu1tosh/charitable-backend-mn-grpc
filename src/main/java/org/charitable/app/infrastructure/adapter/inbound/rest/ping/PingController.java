package org.charitable.app.infrastructure.adapter.inbound.rest.ping;

import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.charitable.app.application.dto.response.AppResponse;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/ping")
@Secured(SecurityRule.IS_ANONYMOUS)
public class PingController {

    @Get()
    public AppResponse<String> ping() {
        return new AppResponse<String>(true, "Ping successful", "pong");
    }
}
