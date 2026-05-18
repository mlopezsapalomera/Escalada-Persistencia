package model.dao;
import java.sql.Date;
import java.util.List;

public interface HistorialDAO {
    /**
     * Registra una ascensión de un escalador en una vía para una fecha concreta.
     * @param idEscalador id del escalador.
     * @param idVia id de la vía ascendida.
     * @param data fecha de la ascensión.
     * @return true si se registra correctamente; false en caso contrario.
     */
    boolean afegirAscensio(int idEscalador, int idVia, Date data);

    /**
     * Recupera el historial de ascensiones de un escalador.
     * @param idEscalador id del escalador.
     * @return lista de filas con los datos del historial.
     */
    List<java.util.Map<String, Object>> obtenirAscensosPerEscalador(int idEscalador);

    /**
     * Recupera el historial de ascensiones asociadas a una vía.
     * @param idVia id de la vía.
     * @return lista de filas con los datos del historial.
     */
    List<java.util.Map<String, Object>> obtenirAscensosPerVia(int idVia);
}
