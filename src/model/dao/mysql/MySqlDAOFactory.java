package model.dao.mysql;

import model.dao.DAOFactory;
import model.dao.EscaladorDAO;
import model.dao.EscolaDAO;

public class MySqlDAOFactory extends DAOFactory {

    /**
     * Retorna una instància de la implementació de MySQL per a EscaladorDAO.
     * @return un objecte MySqlEscaladorDAOImpl.
     */
    @Override
    public EscaladorDAO getEscaladorDAO() {
        return new MySqlEscaladorDAOImpl();
    }

    /**
     * Retorna una instància de la implementació de MySQL per a EscolaDAO.
     * @return un objecte MySqlEscolaDAOImpl.
     */
    @Override
    public EscolaDAO getEscolaDAO() {
        return new MySqlEscolaDAOImpl();
    }

    // Aquí anirien les implementacions per a altres DAOs quan es creïn
    // public EscolaDAO getEscolaDAO() {
    //     return new MySqlEscolaDAOImpl();
    // }
}
