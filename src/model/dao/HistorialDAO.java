package model.dao;
import java.sql.Date;
import java.util.List;

public interface HistorialDAO {
    boolean afegirAscensio(int idEscalador, int idVia, Date data);
    List<java.util.Map<String, Object>> obtenirAscensosPerEscalador(int idEscalador);
    List<java.util.Map<String, Object>> obtenirAscensosPerVia(int idVia);
}
