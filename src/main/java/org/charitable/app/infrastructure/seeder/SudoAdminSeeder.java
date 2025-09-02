package org.charitable.app.infrastructure.seeder;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;

@Singleton
public class DatabaseSeeder implements ApplicationEventListener<ServerStartupEvent> {

    private final AuthRepository authRepository; // Your repository

    public DatabaseSeeder(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        seedAdminUsers();
    }

    private void seedAdminUsers() {
        // Check if admin exists
        if (!authRepository.existsByEmail("admin@example.com")) {
            AuthEntity admin = new AuthEntity(
                    "admin@example.com",
                    // Hash the password properly
                    "hashedPassword123",
                    UserRole.ADMIN
            );
            authRepository.save(admin);
        }
    }
}