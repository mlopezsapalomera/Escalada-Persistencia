package model.dao;
import model.entidades.Via;
import java.util.List;

public interface ViaDAO {
    /**
     * Crea una nueva vía.
     * @param via objeto con los datos de la vía.
     * @return true si se crea correctamente; false en caso contrario.
     */
    boolean crear(Via via);

    /**
     * Obtiene una vía por su id.
     * @param id id de la vía.
     * @return vía encontrada o null si no existe.
     */
    Via obtenirPerId(int id);

    /**
     * Recupera todas las vías registradas.
     * @return lista de vías.
     */
    List<Via> obtenirTots();

    /**
     * Actualiza una vía existente.
     * @param via objeto con los datos actualizados.
     * @return true si se actualiza correctamente; false en caso contrario.
     */
    boolean actualitzar(Via via);

    /**
     * Elimina una vía por su id.
     * @param id id de la vía a eliminar.
     * @return true si se elimina correctamente; false en caso contrario.
     */
    boolean eliminar(int id);

    /**
     * Obtiene vías disponibles para una escuela concreta.
     * @param idEscola id de la escuela.
     * @return lista de vías disponibles en esa escuela.
     */
    List<Via> obtenirViesDisponiblesPerEscola(int idEscola);

    /**
     * Busca vías entre dos grados de dificultad.
     * @param minGrau grado mínimo.
     * @param maxGrau grado máximo.
     * @return lista de vías dentro del rango.
     */
    List<Via> cercarPerDificultat(String minGrau, String maxGrau);

    /**
     * Busca vías por estado.
     * @param estat estado a filtrar.
     * @return lista de vías que coinciden con el estado.
     */
    List<Via> cercarPerEstat(String estat);

    /**
     * Obtiene vías que han pasado a estado apto recientemente.
     * @param dies ventana de días hacia atrás.
     * @return lista de vías aptas recientes.
     */
    List<Via> obtenirViesQueHanPassatAPteRecentment(int dies);

    /**
     * Obtiene las vías más largas de una escuela.
     * @param idEscola id de la escuela.
     * @param limit número máximo de resultados.
     * @return lista de vías ordenadas por longitud.
     */
    List<Via> obtenirViesMesLlarguesPerEscola(int idEscola, int limit);

    /**
     * Fuerza la actualización de estados (por ejemplo, vías cuya fecha de fin de estado ha caducado).
     */
    void actualitzarEstats();
}