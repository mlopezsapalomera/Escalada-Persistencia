package model.dao;

import model.entidades.Escola;
import java.util.List;

public interface EscolaDAO {
    /**
     * Crea una nova escola a la base de dades.
     * @param escola L'objecte Escola a desar.
     * @return true si s'ha creat correctament, false si no.
     */
    boolean create(Escola escola);

    /**
     * Llegeix una escola de la base de dades a partir del seu ID.
     * @param id L'ID de l'escola a cercar.
     * @return L'objecte Escola si es troba, null si no.
     */
    Escola getById(int id);

    /**
     * Retorna totes les escoles de la base de dades.
     * @return Una llista de totes les escoles.
     */
    List<Escola> getAll();

    /**
     * Actualitza les dades d'una escola a la base de dades.
     * @param escola L'objecte Escola amb les dades actualitzades.
     * @return true si s'ha actualitzat correctament, false si no.
     */
    boolean update(Escola escola);

    /**
     * Elimina una escola de la base de dades.
     * @param id L'ID de l'escola a eliminar.
     * @return true si s'ha eliminat correctament, false si no.
     */
    boolean delete(int id);
}
