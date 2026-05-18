1. En el enunciado se especifica "La aplicación contemplará 3 tipos de via: Esportiva, Clàssica i Gel" y que cada via tiene nombre, grado, orientación, estado, escuela/sector, creador y restricciones. Para cumplir esto hemos creado los siguientes archivos principales:

- [src/model/dao/mysql/MySqlViaDAOImpl.java](src/model/dao/mysql/MySqlViaDAOImpl.java): implementa el CRUD de vías y funciones concretas: `create(Via)`, `getById(int)`, `getAll()`, `update(Via)`, `delete(int)`, `refreshEstados()`, `buscarPorDificultat(...)`, `getDisponiblesPerEscola(...)`, `getViesQueHanPassatAPteRecentment(...)`, `getViesMesLlarguesPerEscola(...)`. Estas funciones gestionan tipos de vía (esportiva/clàssica/gel), almacenamiento de `detalls_esportiva` y `llargs`, y transición automática de estados mediante `refreshEstados()`.

- [src/controller/ViaController.java](src/controller/ViaController.java): expone la lógica de la interfaz de consola (menú y acciones) y orquesta validaciones y llamadas al DAO: `gestionarVies()`, `crearNovaVia()`, `modificarVia()`, `eliminarVia()`.

- [src/view/ViaView.java](src/view/ViaView.java): recoge entradas del usuario (nuevo/modificar vía), pide datos de estado/duración y construye el objeto `Via` que se pasa al controlador.

- [src/model/util/GradeUtils.java](src/model/util/GradeUtils.java): centraliza la lista/orden de grados y proporciona `isValid(String)`, `lessOrEqual(String,String)` para validar rangos y el límite específico para vías de gel.

- [src/model/persistencia/conexio_db.java](src/model/persistencia/conexio_db.java) y [src/Main.java](src/Main.java): gestionan la conexión compartida a la base de datos y garantizan cierre (shutdown hook). Esto facilita que los DAOs usen la misma conexión y que `refreshEstados()` se ejecute antes de las consultas.

2. En el enunciado también se menciona reglas de unicidad y consistencia: "No pueden existir dos Escoles con el mismo nombre. Dentro d'una Escola, no poden haver-hi noms de Sectors repetits. Dins d'una Escola, no poden haver-hi noms o números de vies repetits." Para cubrir estas restricciones hemos hecho lo siguiente:

- Verificación de unicidad al crear vías: en `create(Via)` (archivo [src/model/dao/mysql/MySqlViaDAOImpl.java](src/model/dao/mysql/MySqlViaDAOImpl.java)) existe una comprobación previa (`SELECT COUNT(*) ... WHERE id_escola = ? AND LOWER(nom) = LOWER(?)`) que impide insertar otra vía con el mismo nombre en la misma escuela.

- Mantenimiento de contadores `num_vies`: al crear una vía `create(Via)` incrementa `num_vies` en `escoles` y `sectors` (consultas `UPDATE ... SET num_vies = num_vies + 1`). En `update(Via)` se ajustan los contadores si la vía cambia de sector/escola (decremento en origen + incremento en destino). El método `delete(int)` fue implementado de forma transaccional para leer `id_escola`/`id_sector`, eliminar la vía y decrementar `num_vies` en ambas tablas dentro de la misma transacción ([src/model/dao/mysql/MySqlViaDAOImpl.java](src/model/dao/mysql/MySqlViaDAOImpl.java)).

- Limitaciones y alcance actual: la verificación de unicidad al actualizar (`update`) no está centralizada como una comprobación explícita que evita renombrar una vía hacia un nombre ya existente en la misma escuela (esto queda como punto a reforzar). Además, las eliminaciones en cascada (borrar una `escola` o `sector`) dependen del `ON DELETE CASCADE` de la BD, por lo que la coherencia final de `num_vies` en esos escenarios se recomienda recomputarla o manejarla con triggers/DAOs adicionales.

En resumen: el código actual implementa las piezas centrales exigidas por el enunciado (modelado de tipos de vía, CRUD, validación de grados y límites, gestión de estados con `data_finalitzacio_estat`, búsquedas por rango y mantenimiento básico de `num_vies`). Quedan por endurecer casos límite (unicidad en `update`, recomputado de `num_vies` tras eliminaciones masivas y pruebas E2E), pero la estructura y las funciones principales necesarias ya están en los archivos listados arriba.
