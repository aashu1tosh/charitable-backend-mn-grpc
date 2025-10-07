package org.charitable.app.infrastructure.config.security.bcrypt;

import jakarta.inject.Singleton;
import org.charitable.app.domain.port.outbound.passwordHash.PasswordHash;
import org.springframework.security.crypto.password.PasswordEncoder;

@Singleton
class BcryptPasswordHasher implements PasswordHash {

    private final PasswordEncoder encoder;

    public BcryptPasswordHasher(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public boolean matches(String raw, String hashed) {
        return encoder.matches(raw, hashed);
    }

    @Override
    public String hash(String raw) {
        return encoder.encode(raw);
    }
}
