package model.entidades;

public class Escola {
    private int id;
    private String nom;
    private String poblacio;
    private String aproximacio;
    private int numVies;
    private Popularitat popularitat;

    public enum Popularitat {
        BAIXA,
        MITJANA,
        ALTA
    }

    public Escola() {}
    public Escola(String nom, String poblacio, String aproximacio, int numVies, Popularitat popularitat) {
        this.nom = nom;
        this.poblacio = poblacio;
        this.aproximacio = aproximacio;
        this.numVies = numVies;
        this.popularitat = popularitat;
    }

    // Getters y Setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPoblacio() { return poblacio; }
    public void setPoblacio(String poblacio) { this.poblacio = poblacio; }
    public String getAproximacio() { return aproximacio; }
    public void setAproximacio(String aproximacio) { this.aproximacio = aproximacio; }
    public int getNumVies() { return numVies; }
    public void setNumVies(int numVies) { this.numVies = numVies; }
    public Popularitat getPopularitat() { return popularitat; }
    public void setPopularitat(Popularitat popularitat) { this.popularitat = popularitat; }
}