package model.entidades;
import java.math.BigDecimal;

/**
 * Entidad que representa un sector dentro de una escuela.
 */
public class Sector {
    private int id;
    private Escola escola;
    private String nom;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String aproximacio;
    private int numVies;
    private Popularitat popularitat;
    private String restriccions;
    private TipusSector tipusSector;

    /**
     * Nivel de popularidad del sector.
     */
    public enum Popularitat {
        BAIXA,
        MITJANA,
        ALTA
    }

    /**
     * Tipo de sector según su medio principal.
     */
    public enum TipusSector {
        GEL,
        MIXTE_ROCA
    }

    /**
     * Constructor vacío.
     */
    public Sector() {}

    /**
     * Constructor con los datos principales del sector.
     * @param escola escuela a la que pertenece.
     * @param nom nombre del sector.
     * @param latitud latitud geográfica.
     * @param longitud longitud geográfica.
     * @param aproximacio descripción de aproximación.
     * @param numVies número de vías del sector.
     * @param popularitat popularidad del sector.
     * @param restriccions restricciones aplicables.
     * @param tipusSector tipo de sector.
     */
    public Sector(Escola escola, String nom, BigDecimal latitud, BigDecimal longitud, String aproximacio, int numVies, Popularitat popularitat, String restriccions, TipusSector tipusSector) {
        this.escola = escola;
        this.nom = nom;
        this.latitud = latitud;
        this.longitud = longitud;
        this.aproximacio = aproximacio;
        this.numVies = numVies;
        this.popularitat = popularitat;
        this.restriccions = restriccions;
        this.tipusSector = tipusSector;
    }

    /** @return identificador del sector. */
    public int getId() {
        return id;
    }

    /** @param id identificador del sector. */
    public void setId(int id) {
        this.id = id;
    }

    /** @return escuela del sector. */
    public Escola getEscola() {
        return escola;
    }

    /** @param escola escuela del sector. */
    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    /** @return nombre del sector. */
    public String getNom() {
        return nom;
    }

    /** @param nom nombre del sector. */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /** @return latitud del sector. */
    public BigDecimal getLatitud() {
        return latitud;
    }

    /** @param latitud latitud del sector. */
    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    /** @return longitud del sector. */
    public BigDecimal getLongitud() {
        return longitud;
    }

    /** @param longitud longitud del sector. */
    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    /** @return descripción de aproximación al sector. */
    public String getAproximacio() {
        return aproximacio;
    }

    /** @param aproximacio descripción de aproximación al sector. */
    public void setAproximacio(String aproximacio) {
        this.aproximacio = aproximacio;
    }

    /** @return número de vías del sector. */
    public int getNumVies() {
        return numVies;
    }

    /** @param numVies número de vías del sector. */
    public void setNumVies(int numVies) {
        this.numVies = numVies;
    }

    /** @return popularidad del sector. */
    public Popularitat getPopularitat() {
        return popularitat;
    }

    /** @param popularitat popularidad del sector. */
    public void setPopularitat(Popularitat popularitat) {
        this.popularitat = popularitat;
    }

    /** @return restricciones del sector. */
    public String getRestriccions() {
        return restriccions;
    }

    /** @param restriccions restricciones del sector. */
    public void setRestriccions(String restriccions) {
        this.restriccions = restriccions;
    }

    /** @return tipo de sector. */
    public TipusSector getTipusSector() {
        return tipusSector;
    }

    /** @param tipusSector tipo de sector. */
    public void setTipusSector(TipusSector tipusSector) {
        this.tipusSector = tipusSector;
    }
}