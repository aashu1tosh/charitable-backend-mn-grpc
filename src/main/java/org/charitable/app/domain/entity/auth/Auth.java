package org.charitable.app.domain.entity.auth;

import java.util.UUID;

public class Auth {

    private UUID id;
    private String email;
    private String password;
    private String phone;
    private String role;

    public Auth(UUID id, String email, String password, String phone, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    // Getters
    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public void changePassword(String newpassword) {
        this.password = newpassword;
    }

    public void updatePhone(String newPhone) {
        this.phone = newPhone;
    }
}
