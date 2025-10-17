package org.charitable.app.domain.model.token;

import lombok.Builder;
import lombok.Data;
import org.charitable.app.domain.model.Role;

import java.util.UUID;

@Data
@Builder
public class TokenPayload {
    private UUID id;
    private Role role;
    private UUID adminId;
    private UUID organizationId;
    private UUID userId;
}
