package model.entidades;
import java.util.List;

/**
 * Entidad que representa un escalador del sistema.
 */
public class Escalador {
    private int id;
    private String nom;
    private String alias;
    private int edat;
    private String nivell;
    private String nomViaNivellMaxim;
    private Estil estilPreferit;
    private List<String> historial;

    /**
     * Estilo de escalada preferido del escalador.
     */
    public enum Estil {
        ESPORTIVA,
        CLASSICA,
        GEL
    }

    /**
     * Constructor vacío.
     */
    public Escalador() {}

    /**
     * Constructor con los datos principales del escalador.
     * @param nom nombre del escalador.
     * @param alias alias o apodo.
     * @param edat edad.
     * @param nivell nivel máximo alcanzado.
     * @param nomViaNivellMaxim nombre de la vía donde alcanzó el nivel máximo.
     * @param estilPreferit estilo preferido.
     */
    public Escalador(String nom, String alias, int edat, String nivell, String nomViaNivellMaxim, Estil estilPreferit) {
        this.nom = nom;
        this.alias = alias;
        this.edat = edat;
        this.nivell = nivell;
        this.nomViaNivellMaxim = nomViaNivellMaxim;
        this.estilPreferit = estilPreferit;
    }

    /** @return identificador del escalador. */
    public int getId() { return id; }
    /** @param id identificador del escalador. */
    public void setId(int id) { this.id = id; }

    /** @return nombre del escalador. */
    public String getNom() { return nom; }
    /** @param nom nombre del escalador. */
    public void setNom(String nom) { this.nom = nom; }

    /** @return alias del escalador. */
    public String getAlias() { return alias; }
    /** @param alias alias del escalador. */
    public void setAlias(String alias) { this.alias = alias; }

    /** @return edad del escalador. */
    public int getEdat() { return edat; }
    /** @param edat edad del escalador. */
    public void setEdat(int edat) { this.edat = edat; }

    /** @return nivel máximo alcanzado. */
    public String getNivell() { return nivell; }
    /** @param nivell nivel máximo alcanzado. */
    public void setNivell(String nivell) { this.nivell = nivell; }

    /** @return nombre de la vía de nivel máximo. */
    public String getNomViaNivellMaxim() { return nomViaNivellMaxim; }
    /** @param nomViaNivellMaxim nombre de la vía de nivel máximo. */
    public void setNomViaNivellMaxim(String nomViaNivellMaxim) { this.nomViaNivellMaxim = nomViaNivellMaxim; }

    /** @return estilo preferido del escalador. */
    public Estil getEstilPreferit() { return estilPreferit; }
    /** @param estilPreferit estilo preferido. */
    public void setEstilPreferit(Estil estilPreferit) { this.estilPreferit = estilPreferit; }

    /** @return historial asociado al escalador. */
    public List<String> getHistorial() { return historial; }
    /** @param historial historial asociado al escalador. */
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