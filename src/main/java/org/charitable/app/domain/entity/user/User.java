package org.charitable.app.domain.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.base.Base;
import org.charitable.app.infrastructure.adapter.outbound.jpa.base.BaseEntity;

@Getter
@Setter
@AllArgsConstructor
public class User extends Base {
    private String firstName;
    private String middleName;
    private String lastName;
    private float latitude;
    private float longitude;
    private Auth auth;
}
