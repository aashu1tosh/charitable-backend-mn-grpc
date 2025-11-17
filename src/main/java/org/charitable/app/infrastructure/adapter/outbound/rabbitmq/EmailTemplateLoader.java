package org.charitable.app.infrastructure.adapter.outbound.rabbitmq;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.charitable.app.domain.port.outbound.emailQueue.EmailTemplateLoadPort;

@Singleton
@Slf4j
class EmailTemplateLoader implements EmailTemplateLoadPort {

    public String loadTemplate(String templateName, Map<String, Object> variables) {
        try (Scanner scanner = new Scanner(
                getClass().getResourceAsStream("/templates/email/" + templateName),
                StandardCharsets.UTF_8)) {

            String templateContent = scanner.useDelimiter("\\A").next();
            StringSubstitutor substitutor = new StringSubstitutor(variables, "{{", "}}");
//            Use this if template has ${key}
//            StringSubstitutor substitutor = new StringSubstitutor(variables);
            return substitutor.replace(templateContent);
        } catch (Exception e) {
            log.error("Error : {}", e);
            throw new RuntimeException("Failed to load template: " + templateName, e);
        }
    }
}
