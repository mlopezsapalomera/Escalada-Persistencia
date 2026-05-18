package model.entidades;
import java.util.List;

public class ViaGel extends Via {

    public ViaGel() {
        super();
        this.setEstil(Via.Estil.GEL);
    }

    public ViaGel(Sector sector, Escalador creador, String nom, String grauGlobal, Orientacio orientacio, Estat estat, String tipusRoca, List<Llarg> llargs) {
        super();
        this.setSector(sector);
        this.setCreadaPer(creador);
        this.setNom(nom);
        this.setGrauGlobal(grauGlobal);
        this.setOrientacio(orientacio);
        this.setEstat(estat);
        this.setTipusRoca(tipusRoca);
        this.setLlistaLlargs(llargs);
        this.setEstil(Via.Estil.GEL);
    }
}