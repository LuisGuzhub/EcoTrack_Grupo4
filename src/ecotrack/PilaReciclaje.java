package ecotrack;

public class PilaReciclaje {

    private Nodo tope;

    private static class Nodo {
        Residuo dato;
        Nodo siguiente;

        Nodo(Residuo dato) {
            this.dato = dato;
        }
    }

    // Apilar un residuo (LIFO)
    public void apilar(Residuo r) {
        Nodo nuevo = new Nodo(r);
        nuevo.siguiente = tope;
        tope = nuevo;
        System.out.println("Residuo enviado a la pila de reciclaje: " + r.getNombre());
    }

    // Desapilar (retirar el último agregado)
    public Residuo desapilar() {
        if (tope == null) {
            System.out.println("La pila de reciclaje está vacía.");
            return null;
        }
        Residuo r = tope.dato;
        tope = tope.siguiente;
        System.out.println("Residuo procesado desde la pila: " + r.getNombre());
        return r;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    // Versión consola clásica
    public void mostrarPila() {
        System.out.print(generarReportePila());
    }

    // ✅ Versión para GUI: devuelve un texto bonito del contenido de la pila
    public String generarReportePila() {
        StringBuilder sb = new StringBuilder();
        if (tope == null) {
            sb.append("No hay residuos en la pila.\n");
        } else {
            sb.append("Residuos en pila (tope primero):\n");
            Nodo actual = tope;
            while (actual != null) {
                sb.append(actual.dato).append("\n");
                actual = actual.siguiente;
            }
        }
        return sb.toString();
    }
}
