package model.dao.mysql;

import model.dao.DAOFactory;
import model.dao.EscaladorDAO;

public class MySqlDAOFactory extends DAOFactory {

    /**
     * Retorna una instància de la implementació de MySQL per a EscaladorDAO.
     * @return un objecte MySqlEscaladorDAOImpl.
     */
    @Override
    public EscaladorDAO getEscaladorDAO() {
        return new MySqlEscaladorDAOImpl();
    }

    // Aquí anirien les implementacions per a altres DAOs quan es creïn
    // public EscolaDAO getEscolaDAO() {
    //     return new MySqlEscolaDAOImpl();
    // }
}
