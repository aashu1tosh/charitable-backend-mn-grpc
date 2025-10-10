package org.charitable.app.infrastructure.config;

import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;
import org.charitable.app.infrastructure.config.environment.EnvVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

@Singleton
class DatabaseStartupCheck implements ApplicationEventListener<StartupEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseStartupCheck.class);

    private final String url;
    private final String username;
    private final String password;

    public DatabaseStartupCheck(
            EnvVariables environment) {
        this.url = environment.getDataSourceUrl();
        this.username = environment.getDataSourceUsername();
        this.password = environment.getDataSourcePassword();
    }

    @Override
    public void onApplicationEvent(StartupEvent event) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (connection.isValid(5)) {
                LOG.info("✓ Database connection successful!");
                LOG.info("✓ Server started successfully");
            } else {
                LOG.error("✗ Database connection validation failed");
                System.exit(1);
            }
        } catch (Exception e) {
            LOG.error("✗ Failed to connect to database: {}", e.getMessage());
            LOG.error("✗ Server startup aborted");
            System.exit(1);
        }
    }
}