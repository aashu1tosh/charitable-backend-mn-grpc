package org.charitable.app.domain.entity.auth.authStatusHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.base.Base;
import org.charitable.app.domain.model.auth.AuthStatus;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class AuthStatusHistory extends Base {
    private Auth auth;
    private AuthStatus status;
}
