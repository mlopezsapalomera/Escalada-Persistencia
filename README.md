# Escalada-Persistencia


# Base de dades

# Memoria de Diseño: Aplicación Pillam Ltd. Co.

Hemos diseñado esta base de datos enfocándonos en la flexibilidad que requieren Jordi y Miquel para gestionar entornos tan diferentes como una vía deportiva de 10 metros y una pared alpina de hielo.

## Justificación de las Decisiones de Diseño

1. **Jerarquía Escuela-Sector-Vía** [cite: 519, 520]
   Hemos implementado una estructura en cascada (`ON DELETE CASCADE`). Hemos decidido que cada Escuela contenga Sectores y estos contengan las Vías. Para cumplir con la restricción de que los nombres no se repitan dentro de la misma Escuela[cite: 518], hemos usado claves únicas compuestas.

2. **Modelado de "Llarggs" (Tramos)** [cite: 343, 401]
   A diferencia de las vías deportivas, las Clásicas y de Gel se dividen en largos de entre 15 y 30 metros. Hemos decidido crear una tabla independiente de `Llarggs` para que una vía pueda tener un número ilimitado de tramos, manteniendo la coherencia de datos.

3. **Restricción de Sectores de Gel** [cite: 452]
   El enunciado prohíbe mezclar Hielo con otras modalidades. Hemos incluido la columna `tipus_sector`. En nuestra lógica de aplicación (Java), validaremos que si el sector es de tipo 'GEL', el sistema bloquee cualquier intento de insertar una vía 'Esportiva' o 'Clàssica'.

4. **Automatización del Estado "Apte"** [cite: 306, 349]
   Para que una vía pase automáticamente a "Apte" tras un tiempo en construcción o cerrada, hemos añadido `data_finalitzacio_estat`. Hemos decidido que sea la aplicación la que, al realizar la consulta, compare la fecha actual con este campo para determinar la disponibilidad en tiempo real.

5. **Validación de Dificultad y Orientación** [cite: 304, 305]
   Aunque el motor de la base de datos almacena estos valores como `VARCHAR`, hemos decidido que la lógica de persistencia en Java utilice **Expresiones Regulares (Regex)** para asegurar que solo entren grados válidos (ej. 4 a 9c+) y orientaciones cardinales permitidas.

6. **Gestión de Creadores (Escaladores)** [cite: 312, 409]
   Hemos vinculado cada vía a un escalador. Si el creador no existe en la tabla `Escaladors`, nuestro proceso DAO se encargará de darle de alta automáticamente antes de registrar la vía, asegurando la integridad referencial solicitada.

7. **Normalización vs. Rendimiento**
   Hemos mantenido 7 tablas principales. Esto nos permite una normalización de tercer nivel (3NF), evitando redundancias y facilitando las consultas específicas de Jordi y Miquel, como buscar vías por rango de dificultad o sectores con más de X vías disponibles[cite: 534].