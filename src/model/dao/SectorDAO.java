package model.dao;

import model.entidades.Sector;
import java.util.List;

public interface SectorDAO {
    boolean create(Sector sector);
    Sector getById(int id);
    List<Sector> getAll();
    boolean update(Sector sector);
    boolean delete(int id);
    List<model.entidades.Sector> getSectorsWithMoreThanXAvailableVies(int x);
}