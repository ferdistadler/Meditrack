package com.meditrack.usermanagement.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test-Suite für User Entity (Aggregate Root).
 *
 * Testet Factory Method, Business Logic, Validierung und Authentifizierung.
 */
@DisplayName("User Entity Tests")
class UserTest {

    private Email validEmail;
    private Password validPassword;
    private String validFirstName;
    private String validLastName;
    private Role validRole;

    @BeforeEach
    void setUp() {
        validEmail = new Email("user@example.com");
        validPassword = new Password("SecureP@ss123");
        validFirstName = "Max";
        validLastName = "Mustermann";
        validRole = Role.PATIENT;
    }

    // ==================== FACTORY METHOD TESTS (register) ====================

    @Test
    @DisplayName("Should create user with valid data using factory method")
    void testRegisterUserWithValidData() {
        // Act
        User user = User.register(validEmail, validPassword, validFirstName, validLastName, validRole);

        // Assert
        assertNotNull(user);
        assertEquals(validEmail, user.getEmail());
        assertEquals(validPassword, user.getPassword());
        assertEquals(validFirstName, user.getFirstName());
        assertEquals(validLastName, user.getLastName());
        assertEquals(validRole, user.getRole());
        assertNotNull(user.getCreatedAt());
        assertNull(user.getLastLogin(), "Last login should be null for new user");
    }

    @Test
    @DisplayName("CreatedAt should be set to current time on registration")
    void testCreatedAtSetOnRegistration() {
        // Arrange
        LocalDateTime before = LocalDateTime.now();

        // Act
        User user = User.register(validEmail, validPassword, validFirstName, validLastName, validRole);

        // Arrange
        LocalDateTime after = LocalDateTime.now();

        // Assert
        assertNotNull(user.getCreatedAt());
        assertTrue(user.getCreatedAt().isAfter(before) || user.getCreatedAt().isEqual(before));
        assertTrue(user.getCreatedAt().isBefore(after) || user.getCreatedAt().isEqual(after));
    }

    @Test
    @DisplayName("Should reject user registration with null email")
    void testRegisterWithNullEmail() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> User.register(null, validPassword, validFirstName, validLastName, validRole));
    }

    @Test
    @DisplayName("Should reject user registration with null password")
    void testRegisterWithNullPassword() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> User.register(validEmail, null, validFirstName, validLastName, validRole));
    }

    @Test
    @DisplayName("Should reject user registration with null role")
    void testRegisterWithNullRole() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> User.register(validEmail, validPassword, validFirstName, validLastName, null));
    }

    // ==================== NAME VALIDATION TESTS ====================

    @ParameterizedTest
    @DisplayName("Should accept valid first names")
    @ValueSource(strings = {
            "Max",
            "Anna",
            "Jean-Pierre",
            "O'Connor",
            "Müller",
            "Hans-Peter",
            "Mary Ann",
            "José",
            "François"
    })
    void testValidFirstNames(String firstName) {
        // Act
        User user = User.register(validEmail, validPassword, firstName, validLastName, validRole);

        // Assert
        assertNotNull(user);
        assertEquals(firstName, user.getFirstName());
    }

    @ParameterizedTest
    @DisplayName("Should accept valid last names")
    @ValueSource(strings = {
            "Mustermann",
            "Smith",
            "O'Brien",
            "Müller-Schmidt",
            "von Neumann",
            "D'Angelo",
            "McDonald"
    })
    void testValidLastNames(String lastName) {
        // Act
        User user = User.register(validEmail, validPassword, validFirstName, lastName, validRole);

        // Assert
        assertNotNull(user);
        assertEquals(lastName, user.getLastName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should reject null or empty first name")
    void testInvalidFirstNameNullOrEmpty(String invalidFirstName) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> User.register(validEmail, validPassword, invalidFirstName, validLastName, validRole));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should reject null or empty last name")
    void testInvalidLastNameNullOrEmpty(String invalidLastName) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> User.register(validEmail, validPassword, validFirstName, invalidLastName, validRole));
    }

    @ParameterizedTest
    @DisplayName("Should reject first names that are too short (<2 characters)")
    @ValueSource(strings = {"M", "A", "X"})
    void testFirstNameTooShort(String tooShortName) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> User.register(validEmail, validPassword, tooShortName, validLastName, validRole));
    }

    @Test
    @DisplayName("Should reject first name that is too long (>50 characters)")
    void testFirstNameTooLong() {
        // Arrange
        String tooLongName = "A".repeat(51);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> User.register(validEmail, validPassword, tooLongName, validLastName, validRole));
    }

    @ParameterizedTest
    @DisplayName("Should reject names with numbers")
    @ValueSource(strings = {
            "Max123",
            "Anna1",
            "Test99",
            "User2024"
    })
    void testNamesWithNumbers(String invalidName) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> User.register(validEmail, validPassword, invalidName, validLastName, validRole));
    }

    @ParameterizedTest
    @DisplayName("Should reject names with invalid special characters")
    @ValueSource(strings = {
            "Max@Mustermann",
            "Anna#Test",
            "User$Name",
            "Test!Name",
            "Name%Test",
            "User&Name"
    })
    void testNamesWithInvalidSpecialCharacters(String invalidName) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> User.register(validEmail, validPassword, invalidName, validLastName, validRole));
    }

    @Test
    @DisplayName("Should accept name with maximum valid length (50 characters)")
    void testNameWithMaximumLength() {
        // Arrange
        String maxLengthName = "A".repeat(50);

        // Act
        User user = User.register(validEmail, validPassword, maxLengthName, validLastName, validRole);

        // Assert
        assertNotNull(user);
        assertEquals(maxLengthName, user.getFirstName());
    }

    // ==================== AUTHENTICATE METHOD TESTS ====================

    @Test
    @DisplayName("authenticate() should return true with correct password")
    void testAuthenticateWithCorrectPassword() {
        // Arrange
        String plainPassword = "SecureP@ss123";
        Password password = new Password(plainPassword);
        User user = User.register(validEmail, password, validFirstName, validLastName, validRole);

        // Act
        boolean result = user.authenticate(plainPassword);

        // Assert
        assertTrue(result, "Authentication should succeed with correct password");
    }

    @Test
    @DisplayName("authenticate() should return false with incorrect password")
    void testAuthenticateWithIncorrectPassword() {
        // Arrange
        User user = User.register(validEmail, validPassword, validFirstName, validLastName, validRole);

        // Act
        boolean result = user.authenticate("WrongPassword123!");

        // Assert
        assertFalse(result, "Authentication should fail with incorrect password");
    }

    @Test
    @DisplayName("authenticate() should update lastLogin on successful authentication")
    void testAuthenticateUpdatesLastLogin() {
        // Arrange
        String plainPassword = "SecureP@ss123";
        Password password = new Password(plainPassword);
        User user = User.register(validEmail, password, validFirstName, validLastName, validRole);
        assertNull(user.getLastLogin(), "Last login should be null initially");

        LocalDateTime before = LocalDateTime.now();

        // Act
        boolean result = user.authenticate(plainPassword);

        LocalDateTime after = LocalDateTime.now();

        // Assert
        assertTrue(result);
        assertNotNull(user.getLastLogin(), "Last login should be set after successful authentication");
        assertTrue(user.getLastLogin().isAfter(before) || user.getLastLogin().isEqual(before));
        assertTrue(user.getLastLogin().isBefore(after) || user.getLastLogin().isEqual(after));
    }

    @Test
    @DisplayName("authenticate() should NOT update lastLogin on failed authentication")
    void testAuthenticateDoesNotUpdateLastLoginOnFailure() {
        // Arrange
        User user = User.register(validEmail, validPassword, validFirstName, validLastName, validRole);

        // Act
        boolean result = user.authenticate("WrongPassword123!");

        // Assert
        assertFalse(result);
        assertNull(user.getLastLogin(), "Last login should remain null after failed authentication");
    }

    @Test
    @DisplayName("authenticate() should handle null password gracefully")
    void testAuthenticateWithNullPassword() {
        // Arrange
        User user = User.register(validEmail, validPassword, validFirstName, validLastName, validRole);

        // Act
        boolean result = user.authenticate(null);

        // Assert
        assertFalse(result);
        assertNull(user.getLastLogin());
    }

    @Test
    @DisplayName("Multiple successful authentications should update lastLogin each time")
    void testMultipleAuthenticationsUpdateLastLogin() throws InterruptedException {
        // Arrange
        String plainPassword = "SecureP@ss123";
        Password password = new Password(plainPassword);
        User user = User.register(validEmail, password, validFirstName, validLastName, validRole);

        // Act - First authentication
        user.authenticate(plainPassword);
        LocalDateTime firstLogin = user.getLastLogin();
        assertNotNull(firstLogin);

        // Wait a bit to ensure time difference
        Thread.sleep(10);

        // Act - Second authentication
        user.authenticate(plainPassword);
        LocalDateTime secondLogin = user.getLastLogin();

        // Assert
        assertNotNull(secondLogin);
        assertTrue(secondLogin.isAfter(firstLogin) || secondLogin.isEqual(firstLogin));
    }

    // ==================== UPDATE PROFILE METHOD TESTS ====================

    @Test
    @DisplayName("updateProfile() should update first and last name")
    void testUpdateProfileWithValidNames() {
        // Arrange
        User user = User.register(validEmail, validPassword, validFirstName, validLastName, validRole);
        String newFirstName = "Anna";
        String newLastName = "Schmidt";

        // Act
        user.updateProfile(newFirstName, newLastName);

        // Assert
        assertEquals(newFirstName, user.getFirstName());
        assertEquals(newLastName, user.getLastName());
    }

    @Test
    @DisplayName("updateProfile() should reject invalid first name")
    void testUpdateProfileWithInvalidFirstName() {
        // Arrange
        User user = User.register(validEmail, validPassword, validFirstName, validLastName, validRole);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> user.updateProfile("M", "Schmidt"));
        assertThrows(IllegalArgumentException.class, () -> user.updateProfile("Max123", "Schmidt"));
        assertThrows(IllegalArgumentException.class, () -> user.updateProfile(null, "Schmidt"));
    }

    @Test
    @DisplayName("updateProfile() should reject invalid last name")
    void testUpdateProfileWithInvalidLastName() {
        // Arrange
        User user = User.register(validEmail, validPassword, validFirstName, validLastName, validRole);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> user.updateProfile("Anna", "S"));
        assertThrows(IllegalArgumentException.class, () -> user.updateProfile("Anna", "Schmidt123"));
        assertThrows(IllegalArgumentException.class, () -> user.updateProfile("Anna", null));
    }

    @Test
    @DisplayName("updateProfile() should not change names if validation fails")
    void testUpdateProfileRollbackOnFailure() {
        // Arrange
        User user = User.register(validEmail, validPassword, validFirstName, validLastName, validRole);
        String originalFirstName = user.getFirstName();
        String originalLastName = user.getLastName();

        // Act
        try {
            user.updateProfile("Invalid123", "Name456");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Assert - Namen sollten unverändert bleiben
        assertEquals(originalFirstName, user.getFirstName());
        assertEquals(originalLastName, user.getLastName());
    }

    // ==================== CHANGE PASSWORD METHOD TESTS ====================

    @Test
    @DisplayName("changePassword() should successfully change password with correct old password")
    void testChangePasswordSuccess() {
        // Arrange
        String oldPlainPassword = "OldP@ssw0rd";
        String newPlainPassword = "NewP@ssw0rd123";
        Password oldPassword = new Password(oldPlainPassword);
        User user = User.register(validEmail, oldPassword, validFirstName, validLastName, validRole);

        // Act
        user.changePassword(oldPlainPassword, newPlainPassword);

        // Assert - Altes Passwort sollte nicht mehr funktionieren
        assertFalse(user.authenticate(oldPlainPassword), "Old password should no longer work");
        // Neues Passwort sollte funktionieren
        assertTrue(user.authenticate(newPlainPassword), "New password should work");
    }

    @Test
    @DisplayName("changePassword() should reject change with incorrect old password")
    void testChangePasswordWithIncorrectOldPassword() {
        // Arrange
        User user = User.register(validEmail, validPassword, validFirstName, validLastName, validRole);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> user.changePassword("WrongOldPassword!", "NewP@ssw0rd123"));
    }

    @Test
    @DisplayName("changePassword() should reject invalid new password")
    void testChangePasswordWithInvalidNewPassword() {
        // Arrange
        String oldPlainPassword = "OldP@ssw0rd";
        Password oldPassword = new Password(oldPlainPassword);
        User user = User.register(validEmail, oldPassword, validFirstName, validLastName, validRole);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> user.changePassword(oldPlainPassword, "weak"));  // Zu schwach
    }

    @Test
    @DisplayName("changePassword() should not change password if new password is invalid")
    void testChangePasswordRollbackOnInvalidNewPassword() {
        // Arrange
        String oldPlainPassword = "OldP@ssw0rd";
        Password oldPassword = new Password(oldPlainPassword);
        User user = User.register(validEmail, oldPassword, validFirstName, validLastName, validRole);

        // Act
        try {
            user.changePassword(oldPlainPassword, "weak");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Assert - Altes Passwort sollte noch funktionieren
        assertTrue(user.authenticate(oldPlainPassword), "Old password should still work after failed change");
    }

    // ==================== ROLE TESTS ====================

    @Test
    @DisplayName("User should be created with correct role")
    void testUserRole() {
        // Act
        User patientUser = User.register(validEmail, validPassword, validFirstName, validLastName, Role.PATIENT);
        User doctorUser = User.register(new Email("doctor@example.com"), validPassword, "Dr.", "House", Role.DOCTOR);

        // Assert
        assertEquals(Role.PATIENT, patientUser.getRole());
        assertEquals(Role.DOCTOR, doctorUser.getRole());
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Should handle special characters in names correctly")
    void testSpecialCharactersInNames() {
        // Act
        User user1 = User.register(validEmail, validPassword, "Jean-Pierre", "D'Angelo", validRole);
        User user2 = User.register(new Email("user2@test.com"), validPassword, "O'Connor", "Müller-Schmidt", validRole);

        // Assert
        assertEquals("Jean-Pierre", user1.getFirstName());
        assertEquals("D'Angelo", user1.getLastName());
        assertEquals("O'Connor", user2.getFirstName());
        assertEquals("Müller-Schmidt", user2.getLastName());
    }
}