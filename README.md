# Escalada-Persistencia

Aplicación de consola en Java para gestionar escuelas, sectores, vías, escaladores e historial de ascensiones con persistencia MySQL (JDBC), arquitectura MVC + DAO + Abstract Factory.

La idea de este README es que, si llegas por primera vez, entiendas rápido **qué hemos construido**, **cómo está organizado** y **dónde tocar** cada cosa.

---

## Qué hemos construido

Hemos implementado un sistema completo de gestión con:
- CRUD de `Escola`, `Sector`, `Via`, `Escalador`.
- Registro y consulta de ascensiones (`Historial`).
- Soporte de 3 tipos de vía: `ESPORTIVA`, `CLASSICA`, `GEL`.
- Reglas de negocio del enunciado (grados, estados, restricciones temporales, etc.).
- Consultas avanzadas sobre vías.

---

## Arquitectura (cómo lo hemos organizado)

### 1) `view/` (consola)
Aquí pedimos datos y mostramos resultados. No hay SQL.

Ejemplo:
```java
public int mostrarMenuPrincipal() { ... }
```

### 2) `controller/` (flujo + negocio)
Aquí orquestamos la navegación del menú, validamos reglas y delegamos en DAO.

Ejemplo:
```java
if (!validarGrau(v.getGrauGlobal(), v.getEstil())) return;
```

### 3) `model/entidades/` (dominio)
Aquí están las clases del dominio (`Via`, `Escola`, `Sector`, etc.).

### 4) `model/dao/` + `model/dao/mysql/` (persistencia)
- Interfaces DAO en `model/dao`.
- Implementación SQL en `model/dao/mysql`.

Ejemplo:
```java
DAOFactory.obtenirDAOFactory(DAOFactory.MYSQL).obtenirViaDAO();
```

---

## Flujo principal de ejecución

Desde `Main` hemos dejado:
1. Un único `Scanner` compartido.
2. Inicialización de todos los controladores.
3. Bucle principal de menú.
4. Cierre seguro de conexión al salir.

Snippet:
```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    model.persistencia.conexio_db.desconectar();
}));
```

---

## Reglas de negocio importantes que ya hemos cubierto

## Vías y tipos
- **Esportiva**: valida longitud (5–30) y anclajes.
- **Clàssica/Gel**: gestiona lista de `Llarg` con metros y grado por tramo.

## Estados con fecha
- `APTE`, `CONSTRUCCIO`, `TANCADA`.
- Si hay fecha de fin de estado (`data_finalitzacio_estat`), actualizamos estados antes de consultas.

## Compatibilidad sector ↔ estilo
- En sectores `GEL` solo permitimos vías `GEL`.
- En sectores `MIXTE_ROCA` bloqueamos vías `GEL`.

## Dificultad y validación de grados
- Orden de grados centralizado para comparar rangos.
- Validación de formato para evitar grados inválidos.

## Contadores de vías (`num_vies`)
- En altas/bajas/cambios de sector ajustamos contadores de escuela y sector desde DAO.

---

## Consultas avanzadas implementadas

En `MySqlViaDAOImpl` hemos añadido:
1. Vías disponibles por escuela.
2. Búsqueda por rango de dificultad.
3. Búsqueda por estado.
4. Vías que pasaron a aptas recientemente.
5. Vías más largas por escuela.

---

## Persistencia y conexión MySQL

### Configuración
En `model/persistencia/config.java` hemos centralizado:
- `DB_TYPE`
- `URL`
- `USER`
- `PASS`
- `DRIVER`
- `DRIVER_JAR`

### Conexión
- `ConnectionFactory.java`: crea conexión y carga el driver.
- Si el driver no está en classpath, intentamos fallback con búsqueda de JAR + `DriverShim`.
- `conexio_db.java`: conexión compartida con apertura bajo demanda y cierre final.

---

## Estructura resumida

```text
src/
  Main.java
  controller/
    EscolaController.java
    SectorController.java
    ViaController.java
    EscaladorController.java
    HistorialController.java
  view/
    MenuView.java
    EscolaView.java
    SectorView.java
    ViaView.java
    EscaladorView.java
    HistorialView.java
  model/
    entidades/
    dao/
      mysql/
    persistencia/
```

---

## Cómo ejecutar

Con compilado previo (`out`) y conector en classpath:

```powershell
Get-Content .\auto-test-inputs.txt | java -cp "out;connectorMysql\mysql-connector-j-9.7.0.jar" Main
```

Si quieres modo manual, ejecuta igual sin redirección y responde por consola.

---

## Qué documentos mirar según necesidad

- Visión global técnica: [Estudiar.md](Estudiar.md)
- Lógica de vías (clave): `src/controller/ViaController.java` + `src/model/dao/mysql/MySqlViaDAOImpl.java`
- Entrada y validación de datos de vía: `src/view/ViaView.java`
- Conexión y driver: `src/model/persistencia/ConnectionFactory.java`

---

## Estado actual del proyecto

A nivel funcional, hemos dejado el proyecto en un punto sólido para entrega:
- Arquitectura consistente.
- Validaciones de entrada reforzadas.
- Lógica de negocio principal implementada.
- Documentación técnica ampliada (incluyendo `Estudiar.md`).

Si más adelante queremos iterar, la siguiente mejora natural sería añadir tests automáticos de integración DAO + reglas de negocio.
