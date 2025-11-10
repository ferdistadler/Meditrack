# Meditrack

## Aufgabe 1: Code-Metriken analysieren

### Tool: IntelliJ IDEA CodeMetrics Plugin

**Ergebnis:** Durchschnittliche Complexity 1,92 (✅ sehr gut)

**Identifizierte Schwachstelle:**
- `Password.validate()` - Cyclomatic Complexity **10** (Grenzwert erreicht)

**LLM-Optimierung:**
- Methode in kleinere, fokussierte Methoden aufgeteilt
- Neue Complexity pro Methode: ≤ 3
- Bessere Testbarkeit und Wartbarkeit


## Aufgabe 2: Test Coverage erweitern

### Tool: JaCoCo (Java Code Coverage Library)

**Ausgangssituation:**
- Instruction Coverage: 64%
- Branch Coverage: 63%
- Anzahl Tests: 9
- 5 von 15 User-Methoden komplett ungetestet (0% Coverage)

**LLM-gestützte Testplanung:**

Der LLM (Claude) half bei:
1. **Systematischer Identifikation von Coverage-Lücken**
   - Priorisierung nach Kritikalität (ungetestete Methoden zuerst)
   - Komplexeste Methode: `Password.validate()` mit nur 66% Coverage

2. **Edge-Case-Generierung**
   - Boundary-Tests (min/max Längen)
   - Fehlende Zeichentypen
   - Null-Handling
   - equals()/hashCode() Contract-Tests

3. **Erstellung umfassender Test-Suiten**
   - EmailTest: 3 → 41 Tests (+38)
   - PasswordTest: 3 → 39 Tests (+36)
   - UserTest: 3 → 42 Tests (+39)

**Endergebnis:**

| Metrik | Vorher | Nachher | Verbesserung |
|--------|--------|---------|--------------|
| **Instruction Coverage** | 64% | **97%** | +33% |
| **Branch Coverage** | 63% | **96%** | +33% |
| **Anzahl Tests** | 9 | **122** | +1255% |

**Besondere Erfolge:**
- 🏆 User-Klasse: 100% Coverage
- 🏆 Alle kritischen Methoden vollständig getestet
- ✅ Ziel (80%) deutlich übertroffen


## Aufgabe 3: Technical Debt mit SonarQube analysieren

### Tool: SonarQube Community Edition (Docker)

**Quality Gate Status:** ✅ PASSED

**Projekt-Metriken:**

| Metrik | Wert | Bewertung |
|--------|------|-----------|
| **Lines of Code** | 307 | Klein |
| **Code Coverage** | 97.5% | ⭐⭐⭐⭐⭐ Exzellent |
| **Security Issues** | 0 | ⭐⭐⭐⭐⭐ Perfekt |
| **Reliability (Bugs)** | 0 | ⭐⭐⭐⭐⭐ Perfekt |
| **Maintainability Issues** | 15 | ⭐⭐⭐ Verbesserungsbedarf |
| **Code Duplications** | 0.0% | ⭐⭐⭐⭐⭐ Perfekt |
| **Technical Debt** | 54 Minuten | ⭐⭐⭐⭐ Niedrig |

**Identifizierte Issues:**

| Severity | Anzahl | Effort | Beispiel |
|----------|--------|--------|----------|
| 🔴 Blocker | 1 | 10 Min | Test ohne Assertions |
| 🟠 Medium | 11 | 37 Min | Auskommentierter Code, assertEquals-Reihenfolge |
| 🟡 Low | 3 | 7 Min | Regex-Syntax |

**LLM-Analyse der Top-Issues:**

1. **Test ohne Assertions (Blocker)**
   - Problem: Test verifiziert nichts, täuscht Coverage vor
   - LLM-Lösung: Arrange-Act-Assert Pattern implementieren
   - Impact: Echte Test-Qualität sicherstellen

2. **Auskommentierter Code (Medium)**
   - Problem: 3 Blöcke mit commented-out Tests
   - LLM-Empfehlung: In Dokumentation umwandeln
   - Ergebnis: Business-Entscheidungen dokumentiert

3. **assertEquals Reihenfolge (Medium)**
   - Problem: 7 Tests mit falscher Argument-Reihenfolge
   - LLM-Erklärung: `assertEquals(expected, actual)` → bessere Fehlermeldungen
   - Impact: Tests werden wartbarer

**Erwartetes Ergebnis nach Fixes:**
- Technical Debt: 54 Min → **0 Min** ✅
- Maintainability Rating: B → **A** ✅


## Aufgabe 4: Frontend-Entwicklung

### Verwendete Technologien
- **Framework:** React 18
- **Styling:** Tailwind CSS
- **Build-Tool:** Vite

### Implementierte Features
- User Registration Form
- Login/Authentication
- Password Validation 
- Email Validation
- Responsive Design


