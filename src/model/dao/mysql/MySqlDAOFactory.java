package model.dao.mysql;
import model.dao.*;

public class MySqlDAOFactory extends DAOFactory {

    @Override
    /** @return implementación MySQL del DAO de escaladores. */
    public EscaladorDAO obtenirEscaladorDAO() {
        return new MySqlEscaladorDAOImpl();
    }

    @Override
    /** @return implementación MySQL del DAO de escuelas. */
    public EscolaDAO obtenirEscolaDAO() {
        return new MySqlEscolaDAOImpl();
    }

    @Override
    /** @return implementación MySQL del DAO de sectores. */
    public SectorDAO obtenirSectorDAO() {
        return new MySqlSectorDAOImpl();
    }

    @Override
    /** @return implementación MySQL del DAO de vías. */
    public ViaDAO obtenirViaDAO() {
        return new MySqlViaDAOImpl();
    }

    @Override
    /** @return implementación MySQL del DAO de historial. */
    public HistorialDAO obtenirHistorialDAO() {
        return new MySqlHistorialDAOImpl();
    }
}