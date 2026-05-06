package model.entidades;

import java.util.Date;

public abstract class Via {
    private int id;
    private Sector sector;
    private Escola escola;
    private Escalador creador;
    private String nom;
    private String grauGlobal;
    private Orientacio orientacio;
    private Estat estat;
    private Date dataFinalitzacioEstat;
    private TipusRoca tipusRoca;
    private TipusVia tipusVia;
    private String restriccions;

    public enum Orientacio { N, NE, NO, SE, SO, E, O, S }
    public enum Estat { APTE, CONSTRUCCIO, TANCADA }
    public enum TipusRoca { CONGLOMERAT, GRANIT, CALCARIA, ARENISCA, ALTRES }
    public enum TipusVia { ESPORTIVA, CLASSICA, GEL }

    public Via() {}

    public Via(Sector sector, Escola escola, Escalador creador, String nom, String grauGlobal, Orientacio orientacio, Estat estat, Date dataFinalitzacioEstat, TipusRoca tipusRoca, TipusVia tipusVia, String restriccions) {
        this.sector = sector;
        this.escola = escola;
        this.creador = creador;
        this.nom = nom;
        this.grauGlobal = grauGlobal;
        this.orientacio = orientacio;
        this.estat = estat;
        this.dataFinalitzacioEstat = dataFinalitzacioEstat;
        this.tipusRoca = tipusRoca;
        this.tipusVia = tipusVia;
        this.restriccions = restriccions;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Sector getSector() { return sector; }
    public void setSector(Sector sector) { this.sector = sector; }
    public Escola getEscola() { return escola; }
    public void setEscola(Escola escola) { this.escola = escola; }
    public Escalador getCreador() { return creador; }
    public void setCreador(Escalador creador) { this.creador = creador; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getGrauGlobal() { return grauGlobal; }
    public void setGrauGlobal(String grauGlobal) { this.grauGlobal = grauGlobal; }
    public Orientacio getOrientacio() { return orientacio; }
    public void setOrientacio(Orientacio orientacio) { this.orientacio = orientacio; }
    public Estat getEstat() { return estat; }
    public void setEstat(Estat estat) { this.estat = estat; }
    public Date getDataFinalitzacioEstat() { return dataFinalitzacioEstat; }
    public void setDataFinalitzacioEstat(Date dataFinalitzacioEstat) { this.dataFinalitzacioEstat = dataFinalitzacioEstat; }
    public TipusRoca getTipusRoca() { return tipusRoca; }
    public void setTipusRoca(TipusRoca tipusRoca) { this.tipusRoca = tipusRoca; }
    public TipusVia getTipusVia() { return tipusVia; }
    public void setTipusVia(TipusVia tipusVia) { this.tipusVia = tipusVia; }
    public String getRestriccions() { return restriccions; }
    public void setRestriccions(String restriccions) { this.restriccions = restriccions; }
}
