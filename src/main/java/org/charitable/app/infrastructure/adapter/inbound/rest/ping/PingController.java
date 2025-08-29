package org.charitable.app.infrastructure.adapter.inbound.rest.ping;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/api/ping")
public class PingController {

    @Get()
    public String ping() {
        System.out.println("Ping received");
        return "pong";
    }
}
