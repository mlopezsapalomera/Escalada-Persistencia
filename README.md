# Base de Dades Escalada

## 1. Descripció general

Aquesta base de dades s’ha dissenyat per gestionar informació relacionada amb vies d’escalada, escoles, sectors i escaladors. L’objectiu és permetre una gestió flexible i estructurada de les diferents modalitats d’escalada (esportiva, clàssica i gel).

El model segueix una estructura relacional basada en diverses entitats principals i taules auxiliars per garantir la coherència i evitar redundàncies.

---

## 2. Entitats principals

Les taules principals del sistema són:

- escoles: representen les zones generals d’escalada.
- sectors: subdivisions dins d’una escola.
- vies: rutes d’escalada.
- escaladors: persones que creen o escalen vies.

També s’han creat taules auxiliars:

- detalls_esportiva: informació específica de vies esportives.
- llargs: trams de vies clàssiques i de gel.
- historial_escaladors: relació entre escaladors i vies escalades.

---

## 3. Relacions entre entitats

- Una escola pot tenir diversos sectors.
- Un sector pertany a una única escola.
- Un sector pot tenir diverses vies.
- Una via pertany a un sector i a una escola.
- Una via és creada per un escalador.
- Una via pot tenir diversos llargs (en el cas de vies clàssiques i de gel).
- Un escalador pot haver escalat diverses vies.

---

## 4. Decisions de disseny

### 4.1 Estructura jeràrquica
S’ha definit una jerarquia clara entre escoles, sectors i vies. Aquesta estructura reflecteix el funcionament real del món de l’escalada i facilita la gestió de les dades.

---

### 4.2 Modelatge de les vies
S’ha utilitzat una única taula de vies amb un camp tipus_via per diferenciar entre esportiva, clàssica i gel. Això permet mantenir una estructura unificada i simplificar les consultes.

S’ha creat una taula específica (detalls_esportiva) per a les vies esportives.

---

### 4.3 Gestió de llargs
Les vies clàssiques i de gel poden tenir múltiples trams. Per això s’ha creat la taula llargs.

---

### 4.4 Estat de les vies
El camp estat indica si una via està disponible, en construcció o tancada.

El camp data_finalitzacio_estat permet gestionar el retorn automàtic a estat “apte” des de l’aplicació.

---

### 4.5 Validació de dades
- ENUMs per camps tancats (estat, orientació, tipus, popularitat)
- Validació de formats amb expressions regulars a l’aplicació

---

### 4.6 Relació amb escaladors
Cada via està associada a un escalador creador.

També s’ha implementat un historial d’activitats d’escalada.

---

### 4.7 Normalització
La base de dades segueix un model proper a la tercera forma normal (3NF), evitant redundàncies i millorant la integritat de les dades.

---

## 5. Restriccions implementades

- No poden existir dues escoles amb el mateix nom.
- No es poden repetir noms de sectors dins d’una mateixa escola.
- No es poden repetir noms de vies dins d’un mateix sector.
- Integritat referencial amb claus foranes.
- Validació de valors amb ENUM.

---

## 6. Diagrama de la base de dades
