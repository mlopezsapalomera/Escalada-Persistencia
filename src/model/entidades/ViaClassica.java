package model.entidades;

import java.util.Date;
import java.util.List;

public class ViaClassica extends Via {
    private List<Llarg> llargs;
    // Note: The 'ancoratge' in 'llargs' table seems to be per-pitch, not for the whole route.
    // If there's a general anchor type for the route, it should be added here.

    public ViaClassica() {
        super();
        setTipusVia(TipusVia.CLASSICA);
    }

    public ViaClassica(Sector sector, Escola escola, Escalador creador, String nom, String grauGlobal, Orientacio orientacio, Estat estat, Date dataFinalitzacioEstat, TipusRoca tipusRoca, String restriccions, List<Llarg> llargs) {
        super(sector, escola, creador, nom, grauGlobal, orientacio, estat, dataFinalitzacioEstat, tipusRoca, TipusVia.CLASSICA, restriccions);
        this.llargs = llargs;
    }

    // Getters y Setters
    public List<Llarg> getLlargs() { return llargs; }
    public void setLlargs(List<Llarg> llargs) { this.llargs = llargs; }
}
