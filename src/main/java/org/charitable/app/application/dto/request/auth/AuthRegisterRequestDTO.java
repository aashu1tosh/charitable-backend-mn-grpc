package org.charitable.app.application.dto.request.auth;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.charitable.app.domain.model.Role;
import org.charitable.app.domain.model.auth.AuthStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Introspected
public class AuthRegisterRequestDTO {

    @Email(message = "Email address must be a valid Email Address")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message =  "Password cannot be empty")
    private String password;

    @NotBlank(message = "Phone cannot be empty")
    @Pattern(regexp = "^(\\+977)?9\\d{9}$", message = "Phone must be a valid Nepali phone number")
    private String phone;

    @NotNull(message = "Role cannot be null")
    private Role role;

    @NotNull(message = "Status cannot be null")
    private AuthStatus status;
}
