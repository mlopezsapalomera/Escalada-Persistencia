package model.entidades;
import java.math.BigDecimal;

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

    public enum Popularitat {
        BAIXA,
        MITJANA,
        ALTA
    }

    public enum TipusSector {
        GEL,
        MIXTE_ROCA
    }

    public Sector() {}

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

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public String getAproximacio() {
        return aproximacio;
    }

    public void setAproximacio(String aproximacio) {
        this.aproximacio = aproximacio;
    }

    public int getNumVies() {
        return numVies;
    }

    public void setNumVies(int numVies) {
        this.numVies = numVies;
    }

    public Popularitat getPopularitat() {
        return popularitat;
    }

    public void setPopularitat(Popularitat popularitat) {
        this.popularitat = popularitat;
    }

    public String getRestriccions() {
        return restriccions;
    }

    public void setRestriccions(String restriccions) {
        this.restriccions = restriccions;
    }

    public TipusSector getTipusSector() {
        return tipusSector;
    }

    public void setTipusSector(TipusSector tipusSector) {
        this.tipusSector = tipusSector;
    }
}