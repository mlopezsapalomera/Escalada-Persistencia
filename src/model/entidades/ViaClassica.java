package model.entidades;
import java.util.List;

public class ViaClassica extends Via {
    public ViaClassica() {
        super();
        setEstil(Via.Estil.CLASSICA);
    }

    public ViaClassica(Sector sector, Escalador creadaPer, String nom,
                       Via.Orientacio orientacio, Via.Estat estat,
                       Via.TipusRoca tipusRoca, String restriccions,
                       List<Llarg> llargs) {
        super(sector, creadaPer, nom, "N/A", orientacio, estat,
              tipusRoca, Via.Estil.CLASSICA, restriccions);
        setLlistaLlargs(llargs);
    }
}