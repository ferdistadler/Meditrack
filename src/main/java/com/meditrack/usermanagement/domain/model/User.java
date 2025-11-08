package com.meditrack.usermanagement.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private Email email;
    private Password password;
    private String firstName;
    private String lastName;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    private User(Email email, Password password, String firstName, String lastName, Role role) {
        if (email == null || password == null || role == null)
            throw new IllegalArgumentException("Required fields missing");
        validateName(firstName);
        validateName(lastName);
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public static User register(Email email, Password password, String firstName, String lastName, Role role) {
        return new User(email, password, firstName, lastName, role);
    }

    public boolean authenticate(String plainPassword) {
        if (plainPassword == null) return false;
        boolean ok = password.matches(plainPassword);
        if (ok) lastLogin = LocalDateTime.now();
        return ok;
    }

    public void changePassword(String oldPlain, String newPlain) {
        if (!password.matches(oldPlain)) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        this.password = new Password(newPlain);
    }

    public void updateProfile(String firstName, String lastName) {
        validateName(firstName);
        validateName(lastName);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank() || name.length() < 2 || name.length() > 50 ||
                !name.matches("^[A-Za-zÀ-ÖØ-öø-ÿ'\\-\\s]+$")) {
            throw new IllegalArgumentException("Invalid name: " + name);
        }
    }

    // Getters
    public Email getEmail() { return email; }
    public Password getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Role getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLogin() { return lastLogin; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}