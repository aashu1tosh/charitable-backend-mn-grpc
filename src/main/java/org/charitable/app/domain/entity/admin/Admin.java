package org.charitable.app.domain.entity.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.base.Base;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class Admin extends Base {
    private String firstName;
    private String middleName;
    private String lastName;
    private Auth auth;
}
