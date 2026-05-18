package model.dao;

import model.entidades.Escalador;
import java.util.List;

public interface EscaladorDAO {
    /**
     * Crea un nou escalador a la base de dades.
     * @param escalador L'objecte Escalador a desar.
     * @return true si s'ha creat correctament, false si no.
     */
    boolean create(Escalador escalador);

    /**
     * Llegeix un escalador de la base de dades a partir del seu ID.
     * @param id L'ID de l'escalador a cercar.
     * @return L'objecte Escalador si es troba, null si no.
     */
    Escalador getById(int id);

    /**
     * Retorna tots els escaladors de la base de dades.
     * @return Una llista de tots els escaladors.
     */
    List<Escalador> getAll();

    /**
     * Actualitza les dades d'un escalador a la base de dades.
     * @param escalador L'objecte Escalador amb les dades actualitzades.
     * @return true si s'ha actualitzat correctament, false si no.
     */
    boolean update(Escalador escalador);

    /**
     * Elimina un escalador de la base de dades.
     * @param id L'ID de l'escalador a eliminar.
     * @return true si s'ha eliminat correctament, false si no.
     */
    boolean delete(int id);
    java.util.Map<String, java.util.List<model.entidades.Escalador>> getEscaladorsGroupedByNivell();
}
