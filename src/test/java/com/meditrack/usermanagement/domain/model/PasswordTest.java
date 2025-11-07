package com.meditrack.usermanagement.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test-Suite für Password Value Object.
 *
 * Testet Validierung, Verschlüsselung, matches() Methode und Immutability.
 */
@DisplayName("Password Value Object Tests")
class PasswordTest {

    // ==================== HAPPY PATH TESTS ====================

    @Test
    @DisplayName("Should create valid password with all required character types")
    void testCreateValidPassword() {
        // Arrange & Act
        Password password = new Password("Password123!");

        // Assert
        assertNotNull(password);
        assertNotNull(password.getEncryptedValue());
    }

    @ParameterizedTest
    @DisplayName("Should accept all valid password formats")
    @ValueSource(strings = {
            "Password123!",
            "SecureP@ss2024",
            "MyStr0ng#Pass",
            "Abcdef1!",        // Minimale Länge (8)
            "P@ssw0rd",
            "Test1234!",
            "Valid#Pass99",
            "Str0ng$Password",
            "MyP@ss123word"
    })
    void testValidPasswordFormats(String validPassword) {
        // Act
        Password password = new Password(validPassword);

        // Assert
        assertNotNull(password);
        assertNotNull(password.getEncryptedValue());
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Should accept password with minimum length (8 characters)")
    void testMinimumLengthPassword() {
        // Arrange - Exakt 8 Zeichen mit allen erforderlichen Typen
        String minPassword = "Abcd123!";

        // Act
        Password password = new Password(minPassword);

        // Assert
        assertNotNull(password);
    }

    @Test
    @DisplayName("Should accept password with maximum length (128 characters)")
    void testMaximumLengthPassword() {
        // Arrange - 128 Zeichen mit allen erforderlichen Typen
        String maxPassword = "A" + "a".repeat(120) + "1234567!"; // 1 + 120 + 7 = 128

        // Act
        Password password = new Password(maxPassword);

        // Assert
        assertNotNull(password);
    }

    @Test
    @DisplayName("Should accept password with all allowed special characters")
    void testPasswordWithAllSpecialCharacters() {
        // Arrange
        String[] specialChars = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "-", "=", "[", "]", "{", "}", "|", ";", ":", ",", ".", "<", ">", "?"};

        // Act & Assert
        for (String specialChar : specialChars) {
            String password = "Pass123" + specialChar;
            assertDoesNotThrow(() -> new Password(password),
                    "Password with special character " + specialChar + " should be valid");
        }
    }

    @Test
    @DisplayName("Should accept password with multiple special characters")
    void testPasswordWithMultipleSpecialCharacters() {
        // Act
        Password password = new Password("P@ssw0rd!#$");

        // Assert
        assertNotNull(password);
    }

    @Test
    @DisplayName("Should accept password with multiple uppercase letters")
    void testPasswordWithMultipleUppercase() {
        // Act
        Password password = new Password("PASSWord123!");

        // Assert
        assertNotNull(password);
    }

    // ==================== NEGATIVE TESTS ====================

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should reject null or empty password")
    void testNullOrEmptyPassword(String invalidPassword) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Password(invalidPassword));
    }

    @Test
    @DisplayName("Should reject password that is too short (<8 characters)")
    void testPasswordTooShort() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Password("Pass1!"));   // 6 Zeichen
        assertThrows(IllegalArgumentException.class, () -> new Password("Abc12!"));   // 6 Zeichen
        assertThrows(IllegalArgumentException.class, () -> new Password("Test1!"));   // 6 Zeichen
    }

    @Test
    @DisplayName("Should reject password that is too long (>128 characters)")
    void testPasswordTooLong() {
        // Arrange - 129 Zeichen
        String tooLongPassword = "A" + "a".repeat(121) + "1234567!"; // 1 + 121 + 7 = 129

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Password(tooLongPassword));
    }

    @Test
    @DisplayName("Should reject password without uppercase letter")
    void testPasswordWithoutUppercase() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Password("password123!"));
        assertThrows(IllegalArgumentException.class, () -> new Password("mypass1!"));
    }

    @Test
    @DisplayName("Should reject password without lowercase letter")
    void testPasswordWithoutLowercase() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Password("PASSWORD123!"));
        assertThrows(IllegalArgumentException.class, () -> new Password("MYPASS1!"));
    }

    @Test
    @DisplayName("Should reject password without digit")
    void testPasswordWithoutDigit() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Password("Password!"));
        assertThrows(IllegalArgumentException.class, () -> new Password("MyPass!"));
    }

    @Test
    @DisplayName("Should reject password without special character")
    void testPasswordWithoutSpecialCharacter() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Password("Password123"));
        assertThrows(IllegalArgumentException.class, () -> new Password("MyPass123"));
    }

    @Test
    @DisplayName("Should reject password with whitespace")
    void testPasswordWithWhitespace() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Password("Pass word1!"));
        assertThrows(IllegalArgumentException.class, () -> new Password("Password 123!"));
        assertThrows(IllegalArgumentException.class, () -> new Password(" Password123!"));
        assertThrows(IllegalArgumentException.class, () -> new Password("Password123! "));
    }

    @ParameterizedTest
    @DisplayName("Should reject passwords missing one required character type")
    @ValueSource(strings = {
            "password123!",    // Kein Großbuchstabe
            "PASSWORD123!",    // Kein Kleinbuchstabe
            "Password!",       // Keine Ziffer
            "Password123"      // Kein Sonderzeichen
    })
    void testPasswordMissingRequiredCharacterType(String invalidPassword) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Password(invalidPassword));
    }

    // ==================== ENCRYPTION TESTS ====================

    @Test
    @DisplayName("Password should be encrypted (not stored as plaintext)")
    void testPasswordIsEncrypted() {
        // Arrange
        String plainPassword = "MySecureP@ss123";

        // Act
        Password password = new Password(plainPassword);

        // Assert
        assertNotNull(password.getEncryptedValue());
        assertNotEquals(plainPassword, password.getEncryptedValue());
        assertTrue(password.getEncryptedValue().startsWith("$2a$") ||
                        password.getEncryptedValue().startsWith("$2b$"),
                "Encrypted password should be BCrypt format");
    }

    @Test
    @DisplayName("Same plaintext should produce different encrypted values (salt)")
    void testDifferentSaltsProduceDifferentHashes() {
        // Arrange
        String plainPassword = "MySecureP@ss123";

        // Act
        Password password1 = new Password(plainPassword);
        Password password2 = new Password(plainPassword);

        // Assert
        assertNotEquals(password1.getEncryptedValue(), password2.getEncryptedValue(),
                "BCrypt should use different salts for same password");
    }

    // ==================== MATCHES() METHOD TESTS ====================

    @Test
    @DisplayName("matches() should return true for correct password")
    void testMatchesWithCorrectPassword() {
        // Arrange
        String plainPassword = "MySecureP@ss123";
        Password password = new Password(plainPassword);

        // Act
        boolean result = password.matches(plainPassword);

        // Assert
        assertTrue(result, "matches() should return true for correct password");
    }

    @Test
    @DisplayName("matches() should return false for incorrect password")
    void testMatchesWithIncorrectPassword() {
        // Arrange
        String plainPassword = "MySecureP@ss123";
        Password password = new Password(plainPassword);

        // Act
        boolean result = password.matches("WrongPassword123!");

        // Assert
        assertFalse(result, "matches() should return false for incorrect password");
    }

    @Test
    @DisplayName("matches() should return false for similar but not identical password")
    void testMatchesWithSimilarPassword() {
        // Arrange
        Password password = new Password("MySecureP@ss123");

        // Act & Assert
        assertFalse(password.matches("MySecureP@ss124"));  // Letzte Ziffer anders
        assertFalse(password.matches("mySecureP@ss123"));  // Groß-/Kleinschreibung
        assertFalse(password.matches("MySecureP@ss123 ")); // Trailing space
    }

    @Test
    @DisplayName("matches() should handle null gracefully")
    void testMatchesWithNull() {
        // Arrange
        Password password = new Password("MySecureP@ss123");

        // Act
        boolean result = password.matches(null);

        // Assert
        assertFalse(result, "matches() should return false for null");
    }

    @Test
    @DisplayName("matches() should handle empty string gracefully")
    void testMatchesWithEmptyString() {
        // Arrange
        Password password = new Password("MySecureP@ss123");

        // Act
        boolean result = password.matches("");

        // Assert
        assertFalse(result, "matches() should return false for empty string");
    }

    // ==================== IMMUTABILITY TESTS ====================

    @Test
    @DisplayName("Password should be immutable")
    void testPasswordImmutability() {
        // Arrange
        String plainPassword = "MySecureP@ss123";
        Password password = new Password(plainPassword);
        String originalHash = password.getEncryptedValue();

        // Act - Versuche das Original-Passwort zu ändern
        plainPassword = "ChangedPassword1!";

        // Assert - Password sollte unverändert bleiben
        assertEquals(originalHash, password.getEncryptedValue());
    }

    // ==================== EQUALITY TESTS ====================

    @Test
    @DisplayName("Two Password objects with same plaintext should not be equal (different salts)")
    void testPasswordInequality() {
        // Arrange
        String plainPassword = "MySecureP@ss123";
        Password password1 = new Password(plainPassword);
        Password password2 = new Password(plainPassword);

        // Assert - Unterschiedliche Salts = unterschiedliche Hashes = nicht gleich
        assertNotEquals(password1, password2);
        assertNotEquals(password1.hashCode(), password2.hashCode());
    }

    @Test
    @DisplayName("Password should not be equal to null")
    void testPasswordNotEqualToNull() {
        // Arrange
        Password password = new Password("MySecureP@ss123");

        // Assert
        assertNotEquals(null, password);
    }
}