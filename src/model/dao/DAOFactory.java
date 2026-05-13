package model.dao;

import model.dao.mysql.MySqlEscaladorDAOImpl;

public abstract class DAOFactory {

    // Constants per identificar els tipus de fàbrica de DAO.
    public static final int MYSQL = 1;
    // public static final int POSTGRESQL = 2; // Exemple per a futura implementació

    // Mètodes abstractes per obtenir cada tipus de DAO
    public abstract EscaladorDAO getEscaladorDAO();
    // public abstract EscolaDAO getEscolaDAO(); // Per a futures implementacions

    /**
     * Retorna la fàbrica de DAO correcta segons el tipus de base de dades.
     * Aquest mètode amaga la complexitat de la creació.
     */
    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
            case MYSQL:
                // Retorna una implementació anònima de la fàbrica per a MySQL
                return new DAOFactory() {
                    @Override
                    public EscaladorDAO getEscaladorDAO() {
                        return new MySqlEscaladorDAOImpl();
                    }

                    // @Override
                    // public EscolaDAO getEscolaDAO() {
                    //     return new MySqlEscolaDAOImpl();
                    // }
                };
            // case POSTGRESQL:
            //     // Aquí aniria la implementació per a PostgreSQL
            default:
                throw new IllegalArgumentException("Tipus de fàbrica de DAO no suportat.");
        }
    }
}
