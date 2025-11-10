package ecotrack;

import java.io.Serializable;

public class Residuo implements Serializable {

    private int id;
    private String nombre;
    private String tipo;
    private double peso;
    private String fechaRecoleccion;
    private String zona;
    private int prioridad; // 1 = alta, 2 = media, 3 = baja

    public Residuo(int id, String nombre, String tipo, double peso, String fechaRecoleccion, String zona,
            int prioridad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.peso = peso;
        this.fechaRecoleccion = fechaRecoleccion;
        this.zona = zona;
        this.prioridad = prioridad;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPeso() {
        return peso;
    }

    public String getFechaRecoleccion() {
        return fechaRecoleccion;
    }

    public String getZona() {
        return zona;
    }

    public int getPrioridad() {
        return prioridad;
    }

    // Setters
    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nombre + " (" + tipo + "), " + peso + "kg, Zona: " + zona + ", Prioridad: "
                + prioridad;
    }
}
