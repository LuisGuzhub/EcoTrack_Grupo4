package ecotrack;

public class ColaPrioridad {

    private Nodo frente; // El elemento con mayor prioridad
    private int tamano;

    private class Nodo {
        Residuo dato;
        Nodo siguiente;

        Nodo(Residuo r) {
            this.dato = r;
        }
    }

    
    public ColaPrioridad() {
        this.frente = null;
        this.tamaño = 0;
    }

    
    public void encolar(Residuo r) {
        Nodo nuevo = new Nodo(r);


        if (frente == null || r.getPrioridad() < frente.dato.getPrioridad()) {
            nuevo.siguiente = frente;
            frente = nuevo;
        } else {
            // Caso 2: Buscar la posición correcta según la prioridad
            Nodo actual = frente;
            while (actual.siguiente != null &&
                    actual.siguiente.dato.getPrioridad() <= r.getPrioridad()) {
                actual = actual.siguiente;
            }
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamaño++;
    }

    public Residuo desencolar() {
        if (frente == null)
            return null;

        Residuo dato = frente.dato;
        frente = frente.siguiente; // El siguiente pasa a ser el frente
        tamaño--;
        return dato;
    }

    public String generarReporteCola() {
        if (frente == null)
            return "Cola vacía.\n";

        StringBuilder sb = new StringBuilder();
        Nodo temp = frente;
        while (temp != null) {
            sb.append(temp.dato).append("\n");
            temp = temp.siguiente; 
        }
        return sb.toString();
    }
}
