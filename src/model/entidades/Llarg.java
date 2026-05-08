package model.entidades;

public class Llarg {
    private int llargada;
    private String dificultat;
    private Via.Orientacio orientacio;

    public Llarg(int llargada, String dificultat, Via.Orientacio orientacio) {
        this.llargada = llargada;
        this.dificultat = dificultat;
        this.orientacio = orientacio;
    }

    // Getters
    public int getLlargada() { return llargada; }
    public String getDificultat() { return dificultat; }
    public Via.Orientacio getOrientacio() { return orientacio; }
}