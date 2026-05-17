package model.dao;

import java.sql.Date;
import java.util.List;

public interface HistorialDAO {
    boolean addAscensio(int idEscalador, int idVia, Date data);
    List<java.util.Map<String, Object>> getAscensosByEscalador(int idEscalador);
    List<java.util.Map<String, Object>> getAscensosByVia(int idVia);
}
