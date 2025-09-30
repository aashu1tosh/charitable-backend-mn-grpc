package org.charitable.app.domain.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Auth {
    private UUID id;
    private String email;
    private String password;
    private String phone;
    private String role;
}
