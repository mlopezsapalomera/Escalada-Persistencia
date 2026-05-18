package model.entidades;

public class Llarg {
    private int numeroLlarg;
    private int llargada;
    private String dificultat;
    private Via.Orientacio orientacio;

    public Llarg() {
    }

    public Llarg(int llargada, String dificultat, Via.Orientacio orientacio) {
        this.llargada = llargada;
        this.dificultat = dificultat;
        this.orientacio = orientacio;
    }

    // Getters
    public int getNumeroLlarg() { return numeroLlarg; }
    public int getLlargada() { return llargada; }
    public String getDificultat() { return dificultat; }
    public Via.Orientacio getOrientacio() { return orientacio; }

    // Setters
    public void setNumeroLlarg(int numeroLlarg) { this.numeroLlarg = numeroLlarg; }
    public void setMetres(int metres) { this.llargada = metres; }
    public int getMetres() { return llargada; }
    public void setGrau(String grau) { this.dificultat = grau; }
    public String getGrau() { return dificultat; }
    public void setOrientacio(Via.Orientacio orientacio) { this.orientacio = orientacio; }
}