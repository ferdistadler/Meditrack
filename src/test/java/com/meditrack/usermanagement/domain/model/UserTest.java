package com.meditrack.usermanagement.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Entity Tests")
class UserTest {

    @Test
    @DisplayName("Should create user with valid data")
    void testRegisterUserWithValidData() {
        Email email = new Email("user@example.com");
        Password password = new Password("SecureP@ss123");

        User user = User.register(email, password, "Max", "Mustermann", Role.PATIENT);

        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals("Max", user.getFirstName());
        assertEquals("Mustermann", user.getLastName());
        assertEquals(Role.PATIENT, user.getRole());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    @DisplayName("Should reject invalid user data")
    void testRegisterWithInvalidData() {
        Email email = new Email("user@example.com");
        Password password = new Password("SecureP@ss123");

        assertThrows(IllegalArgumentException.class,
                () -> User.register(null, password, "Max", "Mustermann", Role.PATIENT)); // null email
        assertThrows(IllegalArgumentException.class,
                () -> User.register(email, null, "Max", "Mustermann", Role.PATIENT)); // null password
        assertThrows(IllegalArgumentException.class,
                () -> User.register(email, password, "", "Mustermann", Role.PATIENT)); // leerer Name
        assertThrows(IllegalArgumentException.class,
                () -> User.register(email, password, "Max", "Mustermann", null)); // null role
    }

    @Test
    @DisplayName("Should authenticate user with correct password")
    void testAuthentication() {
        Email email = new Email("user@example.com");
        String plainPassword = "SecureP@ss123";
        Password password = new Password(plainPassword);

        User user = User.register(email, password, "Max", "Mustermann", Role.PATIENT);

        assertTrue(user.authenticate(plainPassword));
        assertFalse(user.authenticate("WrongPassword123!"));
        assertNotNull(user.getLastLogin()); // lastLogin wurde gesetzt
    }
}