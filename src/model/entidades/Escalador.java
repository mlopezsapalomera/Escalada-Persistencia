package model.entidades;

public class Escalador {
    private int id;
    private String nom;
    private String alias;
    private int edat;
    private String nivellMaxim;
    private Estil estilPreferit;

    public enum Estil {
        ESPORTIVA,
        CLASSICA,
        GEL
    }

    public Escalador() {}

    public Escalador(String nom, String alias, int edat, String nivellMaxim, Estil estilPreferit) {
        this.nom = nom;
        this.alias = alias;
        this.edat = edat;
        this.nivellMaxim = nivellMaxim;
        this.estilPreferit = estilPreferit;
    }

    // Getters y Setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public int getEdat() { return edat; }
    public void setEdat(int edat) { this.edat = edat; }
    public String getNivellMaxim() { return nivellMaxim; }
    public void setNivellMaxim(String nivellMaxim) { this.nivellMaxim = nivellMaxim; }
    public Estil getEstilPreferit() { return estilPreferit; }
    public void setEstilPreferit(Estil estilPreferit) { this.estilPreferit = estilPreferit; }
}