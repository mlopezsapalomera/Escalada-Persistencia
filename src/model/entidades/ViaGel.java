package model.entidades;
import java.util.List;

public class ViaGel extends Via {
    public ViaGel() {
        super();
        setEstil(Via.Estil.GEL);
    }

    public ViaGel(Sector sector, Escalador creadaPer, String nom,
                  Via.Orientacio orientacio, Via.Estat estat,
                  Via.TipusRoca tipusRoca, String restriccions,
                  List<Llarg> llargs) {
        super(sector, creadaPer, nom, "N/A", orientacio, estat,
              tipusRoca, Via.Estil.GEL, restriccions);
        setLlistaLlargs(llargs);
    }
}