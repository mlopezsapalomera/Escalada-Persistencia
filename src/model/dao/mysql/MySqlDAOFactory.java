package model.dao.mysql;
import model.dao.*;

public class MySqlDAOFactory extends DAOFactory {

    @Override
    public EscaladorDAO obtenirEscaladorDAO() {
        return new MySqlEscaladorDAOImpl();
    }

    @Override
    public EscolaDAO obtenirEscolaDAO() {
        return new MySqlEscolaDAOImpl();
    }

    @Override
    public SectorDAO obtenirSectorDAO() {
        return new MySqlSectorDAOImpl();
    }

    @Override
    public ViaDAO obtenirViaDAO() {
        return new MySqlViaDAOImpl();
    }

    @Override
    public HistorialDAO obtenirHistorialDAO() {
        return new MySqlHistorialDAOImpl();
    }
}