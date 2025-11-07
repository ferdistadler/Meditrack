package com.meditrack.usermanagement.domain.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Objects;

/**
 * Value Object für Passwörter.
 *
 * Speichert Passwörter verschlüsselt mit BCrypt.
 * Immutable und self-validating.
 *
 * DDD Patterns:
 * - Value Object: Keine eigene Identität
 * - Immutable: Keine Setter, alle Felder final
 * - Encapsulation: Plaintext wird nie gespeichert
 */
public final class Password {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;
    private static final int BCRYPT_STRENGTH = 12;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCRYPT_STRENGTH);

    private final String encryptedValue;

    /**
     * Erstellt ein neues Password aus Plaintext.
     * Validiert und verschlüsselt das Passwort.
     *
     * @param plainPassword Das Klartext-Passwort
     * @throws IllegalArgumentException wenn das Passwort ungültig ist
     */
    public Password(String plainPassword) {
        validate(plainPassword);
        this.encryptedValue = encrypt(plainPassword);
    }

    /**
     * Validiert das Passwort gegen alle Regeln.
     *
     * Prüft:
     * - Nicht null oder leer
     * - Länge zwischen 8 und 128 Zeichen
     * - Mindestens 1 Großbuchstabe
     * - Mindestens 1 Kleinbuchstabe
     * - Mindestens 1 Ziffer
     * - Mindestens 1 Sonderzeichen
     * - Keine Leerzeichen
     */
    private void validate(String password) {
        // Null/Empty Check
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Passwort darf nicht null oder leer sein");
        }

        // Längen-Check
        if (password.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Passwort muss mindestens %d Zeichen lang sein", MIN_LENGTH)
            );
        }

        if (password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Passwort darf maximal %d Zeichen lang sein", MAX_LENGTH)
            );
        }

        // Leerzeichen-Check
        if (password.contains(" ")) {
            throw new IllegalArgumentException("Passwort darf keine Leerzeichen enthalten");
        }

        // Großbuchstaben-Check
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Passwort muss mindestens einen Großbuchstaben enthalten");
        }

        // Kleinbuchstaben-Check
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Passwort muss mindestens einen Kleinbuchstaben enthalten");
        }

        // Ziffern-Check
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Passwort muss mindestens eine Ziffer enthalten");
        }

        // Sonderzeichen-Check
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;:,.<>?].*")) {
            throw new IllegalArgumentException("Passwort muss mindestens ein Sonderzeichen enthalten");
        }
    }

    /**
     * Verschlüsselt das Passwort mit BCrypt.
     *
     * @param plainPassword Das Klartext-Passwort
     * @return Der BCrypt-Hash
     */
    private String encrypt(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    /**
     * Prüft ob ein Plaintext-Passwort mit diesem Password übereinstimmt.
     *
     * @param plainPassword Das zu prüfende Klartext-Passwort
     * @return true wenn das Passwort übereinstimmt, false sonst
     */
    public boolean matches(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            return false;
        }
        return encoder.matches(plainPassword, encryptedValue);
    }

    /**
     * Gibt den verschlüsselten Wert zurück (für Persistenz).
     *
     * @return Der BCrypt-Hash
     */
    public String getEncryptedValue() {
        return encryptedValue;
    }

    /**
     * Password Objects sind ungleich, selbst bei gleichem Plaintext,
     * da BCrypt unterschiedliche Salts verwendet.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(encryptedValue, password.encryptedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encryptedValue);
    }

    /**
     * Gibt NICHT das Passwort zurück (Sicherheit).
     */
    @Override
    public String toString() {
        return "[PROTECTED]";
    }
}