package com.meditrack.usermanagement.domain.model;

/**
 * Enum für Benutzerrollen im System.
 *
 * Definiert die verschiedenen Rollen mit ihren Berechtigungen.
 */
public enum Role {

    /**
     * Patient - Kann eigene Daten einsehen und verwalten
     */
    PATIENT("Patient", "Kann eigene medizinische Daten einsehen"),

    /**
     * Arzt - Kann Patienten behandeln und Daten verwalten
     */
    DOCTOR("Arzt", "Kann Patienten behandeln und medizinische Daten verwalten"),

    /**
     * Apotheker - Kann Rezepte einsehen und verwalten
     */
    PHARMACIST("Apotheker", "Kann Rezepte einsehen und Medikamente verwalten"),

    /**
     * Administrator - Hat volle Systemrechte
     */
    ADMIN("Administrator", "Hat volle Systemrechte");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Prüft ob diese Rolle Patientendaten einsehen darf.
     */
    public boolean canViewPatientData() {
        return this == DOCTOR || this == ADMIN;
    }

    /**
     * Prüft ob diese Rolle Behandlungen durchführen darf.
     */
    public boolean canTreatPatients() {
        return this == DOCTOR;
    }

    /**
     * Prüft ob diese Rolle administrative Rechte hat.
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }
}