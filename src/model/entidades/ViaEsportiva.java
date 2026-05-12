package model.entidades;

public class ViaEsportiva extends Via {

    public enum Ancoratge {
        SPITS, PARABOLTS, QUIMICS;

        // Converteix a la forma exacta que espera la BD
        public String toDb() {
            return this == QUIMICS ? "químics" : this.name().toLowerCase();
        }
    }

    private Ancoratge ancoratge;

    public ViaEsportiva() {
        super();
        setEstil(Via.Estil.ESPORTIVA);
    }

    public ViaEsportiva(Sector sector, Escalador creadaPer, String nom,
                        String grau, Via.Orientacio orientacio, Via.Estat estat,
                        Via.TipusRoca tipusRoca, String restriccions,
                        int llargada, Ancoratge ancoratge) {
        super(sector, creadaPer, nom, grau, orientacio, estat,
              tipusRoca, Via.Estil.ESPORTIVA, restriccions);
        setLlargadaTotal(llargada);
        this.ancoratge = ancoratge;
    }

    public Ancoratge getAncoratge()             { return ancoratge; }
    public void setAncoratge(Ancoratge a)       { this.ancoratge = a; }
}