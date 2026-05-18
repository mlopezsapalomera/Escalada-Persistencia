package model.entidades;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad base que representa una vía de escalada.
 */
public class Via {
    /** Estilo principal de la vía. */
    public enum Estil { ESPORTIVA, CLASSICA, GEL }
    /** Estado operativo de la vía. */
    public enum Estat { APTE, CONSTRUCCIO, TANCADA }
    /** Tipo de roca predominante. */
    public enum TipusRoca { CONGLOMERAT, GRANIT, CALCARIA, ARENISCA, ALTRES }
    /** Orientación geográfica principal. */
    public enum Orientacio { N, NE, NO, SE, SO, E, O, S }

    private int id;
    private Sector sector;
    private String nom;
    private Estil estil;
    private Estat estat;
    private Escalador creadaPer;
    private String restriccions;
    private String grauGlobal;

    private int llargadaTotal;
    private String dificultatEsportiva;
    private Orientacio orientacio;
    private String ancoratges;
    private String tipusRoca;

    private List<Llarg> llistaLlargs = new ArrayList<>();

    /**
     * Constructor vacío.
     */
    public Via() {}


    private java.sql.Date dataFinalitzacioEstat;


    /** @return identificador de la vía. */
    public int getId() { return id; }
    /** @param id identificador de la vía. */
    public void setId(int id) { this.id = id; }

    /** @return sector al que pertenece la vía. */
    public Sector getSector() { return sector; }
    /** @param sector sector al que pertenece la vía. */
    public void setSector(Sector sector) { this.sector = sector; }

    /** @return nombre de la vía. */
    public String getNom() { return nom; }
    /** @param nom nombre de la vía. */
    public void setNom(String nom) { this.nom = nom; }

    /** @return estilo de la vía. */
    public Estil getEstil() { return estil; }
    /** @param estil estilo de la vía. */
    public void setEstil(Estil estil) { this.estil = estil; }

    /** @return estado de la vía. */
    public Estat getEstat() { return estat; }
    /** @param estat estado de la vía. */
    public void setEstat(Estat estat) { this.estat = estat; }

    /** @return escalador que creó la vía. */
    public Escalador getCreadaPer() { return creadaPer; }
    /** @param creadaPer escalador que creó la vía. */
    public void setCreadaPer(Escalador creadaPer) { this.creadaPer = creadaPer; }

    /** @return restricciones de la vía. */
    public String getRestriccions() { return restriccions; }
    /** @param restriccions restricciones de la vía. */
    public void setRestriccions(String restriccions) { this.restriccions = restriccions; }
    
    /** @return grado global de la vía. */
    public String getGrauGlobal() { return grauGlobal; }
    /** @param grauGlobal grado global de la vía. */
    public void setGrauGlobal(String grauGlobal) { this.grauGlobal = grauGlobal; }

    /** @return longitud total de la vía. */
    public int getLlargadaTotal() { return llargadaTotal; }
    /** @param llargadaTotal longitud total de la vía. */
    public void setLlargadaTotal(int llargadaTotal) { this.llargadaTotal = llargadaTotal; }

    /** @return dificultad de la vía deportiva. */
    public String getDificultatEsportiva() { return dificultatEsportiva; }
    /** @param dificultatEsportiva dificultad de la vía deportiva. */
    public void setDificultatEsportiva(String dificultatEsportiva) { this.dificultatEsportiva = dificultatEsportiva; }

    /** @return orientación de la vía. */
    public Orientacio getOrientacio() { return orientacio; }
    /** @param orientacio orientación de la vía. */
    public void setOrientacio(Orientacio orientacio) { this.orientacio = orientacio; }

    /** @return anclajes de la vía. */
    public String getAncoratges() { return ancoratges; }
    /** @param ancoratges anclajes de la vía. */
    public void setAncoratges(String ancoratges) { this.ancoratges = ancoratges; }

    /** @return tipo de roca de la vía. */
    public String getTipusRoca() { return tipusRoca; }
    /** @param tipusRoca tipo de roca de la vía. */
    public void setTipusRoca(String tipusRoca) { this.tipusRoca = tipusRoca; }

    /** @return lista de largos de la vía. */
    public List<Llarg> getLlistaLlargs() { return llistaLlargs; }
    /** @param llistaLlargs lista de largos de la vía. */
    public void setLlistaLlargs(List<Llarg> llistaLlargs) { this.llistaLlargs = llistaLlargs; }

    /** @return fecha de fin del estado temporal de la vía. */
    public java.sql.Date getDataFinalitzacioEstat() { return dataFinalitzacioEstat; }
    /** @param dataFinalitzacioEstat fecha de fin del estado temporal de la vía. */
    public void setDataFinalitzacioEstat(java.sql.Date dataFinalitzacioEstat) { this.dataFinalitzacioEstat = dataFinalitzacioEstat; }
}