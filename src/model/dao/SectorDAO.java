package model.dao;
import model.entidades.Sector;
import java.util.List;

public interface SectorDAO {
    /**
     * Crea un nuevo sector.
     * @param sector objeto con los datos del sector.
     * @return true si se crea correctamente; false en caso contrario.
     */
    boolean crear(Sector sector);

    /**
     * Obtiene un sector por su id.
     * @param id id del sector.
     * @return sector encontrado o null si no existe.
     */
    Sector obtenirPerId(int id);

    /**
     * Recupera todos los sectores disponibles.
     * @return lista de sectores.
     */
    List<Sector> obtenirTots();

    /**
     * Actualiza los datos de un sector existente.
     * @param sector objeto con la información actualizada.
     * @return true si se actualiza correctamente; false si falla.
     */
    boolean actualitzar(Sector sector);

    /**
     * Elimina un sector por su id.
     * @param id id del sector a eliminar.
     * @return true si se elimina correctamente; false en caso contrario.
     */
    boolean eliminar(int id);

    /**
     * Obtiene los sectores con más de X vías disponibles.
     * @param x umbral mínimo de vías disponibles.
     * @return lista de sectores que superan el umbral.
     */
    List<model.entidades.Sector> obtenirSectorsAmbMesDeXViesDisponibles(int x);
}