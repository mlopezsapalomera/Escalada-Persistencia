package model.entidades;
import java.util.List; // Importar List

public class Escalador {
    private int id;
    private String nom;
    private String alias;
    private int edat;
    private String nivell; // Nivel máximo que ha assolido
    private String nomViaNivellMaxim; // Nombre de la vía donde ha assolido el nivel máximo
    private Estil estilPreferit;
    private List<String> historial; // PENDENT segons enunciat, però el deixem preparat

    public enum Estil {
        ESPORTIVA,
        CLASSICA,
        GEL
    }

    // Constructor buit
    public Escalador() {}

    // Constructor amb paràmetres
    public Escalador(String nom, String alias, int edat, String nivell, String nomViaNivellMaxim, Estil estilPreferit) {
        this.nom = nom;
        this.alias = alias;
        this.edat = edat;
        this.nivell = nivell;
        this.nomViaNivellMaxim = nomViaNivellMaxim;
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
    public String getNivell() { return nivell; }
    public void setNivell(String nivell) { this.nivell = nivell; }
    public String getNomViaNivellMaxim() { return nomViaNivellMaxim; }
    public void setNomViaNivellMaxim(String nomViaNivellMaxim) { this.nomViaNivellMaxim = nomViaNivellMaxim; }
    public Estil getEstilPreferit() { return estilPreferit; }
    public void setEstilPreferit(Estil estilPreferit) { this.estilPreferit = estilPreferit; }
    public List<String> getHistorial() { return historial; }
    public void setHistorial(List<String> historial) { this.historial = historial; }

    @Override
    public String toString() {
        return "Escalador{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", alias='" + alias + '\'' +
                ", edat=" + edat +
                ", nivell='" + nivell + '\'' +
                ", nomViaNivellMaxim='" + nomViaNivellMaxim + '\'' +
                ", estilPreferit=" + estilPreferit +
                '}';
    }
}