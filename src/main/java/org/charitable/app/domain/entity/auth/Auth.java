package org.charitable.app.domain.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.charitable.app.domain.entity.admin.Admin;
import org.charitable.app.domain.entity.base.Base;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.domain.entity.user.User;
import org.charitable.app.domain.model.Role;
import org.charitable.app.domain.model.auth.AuthStatus;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class Auth extends Base {
    private String email;
    private String password;
    private String phone;
    private Role role;
    private Boolean isEmailVerified;
    private AuthStatus status;
    private Organization organization;
    private User user;
    private Admin admin;
}
