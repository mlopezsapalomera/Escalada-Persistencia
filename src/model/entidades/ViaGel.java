package model.entidades;
import java.util.List;

/**
 * Especialización de {@link Via} para vías de hielo.
 */
public class ViaGel extends Via {

    /**
     * Constructor vacío que fija automáticamente el estilo hielo.
     */
    public ViaGel() {
        super();
        this.setEstil(Via.Estil.GEL);
    }

    /**
     * Constructor completo de vía de hielo.
     * @param sector sector de la vía.
     * @param creador escalador creador.
     * @param nom nombre de la vía.
     * @param grauGlobal grado global.
     * @param orientacio orientación principal.
     * @param estat estado de la vía.
     * @param tipusRoca tipo de roca.
     * @param llargs lista de largos.
     */
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