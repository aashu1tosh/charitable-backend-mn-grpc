package org.charitable.app.domain.port.outbound.emailQueue;

import org.charitable.app.infrastructure.adapter.outbound.rabbitmq.EmailMessage;

import java.util.Map;

public interface EmailPublisherPort {
    void sendEmail(EmailMessage message);
}
