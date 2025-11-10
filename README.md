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

**Zeitersparnis durch LLM:** ~8-10 Stunden
