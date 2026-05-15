package model.dao;

import model.dao.mysql.MySqlDAOFactory;

public abstract class DAOFactory {
    public static final int MYSQL = 1;

    public abstract EscaladorDAO getEscaladorDAO();
    public abstract EscolaDAO getEscolaDAO();
    public abstract SectorDAO getSectorDAO(); 
    public abstract ViaDAO getViaDAO();       

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
            case MYSQL:
                return new MySqlDAOFactory();
            default:
                throw new IllegalArgumentException("Tipus de fàbrica no suportat.");
        }
    }
}