package model.entidades;

import java.util.Date;
import java.util.List;

public class ViaGel extends Via {
    private List<Llarg> llargs;

    public ViaGel() {
        super();
        setTipusVia(TipusVia.GEL);
    }

    public ViaGel(Sector sector, Escola escola, Escalador creador, String nom, String grauGlobal, Orientacio orientacio, Estat estat, Date dataFinalitzacioEstat, TipusRoca tipusRoca, String restriccions, List<Llarg> llargs) {
        super(sector, escola, creador, nom, grauGlobal, orientacio, estat, dataFinalitzacioEstat, tipusRoca, TipusVia.GEL, restriccions);
        this.llargs = llargs;
    }

    // Getters y Setters
    public List<Llarg> getLlargs() { return llargs; }
    public void setLlargs(List<Llarg> llargs) { this.llargs = llargs; }
}
