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
