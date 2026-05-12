package model.entidades;

import java.util.ArrayList;
import java.util.List;

public class Via {

    // Enums ajustats a la BD
    public enum Estil       { ESPORTIVA, CLASSICA, GEL }
    public enum Estat       { APTE, CONSTRUCCIO, TANCADA }  // ← corregit
    public enum TipusRoca   { CONGLOMERAT, GRANIT, CALCARIA, ARENISCA, ALTRES }
    public enum Orientacio  { N, NE, NO, SE, SO, E, O, S }

    // Atributs base (taula 'vies')
    private int id;
    private Sector sector;
    private String nom;
    private Estil estil;
    private Estat estat;
    private Escalador creadaPer;
    private String restriccions;
    private TipusRoca tipusRoca;
    private Orientacio orientacio;

    // Atributs per a Esportiva (taula 'detalls_esportiva')
    private int llargadaTotal;
    private String dificultatEsportiva;
    private String ancoratges;

    // Atributs per a Clàssica/Gel (taula 'llargs')
    private List<Llarg> llistaLlargs = new ArrayList<>();

    // Constructor buit
    public Via() {}

    // Constructor complet (per les subclasses)
    public Via(Sector sector, Escalador creadaPer, String nom,
               String grauGlobal, Orientacio orientacio, Estat estat,
               TipusRoca tipusRoca, Estil estil, String restriccions) {
        this.sector = sector;
        this.creadaPer = creadaPer;
        this.nom = nom;
        this.dificultatEsportiva = grauGlobal;
        this.orientacio = orientacio;
        this.estat = estat;
        this.tipusRoca = tipusRoca;
        this.estil = estil;
        this.restriccions = restriccions;
    }

    // Getters i Setters
    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public Sector getSector()                   { return sector; }
    public void setSector(Sector sector)        { this.sector = sector; }
    public String getNom()                      { return nom; }
    public void setNom(String nom)              { this.nom = nom; }
    public Estil getEstil()                     { return estil; }
    public void setEstil(Estil estil)           { this.estil = estil; }
    public Estat getEstat()                     { return estat; }
    public void setEstat(Estat estat)           { this.estat = estat; }
    public Escalador getCreadaPer()             { return creadaPer; }
    public void setCreadaPer(Escalador e)       { this.creadaPer = e; }
    public String getRestriccions()             { return restriccions; }
    public void setRestriccions(String r)       { this.restriccions = r; }
    public TipusRoca getTipusRoca()             { return tipusRoca; }
    public void setTipusRoca(TipusRoca t)       { this.tipusRoca = t; }
    public Orientacio getOrientacio()           { return orientacio; }
    public void setOrientacio(Orientacio o)     { this.orientacio = o; }
    public int getLlargadaTotal()               { return llargadaTotal; }
    public void setLlargadaTotal(int l)         { this.llargadaTotal = l; }
    public String getDificultatEsportiva()      { return dificultatEsportiva; }
    public void setDificultatEsportiva(String d){ this.dificultatEsportiva = d; }
    public String getAncoratges()               { return ancoratges; }
    public void setAncoratges(String a)         { this.ancoratges = a; }
    public List<Llarg> getLlistaLlargs()        { return llistaLlargs; }
    public void setLlistaLlargs(List<Llarg> l)  { this.llistaLlargs = l; }
}