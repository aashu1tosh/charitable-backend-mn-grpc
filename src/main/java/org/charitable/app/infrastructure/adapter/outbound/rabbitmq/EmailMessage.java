package org.charitable.app.infrastructure.adapter.outbound.rabbitmq;

import io.micronaut.serde.annotation.Serdeable;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Serdeable
public class EmailMessage {
    private String to;
    private String subject;
    private String templateName;
    private Map<String, Object> variables;
}
