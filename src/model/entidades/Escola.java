package model.entidades;

/**
 * Entidad que representa una escuela de escalada.
 */
public class Escola {
    private int id;
    private String nom;
    private String lloc;
    private String aproximacio;
    private int numVies;
    private Popularitat popularitat;

    /**
     * Nivel de popularidad de la escuela.
     */
    public enum Popularitat {
        BAIXA,
        MITJANA,
        ALTA
    }

    /**
     * Constructor vacío.
     */
    public Escola() {}

    /**
     * Constructor con los datos principales de la escuela.
     * @param nom nombre de la escuela.
     * @param lloc ubicación principal.
     * @param aproximacio información de aproximación.
     * @param numVies número de vías.
     * @param popularitat popularidad de la escuela.
     */
    public Escola(String nom, String lloc, String aproximacio, int numVies, Popularitat popularitat) {
        this.nom = nom;
        this.lloc = lloc;
        this.aproximacio = aproximacio;
        this.numVies = numVies;
        this.popularitat = popularitat;
    }

    /** @return identificador de la escuela. */
    public int getId() {
        return id;
    }

    /** @param id identificador de la escuela. */
    public void setId(int id) {
        this.id = id;
    }

    /** @return nombre de la escuela. */
    public String getNom() {
        return nom;
    }

    /** @param nom nombre de la escuela. */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /** @return ubicación de la escuela. */
    public String getLloc() {
        return lloc;
    }

    /** @param lloc ubicación de la escuela. */
    public void setLloc(String lloc) {
        this.lloc = lloc;
    }

    /** @return descripción de aproximación. */
    public String getAproximacio() {
        return aproximacio;
    }

    /** @param aproximacio descripción de aproximación. */
    public void setAproximacio(String aproximacio) {
        this.aproximacio = aproximacio;
    }

    /** @return número de vías. */
    public int getNumVies() {
        return numVies;
    }

    /** @param numVies número de vías. */
    public void setNumVies(int numVies) {
        this.numVies = numVies;
    }

    /** @return popularidad de la escuela. */
    public Popularitat getPopularitat() {
        return popularitat;
    }

    /** @param popularitat popularidad de la escuela. */
    public void setPopularitat(Popularitat popularitat) {
        this.popularitat = popularitat;
    }

    @Override
    public String toString() {
        return "Escola{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", lloc='" + lloc + '\'' +
                ", popularitat=" + popularitat +
                ", numVies=" + numVies +
                '}';
    }
}