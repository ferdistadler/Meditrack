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

### ⚠️ Moderat: User.validateName(String)

### Optimierungspotential: Email.Email(String)


## 2: Test Coverage erweitern und Code Coverage verbessern


