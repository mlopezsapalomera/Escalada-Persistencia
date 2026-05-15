package model.dao.mysql;

import model.dao.*;

public class MySqlDAOFactory extends DAOFactory {

    @Override
    public EscaladorDAO getEscaladorDAO() {
        return new MySqlEscaladorDAOImpl();
    }

    @Override
    public EscolaDAO getEscolaDAO() {
        return new MySqlEscolaDAOImpl();
    }

    @Override
    public SectorDAO getSectorDAO() {
        return new MySqlSectorDAOImpl();
    }

    @Override
    public ViaDAO getViaDAO() {
        return new MySqlViaDAOImpl();
    }
}