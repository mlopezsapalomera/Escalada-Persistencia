package model.dao;
import model.entidades.Escola;
import java.util.List;

public interface EscolaDAO {
    /**
     * Crea una nueva escuela en la base de datos.
     * @param escola objeto con los datos de la escuela.
     * @return true si se crea correctamente; false en caso contrario.
     */
    boolean crear(Escola escola);

    /**
     * Obtiene una escuela por su identificador.
     * @param id id de la escuela.
     * @return escuela encontrada o null si no existe.
     */
    Escola obtenirPerId(int id);

    /**
     * Recupera todas las escuelas registradas.
     * @return lista completa de escuelas.
     */
    List<Escola> obtenirTots();

    /**
     * Actualiza una escuela existente.
     * @param escola objeto con los datos actualizados.
     * @return true si la actualización se realiza; false si falla.
     */
    boolean actualitzar(Escola escola);

    /**
     * Elimina una escuela por su id.
     * @param id id de la escuela a eliminar.
     * @return true si se elimina correctamente; false en caso contrario.
     */
    boolean eliminar(int id);

    /**
     * Obtiene las escuelas con restricciones activas en alguna de sus vías.
     * @return lista de escuelas que cumplen el criterio.
     */
    List<model.entidades.Escola> obtenirEscolesAmbRestriccionsActives();
}
