package model.entidades;

public class Llarg {
    private int id;
    private Via via;
    private int ordreLlarg;
    private int llargada;
    private String grau;
    private String ancoratge;

    public Llarg() {}

    public Llarg(Via via, int ordreLlarg, int llargada, String grau, String ancoratge) {
        this.via = via;
        this.ordreLlarg = ordreLlarg;
        this.llargada = llargada;
        this.grau = grau;
        this.ancoratge = ancoratge;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Via getVia() { return via; }
    public void setVia(Via via) { this.via = via; }
    public int getOrdreLlarg() { return ordreLlarg; }
    public void setOrdreLlarg(int ordreLlarg) { this.ordreLlarg = ordreLlarg; }
    public int getLlargada() { return llargada; }
    public void setLlargada(int llargada) { this.llargada = llargada; }
    public String getGrau() { return grau; }
    public void setGrau(String grau) { this.grau = grau; }
    public String getAncoratge() { return ancoratge; }
    public void setAncoratge(String ancoratge) { this.ancoratge = ancoratge; }
}
