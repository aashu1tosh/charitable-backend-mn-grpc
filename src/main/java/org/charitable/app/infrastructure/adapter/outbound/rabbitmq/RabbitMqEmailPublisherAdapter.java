package org.charitable.app.infrastructure.adapter.outbound.rabbitmq;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class RabbitMqEmailPublisherAdapter implements org.charitable.app.domain.port.outbound.emailQueue.EmailPublisherPort {

    private final EmailPublisher emailPublisher;

    public RabbitMqEmailPublisherAdapter(EmailPublisher emailPublisher) {
        this.emailPublisher = emailPublisher;
    }

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        log.info("Sending email to {}", emailMessage.getTo());
        emailPublisher.sendEmail(emailMessage);
    }
}
