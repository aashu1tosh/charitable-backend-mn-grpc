//package org.charitable.app.infrastructure.seeder;
//
//import io.micronaut.context.event.ApplicationEventListener;
//import io.micronaut.runtime.server.event.ServerStartupEvent;
//import jakarta.inject.Singleton;
//import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthEntity;
//import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Singleton
//public class SudoAdminSeeder {
//
//    private final AuthRepository authRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public SudoAdminSeeder(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
//        this.authRepository = authRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Transactional
//    public void seedAdminUsers() {
//        if (!authRepository.existsByEmail("admin@example.com")) {
//            String hashedPassword = passwordEncoder.encode("aStrongAdminPassword123!");
//
//            AuthEntity admin = AuthEntity.builder()
//                    .email("admin@example.com")
//                    .password(hashedPassword)
//                    .phoneNumber("9876543210")
//                    .role("ADMIN")
//                    .build();
//
//            authRepository.save(admin);
//            System.out.println("Seeded admin user successfully.");
//        } else {
//            System.out.println("Admin user already exists, skipping seeding.");
//        }
//    }
//}