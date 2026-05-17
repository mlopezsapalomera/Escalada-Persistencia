# Resumen (formato solicitado)

Este README está estructurado para que puedas explicar el proyecto punto por punto al profesor.

**1. ¿Qué pide el profesor?**
- CRUD para `Escoles`, `Sectors`, `Vies` y `Escaladors`.
- Historial de ascensos.
- 8 consultas específicas (vies disponibles, búsqueda por grado/estado, escuelas con restricciones, sectores con más de X vías disponibles, escaladores agrupados por nivel, vías recientes que pasaron a 'Apte', vías más largas por escuela).
- Reglas de negocio: unicidad, compatibilidad de tipos de sector, validación de grados y gestión temporal del estado de una vía (pasar a 'Apte' cuando venza la fecha).

**2. ¿Qué hemos hecho para cumplirlo?**
- Implementado controladores (CLI), vistas y DAOs para todas las entidades.
- Añadidas validaciones de negocio en DAOs y controladores: unicidad de nombres, compatibilidad sector/vía, validación de grados.
- Añadida función automática `refreshEstados()` en `MySqlViaDAOImpl` para actualizar `estat` según `data_finalitzacio_estat`.
- Añadido `seed.sql` con datos mínimos para pruebas rápidas.

**3. ¿Por qué lo hemos hecho así?**
- Separación MVC y DAOs: facilita entender quién hace qué y simplifica la corrección y mantenimiento.
- Validaciones en DAO y controladores: evitar datos inválidos en la BD y proporcionar mensajes claros en la CLI.
- `refreshEstados()` en DAO: solución simple y suficiente para una aplicación de consola; evita depender de un job externo.

**4. Documentos/ficheros implicados**
- Controladores: [src/controller/EscolaController.java](src/controller/EscolaController.java#L1), [src/controller/SectorController.java](src/controller/SectorController.java#L1), [src/controller/ViaController.java](src/controller/ViaController.java#L1), [src/controller/EscaladorController.java](src/controller/EscaladorController.java#L1), [src/controller/HistorialController.java](src/controller/HistorialController.java#L1).
- Vistas: [src/view/EscolaView.java](src/view/EscolaView.java#L1), [src/view/SectorView.java](src/view/SectorView.java#L1), [src/view/ViaView.java](src/view/ViaView.java#L1), [src/view/EscaladorView.java](src/view/EscaladorView.java#L1), [src/view/HistorialView.java](src/view/HistorialView.java#L1).
- DAOs/persistencia: [src/model/dao/mysql/MySqlEscolaDAOImpl.java](src/model/dao/mysql/MySqlEscolaDAOImpl.java#L1), [src/model/dao/mysql/MySqlSectorDAOImpl.java](src/model/dao/mysql/MySqlSectorDAOImpl.java#L1), [src/model/dao/mysql/MySqlViaDAOImpl.java](src/model/dao/mysql/MySqlViaDAOImpl.java#L1), [src/model/persistencia/conexio_db.java](src/model/persistencia/conexio_db.java#L1).
- Scripts: `seed.sql` (fichero raíz).

---

Si quieres que complete lo que falta (tests automáticos, seed completo y la ejecución final de la simulación E2E) me indicas y lo hago a continuación.

