package org.charitable.app.infrastructure.config.environment;

import io.micronaut.context.env.Environment;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.charitable.app.application.exception.AppException;

@Singleton
@AllArgsConstructor
public class EnvVariables {

    private final Environment environment;

    public  String getDataSourceUrl() {
        return getEnvOrThrow("datasources.default.url");
    }

    public String getDataSourceUsername() {
        return getEnvOrThrow("datasources.default.username");
    }

    public String getDataSourcePassword() {
        return getEnvOrThrow("datasources.default.password");
    }

    public String getJwtAccessTokenSecret() {
        return getEnvOrThrow("micronaut.security.token.jwt.generator.access-token.secret");
    }

    public String getJwtRefreshTokenSecret() {
        return getEnvOrThrow("micronaut.security.token.jwt.generator.access-token.secret");
    }

    private String getEnvOrThrow(String key) {
        return environment.getProperty(key, String.class).orElseThrow(() -> AppException.internal("Something went wrong. Please try again"));
    }

}
