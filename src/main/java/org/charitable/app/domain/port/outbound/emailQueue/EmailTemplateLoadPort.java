package org.charitable.app.domain.port.outbound.emailQueue;

import java.util.Map;

public interface EmailTemplateLoadPort {
    public String loadTemplate(String templateName, Map<String, Object> variables);
}
