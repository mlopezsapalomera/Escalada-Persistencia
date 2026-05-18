package model.dao;
import model.entidades.Via;
import java.util.List;

public interface ViaDAO {
    boolean crear(Via via);
    Via obtenirPerId(int id);
    List<Via> obtenirTots();
    boolean actualitzar(Via via);
    boolean eliminar(int id);
    List<Via> obtenirViesDisponiblesPerEscola(int idEscola);
    List<Via> cercarPerDificultat(String minGrau, String maxGrau);
    List<Via> cercarPerEstat(String estat);
    List<Via> obtenirViesQueHanPassatAPteRecentment(int dies);
    List<Via> obtenirViesMesLlarguesPerEscola(int idEscola, int limit);
    // Asegura que los estados se actualizan (p. ej. vías cuya data_finalitzacio_estat ha caducado)
    void actualitzarEstats();
}