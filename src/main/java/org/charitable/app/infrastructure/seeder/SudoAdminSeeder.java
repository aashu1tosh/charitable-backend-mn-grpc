//package org.charitable.app.infrastructure.seeder;
//
//import io.micronaut.context.event.ApplicationEventListener;
//import io.micronaut.runtime.server.event.ServerStartupEvent;
//import jakarta.inject.Singleton;
//import jakarta.transaction.Transactional;
//import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthEntity;
//import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthJpaRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Singleton
//public class SudoAdminSeeder implements ApplicationEventListener<ServerStartupEvent> {
//
//    private final AuthJpaRepository authRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public SudoAdminSeeder(AuthJpaRepository authRepository, PasswordEncoder passwordEncoder) {
//        this.authRepository = authRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    @Transactional
//    public void onApplicationEvent(ServerStartupEvent event) {
//        seedAdminUser();
//    }
//
//    private void seedAdminUser() {
//        String adminEmail = "admin@example.com";
//
//        var existingAdmin = authRepository.findByEmail(adminEmail);
//
//        if (existingAdmin.isPresent()) {
//            String rawPassword = "Admin@123";
//            String hashedPassword = passwordEncoder.encode(rawPassword);
//
//            AuthEntity admin = AuthEntity.builder()
//                    .email(adminEmail)
//                    .password(hashedPassword)
//                    .phoneNumber("9843818516")
//                    .role("SUDO_ADMIN")
//                    .build();
//
//            authRepository.save(admin);
//            System.out.println("✅ Seeded admin user successfully.");
//        } else {
//            System.out.println("ℹ️ Admin user already exists, skipping seeding.");
//        }
//    }
//}
