//package org.charitable.app.infrastructure.seeder;
//
//import io.micronaut.context.event.ApplicationEventListener;
//import io.micronaut.runtime.server.event.ServerStartupEvent;
//import jakarta.inject.Singleton;
//import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthEntity;
//import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthRepository;
//import io.micronaut.security.password.PasswordEncoder;
//import jakarta.transaction.Transactional;
//
//@Singleton
//public class DatabaseSeeder implements ApplicationEventListener<ServerStartupEvent> {
//
//    private final AuthRepository authRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public DatabaseSeeder(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
//        this.authRepository = authRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    @Transactional
//    public void onApplicationEvent(ServerStartupEvent event) {
//        seedAdminUsers();
//    }
//
//    private void seedAdminUsers() {
//        if (!authRepository.existsByEmail("admin@example.com")) {
//            String hashedPassword = passwordEncoder.encode("aStrongAdminPassword123!");
//
//            AuthEntity admin = AuthEntity.builder()
//                    .email("admin@example.com")
//                    .password(hashedPassword)
//                    .phoneNumber("9876543210") // Add the missing phone number
//                    .role("ADMIN") // Use the string value
//                    .build();
//
//            authRepository.save(admin);
//            System.out.println("Seeded admin user successfully.");
//        }
//    }
//}