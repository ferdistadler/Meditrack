package com.meditrack.usermanagement.domain.model;

import java.util.Objects;

public class Email {
    private final String value;

    public Email(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!value.equals(value.trim())) {
            throw new IllegalArgumentException("Email contains leading or trailing spaces");
        }
        if (value.length() > 254) {
            throw new IllegalArgumentException("Email too long");
        }
        if (!value.matches("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return value.equals(email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}