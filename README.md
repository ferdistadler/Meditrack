# Aufgabe 5: Software- und Architekturmetriken für Codequalität und Architekturoptimierung

## 1. Überblick und Anwendung einfacher Metriken: 

### Verwendete Tools

**Tool:** IntelliJ IDEA CodeMetrics Plugin

**Grund für die Wahl:**
- Direkte Integration in die verwendete IDE
- Keine zusätzliche Server-Installation erforderlich (im Gegensatz zu SonarQube)

---

### Gemessene Metriken

| Metrik | Bedeutung | Zielwert |
| --- | --- | --- |
| **v(G)** - Cyclomatic Complexity | Anzahl unabhängiger Ausführungspfade durch eine Methode | ≤ 10 |
| **CogC** - Cognitive Complexity | Wie schwer ist der Code zu verstehen? | ≤ 15 |
| **WMC** - Weighted Methods per Class | Summe aller Komplexitäten in einer Klasse | ≤ 50 |
| **OCavg** - Average Operation Complexity | Durchschnittliche Komplexität pro Methode | ≤ 5 |
| **OCmax** - Maximum Operation Complexity | Höchste Komplexität einer Methode in der Klasse | ≤ 10 |

### Detaillierte Klassen-Analyse

### Übersicht der Klassen:

| Klasse | Ø Complexity | Max Complexity | WMC | Anzahl Methoden | Bewertung |
| --- | --- | --- | --- | --- | --- |
| **Password** | 2,57 | **9** | 18 | 7 | ⚠️ Optimierungsbedarf |
| **Email** | 2,20 | 5 | 11 | 5 | ⚡ Akzeptabel |
| **User** | 1,47 | 3 | 22 | 15 | ✅ Gut |
| **PasswordTest** | 1,00 | 1 | 3 | 3 | ✅ Sehr gut |
| **EmailTest** | 1,00 | 1 | 3 | 3 | ✅ Sehr gut |
| **UserTest** | 1,00 | 1 | 3 | 3 | ✅ Sehr gut |
| **Role** | n/a | n/a | 0 | 0 | ✅ Enum (keine Methoden) |

### Identifizierte Schwachstellen

### 🔴 Kritisch: Password.validate(String)
**LLM-Empfehlung:** Die Methode in kleinere, fokussierte Methoden aufteilen (Extract Method Refactoring).

### ⚠️ Moderat: User.validateName(String)
**LLM-Empfehlung:** Wiederverwendbare Validator-Utility-Klasse erstellen.

### Optimierungspotential: Email.Email(String)
**LLM-Empfehlung:** Validierungslogik extrahieren und Pattern-Kompilierung optimieren.

## 2: Test Coverage erweitern und Code Coverage verbessern

## Ausgangssituation 

### Initiale Coverage-Messung 

**Gesamtübersicht:**

Instruction Coverage: 64%
Branch Coverage:      63%
Anzahl Tests:         9
Analysierte Klassen:  4 (Email, Password, User, Role)`

<img width="985" height="263" alt="Test_1_1" src="https://github.com/user-attachments/assets/42343236-d676-4302-ad0f-780747c312ad" />


### Empfehlung 1: Password.validate() - Höchste Priorität

**Begründung:** Komplexeste Methode (Cyclomatic Complexity: 10) mit nur 66% Coverage

**Vorgeschlagene Edge-Case-Tests:**

1. **Null-Handling:**
    - `null` Password
    - Leerer String
    - Nur Whitespace
2. **Längen-Boundary-Tests:**
    - Genau 7 Zeichen (unter Minimum)
    - Genau 8 Zeichen (Minimum)
    - Genau 128 Zeichen (Maximum)
    - 129 Zeichen (über Maximum)
3. **Fehlende Zeichentypen:**
    - Ohne Großbuchstaben
    - Ohne Kleinbuchstaben
    - Ohne Ziffern
    - Ohne Sonderzeichen
4. **Kombinationen:**
    - Mehrere fehlende Requirements gleichzeitig
    - Verschiedene Sonderzeichen testen
    - Unicode-Zeichen

**LLM-Begründung:**

> "Die validate()-Methode ist sicherheitskritisch und sollte alle ungültigen Eingaben robust ablehnen. Jeder Branch sollte explizit getestet werden, um Sicherheitslücken zu vermeiden."
> 

### Empfehlung 2: User - Ungetestete Methoden

**Begründung:** 5 von 15 Methoden komplett ungetestet (0% Coverage)

**Vorgeschlagene Tests für `changePassword()`:**

1. Korrektes altes Password → Erfolg
2. Falsches altes Password → Exception
3. Null-Werte für beide Parameter
4. Ungültiges neues Password
5. Gleiches Password wie vorher

**Vorgeschlagene Tests für `updateProfile()`:**

1. Nur firstName ändern
2. Nur lastName ändern
3. Beide Namen ändern
4. Ungültige Namen (zu kurz, zu lang, null, leer)
5. Namen mit Leerzeichen
6. Boundary-Tests (2 und 50 Zeichen)

**LLM-Begründung:**

> "Diese Methoden sind zentral für die User-Verwaltung. Ohne Tests besteht das Risiko, dass Änderungen unbemerkt Fehler einführen."
> 

### Empfehlung 3: equals() und hashCode() - Alle Klassen

**Begründung:** Fundamentale Methoden für Collections, aber komplett ungetestet

**Vorgeschlagene Tests:**

1. **Equals-Contract:**
    - Reflexivität: `x.equals(x)` = true
    - Symmetrie: `x.equals(y)` = `y.equals(x)`
    - Transitivität: `x.equals(y)` ∧ `y.equals(z)` → `x.equals(z)`
    - Konsistenz: Mehrfache Aufrufe liefern gleiches Ergebnis
    - Null-Vergleich: `x.equals(null)` = false
2. **HashCode-Contract:**
    - Konsistenz: Mehrfache Aufrufe liefern gleichen Hash
    - Equals-HashCode-Konsistenz: `x.equals(y)` → `x.hashCode()` = `y.hashCode()`
3. **Integration-Tests:**
    - Funktioniert in `HashSet`
    - Funktioniert als `HashMap`Key

**LLM-Begründung:**

> "Fehlerhafte equals()/hashCode()-Implementierungen führen zu schwer debugbaren Problemen in Collections. Diese Methoden müssen umfassend getestet werden."
> 

### Empfehlung 4: Edge Cases für Email-Validierung

**Begründung:** Email-Validierung ist anfällig für Sicherheitsprobleme

**Vorgeschlagene Tests:**

1. Ungültige Formate (ohne @, ohne Domain, ohne TLD)
2. Sonderzeichen im lokalen Teil
3. Subdomains
4. Verschiedene TLDs
5. Leading/Trailing Whitespace
6. Case-Sensitivity

## 2.5 Testergebnisse

### Finale Coverage-Messung

**Gesamtübersicht:**

Instruction Coverage: 97% (vorher: 64%)
Branch Coverage:      96% (vorher: 63%)
Anzahl Tests:         122 (vorher: 9)`

### Detaillierte End-Coverage pro Klasse:

| Klasse | Instruction Cov. | Branch Cov. | Verbesserung | Bewertung |
| --- | --- | --- | --- | --- |
| **Email** | 93% (+24%) | 92% (+14%) | ⬆️ Sehr gut | ✅ Ziel übertroffen |
| **Password** | 96% (+37%) | 96% (+35%) | ⬆️ Hervorragend | ✅ Ziel übertroffen |
| **User** | 100% (+38%) | 100% (+43%) | ⬆️ Perfekt | ✅ Ziel übertroffen |
| **Role** | 100% (±0%) | n/a | ⬆️ Perfekt | ✅ Bereits vollständig |

<img width="968" height="287" alt="Test_2_1" src="https://github.com/user-attachments/assets/d96fdd83-3138-41f8-a916-728764112a7e" />

## 3: Technical Debt Analyse mit SonarQube

### Tool: SonarQube Community Edition (Docker)

### Analyse-Ergebnis

**Quality Gate:** ✅ PASSED

| Metrik | Wert | Status |
| --- | --- | --- |
| **Security Issues** | 0 | ✅ Perfekt |
| **Bugs** | 0 | ✅ Perfekt |
| **Test Coverage** | 97.5% | ✅ Exzellent |
| **Code Duplications** | 0.0% | ✅ Perfekt |
| **Maintainability Issues** | 15 | ⚠️ Verbesserungsbedarf |
| **Technical Debt** | 54 Minuten | ⚠️ Überschaubar |

### Die 3 kritischsten Issues

### 1. 🔴 Blocker: Test ohne Assertions (10 Min)

**Problem:** Test verifiziert nichts, täuscht Coverage vor

**LLM-Erklärung:**

> "Tests ohne Assertions schlagen niemals fehl, auch wenn der Code falsch ist. Sie verstoßen gegen das Arrange-Act-Assert-Pattern."
> 


### 2. 🟠 Medium: Auskommentierter Code (15 Min)

**Problem:** 3 Blöcke mit auskommentiertem Code verwirren Entwickler

**LLM-Empfehlung:** Als Dokumentation umschreiben statt löschen

```java
// Email Validation Design Decision:
// The implementation intentionally uses permissive rules to support
// legacy email addresses in the existing customer database.
// Stricter validation may be implemented in v2.0.

```

### 3. 🟠 Medium: assertEquals Reihenfolge (14 Min)

**Problem:** 7 Tests verwenden falsche Argument-Reihenfolge

**LLM-Erklärung:**

> "Die Konvention ist assertEquals(expected, actual), damit JUnit 'expected X but was Y' ausgibt - nicht umgekehrt."
> 

**Fix:** `assertEquals(user.getName(), "John")` → `assertEquals("John", user.getName())`


## 4. Frontend-Entwicklung und Erweiterung der Anwendung: 

**Einfacher Login, nicht rollenspezifisch**
- **Vanilla JavaScript + Tailwind CSS** - Leichtgewichtige, moderne Web-UI ohne Framework-Dependencies
- **REST API Integration** - Fetch API für asynchrone Backend-Kommunikation mit Spring Boot
- **Responsive Design** - Mobile-first Ansatz mit Tailwind's Utility-Classes für optimale Darstellung auf allen Geräten
