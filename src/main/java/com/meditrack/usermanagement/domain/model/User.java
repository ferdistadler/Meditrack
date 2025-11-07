package com.meditrack.usermanagement.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * User Entity (Aggregate Root).
 *
 * Zentrale Entität für Benutzerverwaltung mit allen Geschäftsregeln.
 *
 * DDD Patterns:
 * - Aggregate Root: Einstiegspunkt für alle User-Operationen
 * - Factory Method: register() statt public Constructor
 * - Business Logic in Entity: authenticate(), updateProfile(), changePassword()
 * - Value Objects: Email und Password kapseln Validierungslogik
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "email", unique = true, nullable = false))
    })
    private Email email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "encryptedValue", column = @Column(name = "password", nullable = false))
    })
    private Password password;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // Regex für Namen-Validierung
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-zÄÖÜäöüß\\s\\-']{2,50}$");

    /**
     * JPA Constructor (protected für DDD).
     * Verwendet nur von JPA/Hibernate.
     */
    protected User() {
        // Für JPA/Hibernate
    }

    /**
     * Private Constructor für Factory Method.
     * Erzwingt Verwendung von register().
     */
    private User(Email email, Password password, String firstName, String lastName, Role role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.lastLogin = null;
    }

    /**
     * Factory Method zur User-Registrierung (DDD Pattern).
     *
     * Erstellt einen neuen User mit Validierung aller Felder.
     *
     * @param email Email Value Object
     * @param password Password Value Object
     * @param firstName Vorname
     * @param lastName Nachname
     * @param role Benutzerrolle
     * @return Neu erstellter User
     * @throws IllegalArgumentException bei ungültigen Daten
     */
    public static User register(Email email, Password password, String firstName, String lastName, Role role) {
        // Validierung
        validateEmail(email);
        validatePassword(password);
        validateName(firstName, "Vorname");
        validateName(lastName, "Nachname");
        validateRole(role);

        return new User(email, password, firstName, lastName, role);
    }

    /**
     * Authentifiziert den User mit einem Passwort.
     * Aktualisiert lastLogin bei erfolgreichem Login.
     *
     * @param plainPassword Das Klartext-Passwort
     * @return true wenn Authentifizierung erfolgreich, false sonst
     */
    public boolean authenticate(String plainPassword) {
        boolean success = password.matches(plainPassword);

        if (success) {
            this.lastLogin = LocalDateTime.now();
        }

        return success;
    }

    /**
     * Aktualisiert das Benutzerprofil (Vor- und Nachname).
     *
     * @param firstName Neuer Vorname
     * @param lastName Neuer Nachname
     * @throws IllegalArgumentException bei ungültigen Namen
     */
    public void updateProfile(String firstName, String lastName) {
        validateName(firstName, "Vorname");
        validateName(lastName, "Nachname");

        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Ändert das Passwort des Users.
     *
     * @param oldPassword Altes Passwort (zur Verifikation)
     * @param newPlainPassword Neues Klartext-Passwort
     * @throws IllegalArgumentException wenn altes Passwort falsch oder neues ungültig
     */
    public void changePassword(String oldPassword, String newPlainPassword) {
        // Altes Passwort prüfen
        if (!password.matches(oldPassword)) {
            throw new IllegalArgumentException("Altes Passwort ist falsch");
        }

        // Neues Passwort erstellen (validiert automatisch)
        Password newPassword = new Password(newPlainPassword);

        // Passwort ändern
        this.password = newPassword;
    }

    // ==================== VALIDATION METHODS ====================

    private static void validateEmail(Email email) {
        if (email == null) {
            throw new IllegalArgumentException("Email darf nicht null sein");
        }
    }

    private static void validatePassword(Password password) {
        if (password == null) {
            throw new IllegalArgumentException("Passwort darf nicht null sein");
        }
    }

    private static void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " darf nicht null oder leer sein");
        }

        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(
                    fieldName + " ist ungültig. " +
                            "Erlaubt sind Buchstaben, Leerzeichen, Bindestriche und Apostrophe (2-50 Zeichen)."
            );
        }
    }

    private static void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Rolle darf nicht null sein");
        }
    }

    // ==================== GETTERS ====================

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    /**
     * Gibt den vollständigen Namen zurück.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // ==================== EQUALS & HASHCODE ====================

    /**
     * Entities sind gleich wenn ihre IDs gleich sind (DDD).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // Entity Equality based on ID
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        // Konstanter Hash für Entities (wichtig für Hibernate)
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, email=%s, name=%s %s, role=%s]",
                id, email, firstName, lastName, role);
    }
}