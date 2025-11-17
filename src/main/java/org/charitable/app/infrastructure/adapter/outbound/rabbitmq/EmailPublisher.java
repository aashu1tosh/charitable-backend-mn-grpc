package org.charitable.app.infrastructure.adapter.outbound.rabbitmq;

import io.micronaut.rabbitmq.annotation.RabbitClient;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.Queue;

@RabbitClient("${rabbitmq.exchange.emails}")
public interface EmailPublisher {

    @Binding("${rabbitmq.routing-key.send}")
    void sendEmail(EmailMessage message);
}
