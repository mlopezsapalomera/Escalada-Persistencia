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
    List<Via> buscarPorDificultat(String minGrau, String maxGrau);
    List<Via> buscarPorEstat(String estat);
    List<Via> getViesQueHanPassatAPteRecentment(int dies);
    List<Via> getViesMesLlarguesPerEscola(int idEscola, int limit);
    // Ensure estados are refreshed (e.g., vías whose data_finalitzacio_estat expired)
    void refreshEstados();
}