package model.dao;
import model.entidades.Escalador;
import java.util.List;

public interface EscaladorDAO {
    /**
     * Crea un nuevo escalador en la base de datos.
     * @param escalador objeto con los datos del escalador.
     * @return true si se crea correctamente; false en caso contrario.
     */
    boolean crear(Escalador escalador);

    /**
     * Obtiene un escalador por su identificador.
     * @param id id del escalador.
     * @return escalador encontrado o null si no existe.
     */
    Escalador obtenirPerId(int id);

    /**
     * Recupera todos los escaladores registrados.
     * @return lista completa de escaladores.
     */
    List<Escalador> obtenirTots();

    /**
     * Actualiza los datos de un escalador existente.
     * @param escalador objeto con los datos actualizados.
     * @return true si la actualización se realiza; false si falla.
     */
    boolean actualitzar(Escalador escalador);

    /**
     * Elimina un escalador por su id.
     * @param id id del escalador a eliminar.
     * @return true si se elimina correctamente; false en caso contrario.
     */
    boolean eliminar(int id);

    /**
     * Agrupa escaladores por nivel de experiencia.
     * @return mapa nivel -> lista de escaladores de ese nivel.
     */
    java.util.Map<String, java.util.List<model.entidades.Escalador>> obtenirEscaladorsAgrupatsPerNivell();
}
