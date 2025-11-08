package com.meditrack.usermanagement.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Password Value Object Tests")
class PasswordTest {

    @Test
    @DisplayName("Should create valid password and encrypt it")
    void testCreateValidPassword() {
        String plainPassword = "Password123!";
        Password password = new Password(plainPassword);

        assertNotNull(password);
        assertNotNull(password.getEncryptedValue());
        assertNotEquals(plainPassword, password.getEncryptedValue());
    }

    @Test
    @DisplayName("Should reject invalid passwords")
    void testRejectInvalidPassword() {
        assertThrows(IllegalArgumentException.class, () -> new Password(null));
        assertThrows(IllegalArgumentException.class, () -> new Password(""));
        assertThrows(IllegalArgumentException.class, () -> new Password("weak")); // zu kurz
        assertThrows(IllegalArgumentException.class, () -> new Password("password123!")); // kein Großbuchstabe
    }

    @Test
    @DisplayName("matches() should verify correct password")
    void testPasswordMatches() {
        String plainPassword = "Password123!";
        Password password = new Password(plainPassword);

        assertTrue(password.matches(plainPassword));
        assertFalse(password.matches("WrongPassword123!"));
        assertFalse(password.matches(null));
    }
}