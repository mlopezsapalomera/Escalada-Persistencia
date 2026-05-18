package model.dao;
import model.entidades.Sector;
import java.util.List;

public interface SectorDAO {
    boolean crear(Sector sector);
    Sector obtenirPerId(int id);
    List<Sector> obtenirTots();
    boolean actualitzar(Sector sector);
    boolean eliminar(int id);
    List<model.entidades.Sector> obtenirSectorsAmbMesDeXViesDisponibles(int x);
}