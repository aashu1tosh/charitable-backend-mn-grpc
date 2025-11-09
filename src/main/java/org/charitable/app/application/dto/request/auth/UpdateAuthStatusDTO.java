package org.charitable.app.application.dto.request.auth;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.charitable.app.domain.model.auth.AuthStatus;

import java.util.UUID;

@Getter
@Setter
@Builder
@Introspected
public class UpdateAuthStatusDTO {
    @NotBlank(message = "Id of user is required ")
    private UUID id;

    @NotBlank(message = "Auth Status is required")
    private AuthStatus authStatus;
}
