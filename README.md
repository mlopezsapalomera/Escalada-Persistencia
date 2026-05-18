# Projecte: Escalada-Persistencia

Aplicació de consola desenvolupada en Java per a la gestió integral de zones d'escalada, sectors, vies i historials d'ascensions d'escaladors. El projecte implementa una arquitectura multicapa amb persistència en una base de dades relacional MySQL mitjançant JDBC, utilitzant els patrons DAO (Data Access Object) i Abstract Factory.

---

## 1. Arquitectura del Sistema

L'aplicació s'ha estructurada seguiment el patró MVC (Model-Vista-Controlador) i DAO per garantir el desacoblament complet entre la interfície d'usuari i l'accés a les dades:

1. **view (Presentació):** Classes que gestionen de forma exclusiva la interacció per consola (Scanner), mostren els menús i validen les dades d'entrada netejant els buffers per evitar pèrdues de text.
2. **controller (Lògica de Control):** Intermediaris que reben les peticions de les vistes, gestionen el flux del programa i criden les operacions de persistència.
3. **model.entidades (Domini):** Objectes de negoci que mapegen les entitats de l'enunciat (Escola, Sector, Via, Llarg, Escalador i Historial).
4. **model.dao (Persistència):** Interfícies i implementacions SQL (MySqlViaDAOImpl, etc.) utilitzant una factoria abstracta per obtenir les connexions de forma transparent.

---

## 2. Interfície i Menú Principal de l'Aplicació

El programa s'executa en mode interactiu a través d'un menú principal per consola que dóna accés a tots els mòduls requerits:

* **1. Gestionar Escoles:** CRUD de zones generals d'escalada.
* **2. Gestionar Sectors:** CRUD dels sectors vinculats a cada escola.
* **3. Gestionar Vies:** CRUD de rutes (Esportiva, Clàssica, Gel) i configuració de llargs.
* **4. Gestionar Escaladors:** CRUD dels perfils i configuració del nivell màxim.
* **5. Gestionar Historial:** Mòdul per registrar ascensions, èxits i actualització en calent de les dades.
* **0. Sortir:** Tancament segur de l'aplicació i de les connexions JDBC.

---

## 3. Requeriments del Domini i Regles de Negoci

El sistema valida i executa de forma automàtica les restriccions del món de l'escalada demanades:

* **Tipus de Vies:**
  - *Esportiva:* Controla que la llargada total estigui estrictament entre 5 i 30 metres i en llista els ancoratges vàlids (spits, parabolts, químics).
  - *Clàssica i Gel:* Desglossa la via de forma dinàmica en diversos Llargs, on cada tram es registra independentment amb els seus metres i el seu grau a la taula Llarg.
* **Control Temporal d'Estats:** Els estats de les vies (Apte, Construcció, Tancada) utilitzen el camp data_finalitzacio_estat des de Java per controlar automàticament quan venç una restricció de cara a les consultes.
* **Càlcul Dinàmic de l'Historial:** Quan es registra una ascensió com a reeixida, l'aplicació comprova si el grau de la via superat és major que el nivell_maxim actual de l'escalador; si és així, actualitza la seva fitxa a la base de dades automàticament.

---

## 4. Model de Dades i Persistència (SQL)

La base de dades local s'ha normalitzat per garantir la integritat referencial mitjançant claus foranes (FOREIGN KEY). L'esquema relacional se sustenta sobre les següents taules:

| Taula | Camps i Atributs Principals |
|---|---|
| escola | id, nom (Únic), lloc, popularitat |
| sector | id, nom, id_escola |
| via | id, nom, grau_global, orientacio, estil, estat, data_final_estat, llargada_total, ancoratges, id_sector |
| llarg | id, numero_llarg, llargada, dificultat, id_via |
| escalador | id, nom, alies, edat, nivell_maxim, id_via_maxim, estil_preferit |
| historial | id, id_escalador, id_via, data_ascensio, exit |

*Nota sobre el Classpath:* El mòdul ConnectionFactory compta amb una lògica de cerca automàtica (DriverShim) que localitza el connector .jar de MySQL a l'arrel o subcarpetes sense necessitat de configurar variables d'entorn manuals al sistema.

---

## 5. Consultes Avançades Integrades

A més dels fluxos de gestió estàndard, s'ha donat solució a les consultes específiques de l'enunciat:
1. **Disponibilitat per Escola:** Llista les vies en estat Apte d'una zona concreta.
2. **Cerca per Rang de Dificultat:** Filtra vies per graus (ex: entre 6a i 7b) mostrant dades del sector i escola.
3. **Cerca per Estat:** Troba de manera immediata rutes tancades o en modificació.
4. **Vies Aptes Recentment:** Mostra les reobertures segons el venciment de la data límit de la restricció.
5. **Vies més llargues:** Realitza la sumatòria total de metres dels llargs en clàssica/gel per determinar quina és la ruta amb major recorregut d'una escola.

---

## 6. Diagrama Entitat-Relació

A continuació es mostra el disseny relacional triat per a l'emmagatzematge de dades, reflectint com les taules auxiliars i les relacions d'obligatorietat eviten la redundància de dades:

![Diagrama base de dades](diagrama_bd.png)