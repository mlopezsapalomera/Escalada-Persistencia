package model.dao;
import model.dao.mysql.MySqlDAOFactory;

public abstract class DAOFactory {
    public static final int MYSQL = 1;

    public abstract EscaladorDAO obtenirEscaladorDAO();
    public abstract EscolaDAO obtenirEscolaDAO();
    public abstract SectorDAO obtenirSectorDAO(); 
    public abstract ViaDAO obtenirViaDAO();       
    public abstract HistorialDAO obtenirHistorialDAO();

    public static DAOFactory obtenirDAOFactory(int whichFactory) {
        switch (whichFactory) {
            case MYSQL:
                return new MySqlDAOFactory();
            default:
                throw new IllegalArgumentException("Tipus de fàbrica no suportat.");
        }
    }
}