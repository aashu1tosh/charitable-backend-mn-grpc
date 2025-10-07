package org.charitable.app.domain.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdentityTokens {
    private String accessToken;
    private String refreshToken;
}
