package ecotrack;

import java.util.Comparator;
import java.util.PriorityQueue;

public class ColaPrioridad {

    private PriorityQueue<Residuo> cola;

    public ColaPrioridad() {
        cola = new PriorityQueue<>(Comparator.comparingInt(Residuo::getPrioridad));
    }

    public void encolar(Residuo r) {
        cola.add(r);
        System.out.println("Residuo agregado a la cola: " + r.getNombre() + " (Prioridad " + r.getPrioridad() + ")");
    }

    public Residuo desencolar() {
        if (cola.isEmpty()) {
            System.out.println("No hay residuos en la cola.");
            return null;
        }
        Residuo r = cola.poll();
        System.out.println("Residuo atendido: " + r.getNombre());
        return r;
    }

    public void mostrarCola() {
        System.out.print(generarReporteCola());
    }

    // Para la GUI
    public String generarReporteCola() {
        if (cola.isEmpty()) {
            return "No hay residuos en la cola de prioridad.\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Residuos pendientes por prioridad:\n");
        for (Residuo r : cola) {
            sb.append(r).append("\n");
        }
        return sb.toString();
    }
}

