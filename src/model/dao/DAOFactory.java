package model.dao;
import model.dao.mysql.MySqlDAOFactory;

/**
 * Fábrica abstracta de DAOs.
 * Centraliza la creación de implementaciones concretas según el motor de persistencia.
 */
public abstract class DAOFactory {
    /** Identificador de la fábrica MySQL. */
    public static final int MYSQL = 1;

    /** @return DAO para operaciones de escaladores. */
    public abstract EscaladorDAO obtenirEscaladorDAO();
    /** @return DAO para operaciones de escuelas. */
    public abstract EscolaDAO obtenirEscolaDAO();
    /** @return DAO para operaciones de sectores. */
    public abstract SectorDAO obtenirSectorDAO(); 
    /** @return DAO para operaciones de vías. */
    public abstract ViaDAO obtenirViaDAO();       
    /** @return DAO para operaciones de historial de ascensiones. */
    public abstract HistorialDAO obtenirHistorialDAO();

    /**
     * Devuelve la fábrica concreta en función del tipo solicitado.
     * @param whichFactory tipo de fábrica (por ejemplo, MYSQL).
     * @return instancia de {@link DAOFactory} adecuada.
     */
    public static DAOFactory obtenirDAOFactory(int whichFactory) {
        switch (whichFactory) {
            case MYSQL:
                return new MySqlDAOFactory();
            default:
                throw new IllegalArgumentException("Tipus de fàbrica no suportat.");
        }
    }
}