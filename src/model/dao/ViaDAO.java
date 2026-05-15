package model.dao;

import model.entidades.Via;
import java.util.List;

public interface ViaDAO {
    boolean create(Via via);
    Via getById(int id);
    List<Via> getAll();
    boolean update(Via via);
    boolean delete(int id);
    List<Via> getDisponiblesPerEscola(int idEscola);
}