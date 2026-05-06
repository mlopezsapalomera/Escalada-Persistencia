package model.entidades;

import java.util.Date;

public class ViaEsportiva extends Via {
    private int llargada;
    private Ancoratge ancoratge;

    public enum Ancoratge {
        SPITS,
        PARABOLTS,
        QUIMICS
    }

    public ViaEsportiva() {
        super();
        setTipusVia(TipusVia.ESPORTIVA);
    }

    public ViaEsportiva(Sector sector, Escola escola, Escalador creador, String nom, String grauGlobal, Orientacio orientacio, Estat estat, Date dataFinalitzacioEstat, TipusRoca tipusRoca, String restriccions, int llargada, Ancoratge ancoratge) {
        super(sector, escola, creador, nom, grauGlobal, orientacio, estat, dataFinalitzacioEstat, tipusRoca, TipusVia.ESPORTIVA, restriccions);
        this.llargada = llargada;
        this.ancoratge = ancoratge;
    }

    // Getters y Setters
    public int getLlargada() { return llargada; }
    public void setLlargada(int llargada) { this.llargada = llargada; }
    public Ancoratge getAncoratge() { return ancoratge; }
    public void setAncoratge(Ancoratge ancoratge) { this.ancoratge = ancoratge; }
}
