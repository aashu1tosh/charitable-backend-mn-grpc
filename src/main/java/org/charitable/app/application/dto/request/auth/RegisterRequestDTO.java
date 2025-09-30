package org.charitable.app.application.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequestDTO {

    @Email(message = "Email address must be a valid Email Address")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message =  "Password cannot be empty")
    private String password;
}
