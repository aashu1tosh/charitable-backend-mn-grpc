package org.charitable.app.application.port.inbound.user;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Introspected
@Getter
@AllArgsConstructor
@Builder
public class UserRegisterRequestDTO {

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    private float latitude;

    private float longitude;
}
