package model.entidades;

/**
 * Especialización de {@link Via} para vías deportivas.
 */
public class ViaEsportiva extends Via {

    /**
     * Tipos de anclaje habituales en vías deportivas.
     */
    public enum Ancoratge {
        SPITS, PARABOLTS, QUIMICS
    }

    /**
     * Constructor vacío que fija automáticamente el estilo deportiva.
     */
    public ViaEsportiva() {
        super();
        this.setEstil(Via.Estil.ESPORTIVA);
    }

    /**
     * Constructor completo de vía deportiva.
     * @param sector sector de la vía.
     * @param creador escalador creador.
     * @param nom nombre de la vía.
     * @param orientacio orientación principal.
     * @param estat estado de la vía.
     * @param tipusRoca tipo de roca.
     * @param llargada longitud total.
     * @param ancoratge tipo de anclaje.
     */
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