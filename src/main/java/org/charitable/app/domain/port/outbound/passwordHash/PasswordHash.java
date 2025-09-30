package org.charitable.app.domain.port.outbound.passwordHash;

public interface PasswordHash {
    boolean matches(String rawPassword, String hashedPassword);
    String hash(String rawPassword);
}
