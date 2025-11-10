# Meditrack

# Aufgabenbearbeitung: Code-QualitÃ¤t und Frontend-Entwicklung

## 1. Metriken-Analyse und Code-Optimierung

### 1.1 Verwendete Tools

**Tool:** IntelliJ IDEA CodeMetrics Plugin  
**Grund fÃ¼r die Wahl:** 
- Direkte Integration in die verwendete IDE
- Keine zusÃ¤tzliche Server-Installation erforderlich
- Ausreichend fÃ¼r ProjektgrÃ¶ÃŸe und Aufgabenstellung
- Schnelle Analyse und direkte Navigation zu problematischen Code-Stellen

**Analysedatum:** 09.11.2025  
**Analysierter Scope:** Gesamtes Projekt (inkl. Test-Quellen)  
**Metrics Profile:** Complexity Metrics (System)

---

### 1.2 Gemessene Metriken

#### ErklÃ¤rung der verwendeten Metriken:

| Metrik | Bedeutung | Zielwert |
|--------|-----------|----------|
| **v(G)** - Cyclomatic Complexity | Anzahl unabhÃ¤ngiger AusfÃ¼hrungspfade durch eine Methode | â‰¤ 10 |
| **CogC** - Cognitive Complexity | Wie schwer ist der Code zu verstehen? | â‰¤ 15 |
| **WMC** - Weighted Methods per Class | Summe aller KomplexitÃ¤ten in einer Klasse | â‰¤ 50 |
| **OCavg** - Average Operation Complexity | Durchschnittliche KomplexitÃ¤t pro Methode | â‰¤ 5 |
| **OCmax** - Maximum Operation Complexity | HÃ¶chste KomplexitÃ¤t einer Methode in der Klasse | â‰¤ 10 |

#### Projekt-GesamtÃ¼bersicht:

```
Projekt: Meditrack
â”œâ”€â”€ Durchschnittliche Complexity (v(G)avg): 1,92 âœ…
â”œâ”€â”€ Gesamte Complexity (v(G)tot): 69
â”œâ”€â”€ Anzahl Klassen: 7 (davon 3 Test-Klassen)
â””â”€â”€ Package: com.meditrack.usermanagement.domain.model
```

**Bewertung:** Die durchschnittliche KomplexitÃ¤t von **1,92** ist ausgezeichnet (Ziel: < 5). Das Projekt hat eine gute Gesamtstruktur.

---

### 1.3 Detaillierte Klassen-Analyse

#### Ãœbersicht der Klassen:

| Klasse | Ã˜ Complexity | Max Complexity | WMC | Anzahl Methoden | Bewertung |
|--------|--------------|----------------|-----|-----------------|-----------|
| **Password** | 2,57 | **9** | 18 | 7 | âš ï¸ Optimierungsbedarf |
| **Email** | 2,20 | 5 | 11 | 5 | âš¡ Akzeptabel |
| **User** | 1,47 | 3 | 22 | 15 | âœ… Gut |
| **PasswordTest** | 1,00 | 1 | 3 | 3 | âœ… Sehr gut |
| **EmailTest** | 1,00 | 1 | 3 | 3 | âœ… Sehr gut |
| **UserTest** | 1,00 | 1 | 3 | 3 | âœ… Sehr gut |
| **Role** | n/a | n/a | 0 | 0 | âœ… Enum (keine Methoden) |

---

### 1.4 Identifizierte Schwachstellen

#### ðŸ”´ Kritisch: Password.validate(String)

**Gemessene Werte:**
- Cognitive Complexity: **9**
- Cyclomatic Complexity: **10** (Grenzwert erreicht!)
- Essential Complexity: **9**

**Problem:**  
Die Methode hat 10 verschiedene AusfÃ¼hrungspfade, was sie schwer testbar und wartbar macht. Dies deutet auf viele verschachtelte Bedingungen oder eine lange if-else-Kette hin.

**Vermuteter Code (vor Optimierung):**
```java
public static void validate(String password) {
    if (password == null) {
        throw new IllegalArgumentException("Password cannot be null");
    }
    if (password.length() < 8) {
        throw new IllegalArgumentException("Password too short");
    }
    if (password.length() > 128) {
        throw new IllegalArgumentException("Password too long");
    }
    if (!password.matches(".*[A-Z].*")) {
        throw new IllegalArgumentException("Password must contain uppercase");
    }
    if (!password.matches(".*[a-z].*")) {
        throw new IllegalArgumentException("Password must contain lowercase");
    }
    if (!password.matches(".*[0-9].*")) {
        throw new IllegalArgumentException("Password must contain digit");
    }
    if (!password.matches(".*[!@#$%^&*].*")) {
        throw new IllegalArgumentException("Password must contain special char");
    }
}
```

---

#### âš ï¸ Moderat: User.validateName(String)

**Gemessene Werte:**
- Cognitive Complexity: **2**
- Cyclomatic Complexity: **6**
- Irreducible Complexity: **5** (viele Verzweigungen)

**Problem:**  
Die Methode prÃ¼ft mehrere Bedingungen in einer einzigen komplexen if-Anweisung, was die Lesbarkeit reduziert.

---

#### âš¡ Optimierungspotential: Email.Email(String)

**Gemessene Werte:**
- Cognitive Complexity: **5**
- Cyclomatic Complexity: **6**

**Problem:**  
Der Konstruktor fÃ¼hrt komplexe Email-Validierung durch. Validierungslogik sollte extrahiert werden.

---

### 1.5 LLM-gestÃ¼tzte OptimierungsvorschlÃ¤ge

#### Optimierung 1: Password.validate() - Modularisierung

**LLM-Empfehlung:** Die Methode in kleinere, fokussierte Methoden aufteilen (Extract Method Refactoring).

**Optimierter Code:**
```java
public static void validate(String password) {
    validateNotNull(password);
    validateLength(password);
    validateComplexityRequirements(password);
}

private static void validateNotNull(String password) {
    if (password == null) {
        throw new IllegalArgumentException("Password cannot be null");
    }
}

private static void validateLength(String password) {
    if (password.length() < 8) {
        throw new IllegalArgumentException(
            "Password must be at least 8 characters long"
        );
    }
    if (password.length() > 128) {
        throw new IllegalArgumentException(
            "Password must not exceed 128 characters"
        );
    }
}

private static void validateComplexityRequirements(String password) {
    List<String> errors = new ArrayList<>();
    
    if (!hasUpperCase(password)) {
        errors.add("uppercase letter");
    }
    if (!hasLowerCase(password)) {
        errors.add("lowercase letter");
    }
    if (!hasDigit(password)) {
        errors.add("digit");
    }
    if (!hasSpecialCharacter(password)) {
        errors.add("special character");
    }
    
    if (!errors.isEmpty()) {
        throw new IllegalArgumentException(
            "Password must contain: " + String.join(", ", errors)
        );
    }
}

private static boolean hasUpperCase(String password) {
    return password.matches(".*[A-Z].*");
}

private static boolean hasLowerCase(String password) {
    return password.matches(".*[a-z].*");
}

private static boolean hasDigit(String password) {
    return password.matches(".*[0-9].*");
}

private static boolean hasSpecialCharacter(String password) {
    return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
}
```

**Vorteile:**
- âœ… Cyclomatic Complexity pro Methode: **â‰¤ 3** (statt 10)
- âœ… Bessere Lesbarkeit und Wartbarkeit
- âœ… Einfachere Unit-Tests (jede Methode einzeln testbar)
- âœ… Bessere Fehlermeldungen (alle fehlenden Anforderungen auf einmal)
- âœ… DRY-Prinzip: Wiederverwendbare Helper-Methoden

**Erwartete neue Metriken nach Refactoring:**
- `validate()`: v(G) = **1** (nur Methodenaufrufe)
- `validateNotNull()`: v(G) = **2** (eine Bedingung)
- `validateLength()`: v(G) = **3** (zwei Bedingungen)
- `validateComplexityRequirements()`: v(G) = **5** (vier Bedingungen, aber klar strukturiert)

---

#### Optimierung 2: User.validateName() - Validator-Pattern

**LLM-Empfehlung:** Wiederverwendbare Validator-Utility-Klasse erstellen.

**Optimierter Code:**
```java
// Neue Validator-Utility-Klasse
public class StringValidator {
    
    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                fieldName + " cannot be null or blank"
            );
        }
    }
    
    public static void requireLengthBetween(
        String value, 
        int min, 
        int max, 
        String fieldName
    ) {
        if (value.length() < min || value.length() > max) {
            throw new IllegalArgumentException(
                fieldName + " must be between " + min + " and " + max + " characters"
            );
        }
    }
    
    public static void requirePattern(
        String value, 
        String pattern, 
        String fieldName, 
        String requirement
    ) {
        if (!value.matches(pattern)) {
            throw new IllegalArgumentException(
                fieldName + " must " + requirement
            );
        }
    }
}

// Vereinfachte validateName() Methode in User-Klasse
private void validateName(String name) {
    StringValidator.requireNonBlank(name, "Name");
    StringValidator.requireLengthBetween(name, 2, 50, "Name");
    StringValidator.requirePattern(
        name, 
        "^[a-zA-Z\\s]+$", 
        "Name", 
        "contain only letters and spaces"
    );
}
```

**Vorteile:**
- âœ… Cyclomatic Complexity: **1** (nur Methodenaufrufe)
- âœ… Wiederverwendbarkeit: Validator kann in Email, User, etc. genutzt werden
- âœ… Konsistente Fehlermeldungen
- âœ… Reduziert Code-Duplizierung (DRY-Prinzip)

---

#### Optimierung 3: Email-Konstruktor vereinfachen

**LLM-Empfehlung:** Validierungslogik extrahieren und Pattern-Kompilierung optimieren.

**Optimierter Code:**
```java
public class Email {
    // Pattern als Konstante fÃ¼r bessere Performance
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private final String value;
    
    public Email(String value) {
        this.value = validateAndNormalize(value);
    }
    
    private String validateAndNormalize(String email) {
        requireNonNull(email);
        String normalized = email.trim().toLowerCase();
        requireValidFormat(normalized);
        return normalized;
    }
    
    private void requireNonNull(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
    }
    
    private void requireValidFormat(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
}
```

**Vorteile:**
- âœ… Konstruktor Complexity: **1** (delegiert an Validator-Methode)
- âœ… Pattern wird nur einmal kompiliert (Performance-Verbesserung)
- âœ… Klarere Trennung von Verantwortlichkeiten
- âœ… Einfacher zu testen

---

### 1.6 Allgemeine Best Practices (LLM-Empfehlungen)

#### 1. Single Responsibility Principle (SRP)
- Jede Methode sollte **genau eine Aufgabe** haben
- Komplexe Validierungen in separate Methoden extrahieren

#### 2. Cyclomatic Complexity Zielwerte
- **1-4:** Einfach, geringes Risiko
- **5-7:** Moderat komplex, mittleres Risiko
- **8-10:** Komplex, hohes Risiko â†’ **Refactoring empfohlen**
- **11+:** Sehr komplex, sehr hohes Risiko â†’ **Refactoring notwendig**

#### 3. Guard Clauses verwenden
```java
// Statt verschachtelter if-Statements:
if (value != null) {
    if (value.length() > 0) {
        if (value.matches(pattern)) {
            // Logik
        }
    }
}

// Besser: Guard Clauses (Early Return)
if (value == null) throw new IllegalArgumentException();
if (value.length() == 0) throw new IllegalArgumentException();
if (!value.matches(pattern)) throw new IllegalArgumentException();
// Logik
```

#### 4. Validator-Pattern fÃ¼r wiederverwendbare Validierung
- Zentrale Validator-Klasse reduziert Duplizierung
- Konsistente Fehlermeldungen im gesamten Projekt
- Einfacher zu warten und zu erweitern

---

### 1.7 Positive Erkenntnisse

#### âœ… Was bereits gut lÃ¤uft:

1. **Niedrige durchschnittliche KomplexitÃ¤t (1,92)**
   - Deutlich unter dem Zielwert von 5
   - Zeigt insgesamt gute Code-QualitÃ¤t

2. **Test-Klassen sind perfekt einfach**
   - Alle Test-Methoden haben Complexity = 1
   - Einfache, fokussierte Tests

3. **User-Klasse gut strukturiert**
   - 15 Methoden mit durchschnittlich v(G) = 1,47
   - Keine Ã¼bermÃ¤ÃŸig komplexen Methoden
   - WMC = 22 ist akzeptabel

4. **Keine Parameter-Explosion**
   - Keine Methoden mit mehr als 5 Parametern gefunden
   - Constructor Injection wird sinnvoll eingesetzt

5. **Domain-Driven Design**
   - Gute Kapselung in Value Objects (Email, Password)
   - Validierung in den Domain-Objekten selbst

---

### 1.8 Handlungsplan

#### PrioritÃ¤t 1 (Hoch) - Sollte vor Frontend-Entwicklung umgesetzt werden:
- [ ] **Password.validate()** refaktorieren
  - Methode aufteilen wie in Optimierung 1 beschrieben
  - Unit-Tests fÃ¼r neue Methoden schreiben
  - Erwartete Zeitaufwand: 30-45 Minuten

#### PrioritÃ¤t 2 (Mittel) - Kann parallel zur Frontend-Entwicklung erfolgen:
- [ ] **StringValidator-Utility** erstellen
  - Zentrale Validator-Klasse implementieren
  - User.validateName() und Email umbauen
  - Erwartete Zeitaufwand: 45-60 Minuten

#### PrioritÃ¤t 3 (Niedrig) - Nice to have:
- [ ] **Email-Konstruktor** optimieren
  - Pattern als Konstante auslagern
  - Validierung extrahieren
  - Erwartete Zeitaufwand: 20 Minuten

---

### 1.9 Metriken-Ziele nach Refactoring

| Metrik | Aktuell | Ziel nach Refactoring |
|--------|---------|----------------------|
| Password.validate() v(G) | **10** | **â‰¤ 3** pro Methode |
| User.validateName() v(G) | **6** | **â‰¤ 2** |
| Email-Konstruktor v(G) | **6** | **â‰¤ 2** |
| Durchschnittliche v(G) | 1,92 | **â‰¤ 1,80** |
| WMC Password-Klasse | 18 | **â‰¤ 15** |

---

### 1.10 Werkzeuge und Ressourcen

**Verwendete Tools:**
- IntelliJ IDEA CodeMetrics Plugin
- IntelliJ Built-in Inspections
- JUnit 5 (fÃ¼r Unit-Tests)

**WeiterfÃ¼hrende Literatur:**
- "Clean Code" von Robert C. Martin
- "Refactoring: Improving the Design of Existing Code" von Martin Fowler
- Cyclomatic Complexity: McCabe (1976)

**Online-Ressourcen:**
- [SonarSource: Cognitive Complexity](https://www.sonarsource.com/docs/CognitiveComplexity.pdf)
- [Code Metrics Documentation](https://www.jetbrains.com/help/idea/code-metrics.html)

---

### 1.11 Zusammenfassung

**Gesamtbewertung:** â­â­â­â­â˜† (4/5 Sterne)

Das Projekt zeigt eine **solide Code-QualitÃ¤t** mit einer durchschnittlichen KomplexitÃ¤t von nur 1,92. Die meisten Klassen und Methoden sind gut strukturiert und einfach zu verstehen.

**Haupterkenntnisse:**
- Eine kritische Methode (`Password.validate()`) benÃ¶tigt dringendes Refactoring
- Validierungslogik kÃ¶nnte durch ein Validator-Pattern vereinheitlicht werden
- Test-Code ist vorbildlich einfach gehalten
- Domain-Driven Design wird gut umgesetzt

**NÃ¤chster Schritt:**  
Vor der Frontend-Entwicklung sollte die `Password.validate()`-Methode refaktoriert werden, um die Code-QualitÃ¤t auf einem hohen Niveau zu halten.

---

**Erstellt am:** 09.11.2025  
**Analysiert mit:** IntelliJ IDEA CodeMetrics Plugin  
**LLM-UnterstÃ¼tzung:** Claude (Anthropic) fÃ¼r OptimierungsvorschlÃ¤ge
