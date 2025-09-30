package org.charitable.app.infrastructure.command;

import io.micronaut.configuration.picocli.PicocliRunner;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth.AuthGrpcService;
import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import picocli.CommandLine.Command;

@Command(name = "seed-admin", description = "Seeds the sudo admin user", mixinStandardHelpOptions = true)
public class SeedCommand implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AuthGrpcService.class);

    @Inject
    AuthJpaRepository authRepository;

    @Inject
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run() {

        try {
            String sudoAdminEmail = "sudo@admin.com";
            String rawPassword = "Admin@123";

            var check = authRepository.findByEmail(sudoAdminEmail);

            if (check.isEmpty()) {
                logger.info("🔄 Seeding SUDO ADMIN...");

                AuthEntity admin = AuthEntity.builder()
                        .email(sudoAdminEmail)
                        .password(passwordEncoder.encode(rawPassword))
                        .role("SUDO_ADMIN")
                        .phoneNumber("9876543210")
                        .build();

                authRepository.save(admin);

                logger.info("✅ SUDO ADMIN seeded successfully!");
                logger.info("📧 Email: " + sudoAdminEmail);
                logger.info("🔐 Password: " + rawPassword);
            } else {
                logger.info("ℹ️ SUDO ADMIN already exists, skipping seeding.");
            }
        } catch (Exception e) {
            logger.error("❌ Error seeding SUDO ADMIN: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        PicocliRunner.run(SeedCommand.class, args);
    }
}
