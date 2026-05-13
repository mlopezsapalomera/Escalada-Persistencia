# Projecte de Gestió d'Escalada

## 1. Descripció General

Aquest projecte s'ha desenvolupat per donar resposta a les necessitats plantejades a l'enunciat, creant una aplicació de consola en Java per gestionar informació sobre vies d'escalada, escoles, sectors i escaladors. L'objectiu principal és proporcionar una eina robusta que no només permeti les operacions CRUD bàsiques, sinó que també implementi les regles de negoci i els casos d'ús específics del món de l'escalada.

A continuació, es detalla com s'ha abordat cada requisit de l'enunciat en el disseny de l'aplicació i de la base de dades.

---

## 2. Model de Dades i Estructura de la Base de Dades

L'enunciat demanava emmagatzemar informació detallada sobre diferents entitats. Per això, s'ha dissenyat una base de dades relacional que reflecteix aquestes necessitats.

### 2.1. Gestió de Vies, Escoles i Sectors

- **Requisit**: L'enunciat especificava una jerarquia clara: Les **Escoles** contenen **Sectors**, i els **Sectors** contenen **Vies**.
- **Implementació**: S'han creat les taules `escoles`, `sectors` i `vies`.
    - La taula `sectors` té una clau forana (`escola_id`) que apunta a `escoles`.
    - La taula `vies` té una clau forana (`sector_id`) que apunta a `sectors`. A més, s'ha inclòs una clau forana directa a `escola_id` per simplificar les consultes que demanen totes les vies d'una escola, millorant així el rendiment.

### 2.2. Modelatge dels Tipus de Vies

- **Requisit**: L'aplicació ha de gestionar tres tipus de via: **Esportiva**, **Clàssica** i **Gel**, cadascuna amb atributs particulars. Les vies Clàssiques i de Gel es componen de **llargs**.
- **Implementació**:
    - S'ha optat per una taula principal `vies` que conté els atributs comuns a totes les vies (nom, grau, orientació, estat, etc.). Un camp `tipus_via` (ENUM) diferencia entre 'ESPORTIVA', 'CLASSICA' i 'GEL'.
    - Per als atributs específics, s'han creat taules addicionals:
        - `detalls_esportiva`: Emmagatzema l'allargada i el tipus d'ancoratge per a les vies esportives, amb una relació 1 a 1 amb la taula `vies`.
        - `llargs`: Emmagatzema els diferents trams de les vies Clàssiques i de Gel. Cada registre d'aquesta taula està vinculat a una via mitjançant una clau forana (`via_id`).

### 2.3. Gestió d'Escaladors

- **Requisit**: Calia guardar informació sobre els **escaladors**, incloent el seu nivell màxim i la via on el van assolir. A més, cada via ha de tenir un creador, que ha de ser un escalador existent.
- **Implementació**:
    - S'ha creat la taula `escaladors` amb tots els camps requerits (`nom`, `alias`, `nivell`, `nom_via_nivell_maxim`, etc.).
    - A la taula `vies`, el camp `creador_id` és una clau forana que apunta a la taula `escaladors`, assegurant la integritat referencial.

---

## 3. Implementació de les Regles de Negoci

L'enunciat definia diverses regles de negoci que s'han implementat tant a la base de dades com a l'aplicació.

- **Requisit**: Noms únics per a Escoles, Sectors (dins d'una escola) i Vies (dins d'un sector).
- **Implementació**: S'han aplicat restriccions `UNIQUE` a la base de dades en les columnes corresponents per garantir la unicitat de les dades a nivell de base de dades. L'aplicació Java també realitza comprovacions prèvies abans d'intentar una inserció.

- **Requisit**: Gestió de l'estat de les vies (`Apte`, `construcció`, `tancada`) amb un retorn automàtic a "Apte".
- **Implementació**: La taula `vies` inclou un camp `estat` (ENUM) i un camp `data_finalitzacio_estat` (DATE). La lògica per comprovar aquesta data i actualitzar l'estat a "Apte" es gestionarà des de l'aplicació Java, ja que requereix una lògica temporal que és més flexible de mantenir al codi.

- **Requisit**: Els sectors només poden contenir vies de Gel, o una combinació de Clàssica i Esportiva, però no Gel amb les altres.
- **Implementació**: Aquesta és una regla de negoci complexa. Es gestiona a la capa de lògica de l'aplicació (als controladors o serveis). Abans de crear una via en un sector, l'aplicació comprova les vies existents en aquest sector per assegurar que es compleix la regla.

---

## 4. Arquitectura de l'Aplicació Java

Per implementar els requisits funcionals (CRUD i casos d'ús), s'ha seguit un patró d'arquitectura Model-Vista-Controlador (MVC).

- **Model**: Conté les entitats (`Escalador.java`, `Via.java`, etc.) i les classes d'accés a dades (DAO), que s'encarreguen de tota la comunicació amb la base de dades.
- **Vista**: Classes responsables d'interactuar amb l'usuari a través de la consola (`EscaladorView.java`, `MenuView.java`, etc.), mostrant menús i sol·licitant dades.
- **Controlador**: Actua com a intermediari, rebent les peticions de la Vista, utilitzant el DAO corresponent per interactuar amb el Model i retornant els resultats a la Vista.

Aquesta separació de responsabilitats fa que el codi sigui més organitzat, mantenible i fàcil de testejar.

### 4.1. Patró Abstract Factory per a la Persistència (DAO Factory)

- **Requisit**: Un dels requisits clau és que l'aplicació sigui independent del motor de la base de dades (MySQL, PostgreSQL, etc.), permetent un canvi de sistema de persistència sense modificar la lògica de negoci.
- **Implementació**: Per aconseguir-ho, s'ha implementat el patró de disseny **Abstract Factory** (conegut com a **DAO Factory**).
    1.  **Interfícies DAO** (a `src/model/dao/`): Per a cada entitat (ex: `EscaladorDAO`), es defineix una interfície amb els mètodes CRUD. Aquestes interfícies són el "contracte" que la resta de l'aplicació utilitza, sense conèixer els detalls de la implementació.
    2.  **Implementacions Concretes** (a `src/model/dao/mysql/`): Per a cada interfície, es crea una classe d'implementació específica per a MySQL (ex: `MySqlEscaladorDAOImpl.java`) que conté el codi SQL concret.
    3.  **Fàbrica Abstracta (`DAOFactory`)**: És una classe abstracta que defineix quins DAOs es poden obtenir.
    4.  **Fàbrica Concreta (`MySqlDAOFactory`)**: És la classe que sap com crear i retornar les instàncies de les implementacions de MySQL.

Gràcies a aquest patró, si en el futur es volgués afegir suport per a PostgreSQL, només caldria crear una nova carpeta `src/model/dao/postgres/` amb les seves implementacions i una `PostgresDAOFactory`, sense haver de tocar ni una línia del codi dels controladors o les vistes.

---

## 5. Diagrama de la Base de Dades

El disseny de la base de dades es pot visualitzar en el següent diagrama:

![Diagrama base de dades](diagrama_bd.png)

---

## 6. Consideracions Finals

El disseny actual compleix tots els requisits de l'enunciat. Les decisions preses busquen un equilibri entre la normalització de la base de dades per garantir la integritat i la desnormalització controlada (com la clau `escola_id` a la taula `vies`) per optimitzar les consultes més freqüents. La lògica de negoci més complexa es delega a l'aplicació Java per a una major flexibilitat.

