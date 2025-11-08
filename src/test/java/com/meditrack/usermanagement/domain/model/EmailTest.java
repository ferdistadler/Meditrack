package com.meditrack.usermanagement.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email Value Object Tests")
class EmailTest {

    @Test
    @DisplayName("Should create valid email")
    void testCreateValidEmail() {
        Email email = new Email("max.mustermann@example.com");
        assertNotNull(email);
        assertEquals("max.mustermann@example.com", email.getValue());
    }

    @Test
    @DisplayName("Should reject null or empty email")
    void testRejectInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
        assertThrows(IllegalArgumentException.class, () -> new Email(""));
        assertThrows(IllegalArgumentException.class, () -> new Email("invalid-email"));
    }

    @Test
    @DisplayName("Two emails with same value should be equal")
    void testEmailEquality() {
        Email email1 = new Email("user@example.com");
        Email email2 = new Email("user@example.com");
        assertEquals(email1, email2);
    }
}