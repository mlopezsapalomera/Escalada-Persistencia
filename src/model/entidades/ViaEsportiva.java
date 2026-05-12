package model.entidades;

public class ViaEsportiva extends Via {

    public enum Ancoratge {
        SPITS, PARABOLTS, QUIMICS
    }

    public ViaEsportiva() {
        super();
        this.setEstil(Via.Estil.ESPORTIVA);
    }

    // Constructor estructurat amb setters (més segur)
    public ViaEsportiva(Sector sector, Escalador creador, String nom, Orientacio orientacio, Estat estat, String tipusRoca, int llargada, Ancoratge ancoratge) {
        super();
        this.setSector(sector);
        this.setCreadaPer(creador);
        this.setNom(nom);
        this.setOrientacio(orientacio);
        this.setEstat(estat);
        this.setTipusRoca(tipusRoca);
        this.setLlargadaTotal(llargada);
        this.setAncoratges(ancoratge.name().toLowerCase());
        this.setEstil(Via.Estil.ESPORTIVA);
    }
}