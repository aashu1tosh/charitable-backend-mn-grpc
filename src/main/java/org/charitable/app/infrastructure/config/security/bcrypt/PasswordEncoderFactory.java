package org.charitable.app.infrastructure.config.security.bcrypt;

import io.micronaut.context.annotation.Factory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.inject.Singleton;

@Factory
public class PasswordEncoderFactory {

    @Singleton
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


