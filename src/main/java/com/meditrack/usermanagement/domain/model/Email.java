package com.meditrack.usermanagement.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object für E-Mail-Adressen.
 *
 * Immutable und validiert gemäß RFC 5322 (vereinfacht).
 *
 * DDD Patterns:
 * - Value Object: Keine eigene Identität, definiert durch Wert
 * - Immutable: Keine Setter, alle Felder final
 * - Self-validating: Validierung im Konstruktor
 */
public final class Email {

    private static final int MAX_LENGTH = 254;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private final String value;

    /**
     * Erstellt eine neue Email mit Validierung.
     *
     * @param value Die E-Mail-Adresse als String
     * @throws IllegalArgumentException wenn die E-Mail ungültig ist
     */
    public Email(String value) {
        this.value = validate(value);
    }

    /**
     * Validiert die E-Mail-Adresse.
     *
     * Prüft:
     * - Nicht null oder leer
     * - Gültiges Format (Regex)
     * - Maximale Länge (254 Zeichen)
     * - Keine aufeinanderfolgenden Punkte
     * - Punkt nicht am Anfang/Ende des lokalen Teils
     */
    private String validate(String email) {
        // Null/Empty Check
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email darf nicht null oder leer sein");
        }

        // Trim whitespace
        email = email.trim();

        // Längen-Check
        if (email.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Email darf maximal %d Zeichen lang sein", MAX_LENGTH)
            );
        }

        // Format-Check mit Regex
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Ungültiges Email-Format");
        }

        // Aufeinanderfolgende Punkte prüfen
        if (email.contains("..")) {
            throw new IllegalArgumentException("Email darf keine aufeinanderfolgenden Punkte enthalten");
        }

        // Punkt am Anfang/Ende des lokalen Teils prüfen
        String localPart = email.split("@")[0];
        if (localPart.startsWith(".") || localPart.endsWith(".")) {
            throw new IllegalArgumentException(
                    "Lokaler Teil der Email darf nicht mit einem Punkt beginnen oder enden"
            );
        }

        return email;
    }

    /**
     * Gibt den E-Mail-Wert zurück.
     *
     * @return Die E-Mail-Adresse als String
     */
    public String getValue() {
        return value;
    }

    /**
     * Value Objects sind gleich, wenn ihre Werte gleich sind.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}