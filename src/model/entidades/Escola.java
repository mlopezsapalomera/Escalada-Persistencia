package model.entidades;
public class Escola {
    private int id;
    private String nom;
    private String lloc;
    private String aproximacio;
    private int numVies; // Aquest camp es calcularà, però el mantenim per si es vol desar
    private Popularitat popularitat;

    public enum Popularitat {
        BAIXA,
        MITJANA,
        ALTA
    }

    // Constructor buit
    public Escola() {}

    // Constructor amb paràmetres
    public Escola(String nom, String lloc, String aproximacio, int numVies, Popularitat popularitat) {
        this.nom = nom;
        this.lloc = lloc;
        this.aproximacio = aproximacio;
        this.numVies = numVies;
        this.popularitat = popularitat;
    }

    // Getters i Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLloc() {
        return lloc;
    }

    public void setLloc(String lloc) {
        this.lloc = lloc;
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