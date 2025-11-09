package org.charitable.app.domain.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.base.Base;
import org.charitable.app.domain.model.Role;
import org.charitable.app.infrastructure.adapter.outbound.jpa.base.BaseEntity;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class User extends Base {
    private String firstName;
    private String middleName;
    private String lastName;
    private Float latitude;
    private Float longitude;
    private Auth auth;
}
