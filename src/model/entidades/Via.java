package model.entidades;

import java.util.ArrayList;
import java.util.List;

public class Via {
    // 1. ENUMS (Hem canviat TipusVia per Estil)
    public enum Estil { ESPORTIVA, CLASSICA, GEL }
    public enum Estat { APTE, CONSTRUCCIO, TANCADA }
    public enum TipusRoca { CONGLOMERAT, GRANIT, CALCARIA, ARENISCA, ALTRES }
    public enum Orientacio { N, NE, NO, SE, SO, E, O, S }

    // 2. ATRIBUTS BASE
    private int id;
    private Sector sector;
    private String nom;
    private Estil estil; // Ara l'Enum i la variable es diuen igual (Estil)
    private Estat estat;
    private Escalador creadaPer;
    private String restriccions;
    private String grauGlobal; // Afegit perquè ho demanaven les filles

    // 3. ATRIBUTS ESPORTIVA
    private int llargadaTotal;
    private String dificultatEsportiva;
    private Orientacio orientacio;
    private String ancoratges;
    private String tipusRoca;

    // 4. ATRIBUTS CLÀSSICA/GEL
    private List<Llarg> llistaLlargs = new ArrayList<>();

    // CONSTRUCTOR BUIT ÚNIC
    public Via() {}


    private java.sql.Date dataFinalitzacioEstat;


    // --- GETTERS I SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Sector getSector() { return sector; }
    public void setSector(Sector sector) { this.sector = sector; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Estil getEstil() { return estil; }
    public void setEstil(Estil estil) { this.estil = estil; }

    public Estat getEstat() { return estat; }
    public void setEstat(Estat estat) { this.estat = estat; }

    public Escalador getCreadaPer() { return creadaPer; }
    public void setCreadaPer(Escalador creadaPer) { this.creadaPer = creadaPer; }

    public String getRestriccions() { return restriccions; }
    public void setRestriccions(String restriccions) { this.restriccions = restriccions; }
    
    public String getGrauGlobal() { return grauGlobal; }
    public void setGrauGlobal(String grauGlobal) { this.grauGlobal = grauGlobal; }

    public int getLlargadaTotal() { return llargadaTotal; }
    public void setLlargadaTotal(int llargadaTotal) { this.llargadaTotal = llargadaTotal; }

    public String getDificultatEsportiva() { return dificultatEsportiva; }
    public void setDificultatEsportiva(String dificultatEsportiva) { this.dificultatEsportiva = dificultatEsportiva; }

    public Orientacio getOrientacio() { return orientacio; }
    public void setOrientacio(Orientacio orientacio) { this.orientacio = orientacio; }

    public String getAncoratges() { return ancoratges; }
    public void setAncoratges(String ancoratges) { this.ancoratges = ancoratges; }

    public String getTipusRoca() { return tipusRoca; }
    public void setTipusRoca(String tipusRoca) { this.tipusRoca = tipusRoca; }

    public List<Llarg> getLlistaLlargs() { return llistaLlargs; }
    public void setLlistaLlargs(List<Llarg> llistaLlargs) { this.llistaLlargs = llistaLlargs; }

    public java.sql.Date getDataFinalitzacioEstat() { return dataFinalitzacioEstat; }
    public void setDataFinalitzacioEstat(java.sql.Date dataFinalitzacioEstat) { this.dataFinalitzacioEstat = dataFinalitzacioEstat; }
}