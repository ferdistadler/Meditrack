package com.meditrack.usermanagement.domain.model;

import org.mindrot.jbcrypt.BCrypt;
import java.util.Objects;

public class Password {

    private final String encryptedValue;

    public Password(String plain) {
        validate(plain);
        this.encryptedValue = encrypt(plain);
    }

    private void validate(String plain) {
        if (plain == null || plain.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (plain.length() < 8) {
            throw new IllegalArgumentException("Password too short");
        }
        if (plain.length() > 128) {
            throw new IllegalArgumentException("Password too long");
        }
        if (!plain.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        if (!plain.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        if (!plain.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
        if (!plain.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;:,.<>?].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }
        if (plain.contains(" ")) {
            throw new IllegalArgumentException("Password must not contain whitespace");
        }
    }

    private String encrypt(String plain) {
        // Standard 10 Rounds Salting mit BCrypt
        return BCrypt.hashpw(plain, BCrypt.gensalt());
    }

    public boolean matches(String plain) {
        if (plain == null || plain.isEmpty()) return false;
        return BCrypt.checkpw(plain, this.encryptedValue);
    }

    public String getEncryptedValue() {
        return encryptedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;
        Password that = (Password) o;
        return encryptedValue.equals(that.encryptedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encryptedValue);
    }
}