package ecotrack;

import java.util.Iterator;

public class ListaCircular {

    private Nodo inicio;

    private class Nodo {
        Residuo dato;
        Nodo siguiente;
        Nodo anterior;

        Nodo(Residuo r) {
            this.dato = r;
        }
    }

    public void insertar(Residuo r) {
        Nodo nuevo = new Nodo(r);
        if (inicio == null) {
            inicio = nuevo;
            inicio.siguiente = inicio;
            inicio.anterior = inicio;
        } else {
            Nodo ultimo = inicio.anterior;
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            nuevo.siguiente = inicio;
            inicio.anterior = nuevo;
        }
    }

    public void mostrarAdelante() {
        if (inicio == null) {
            System.out.println("Lista vacía.");
            return;
        }
        Nodo actual = inicio;
        do {
            System.out.println(actual.dato);
            actual = actual.siguiente;
        } while (actual != inicio);
    }

    // Iterador personalizado hacia adelante
    public Iterator<Residuo> iteradorAdelante() {
        return new Iterator<Residuo>() {

            private Nodo actual = inicio;
            private boolean primeraVez = true;

            @Override
            public boolean hasNext() {
                if (inicio == null) {
                    return false;
                }
                return primeraVez || actual != inicio;
            }

            @Override
            public Residuo next() {
                Residuo dato = actual.dato;
                actual = actual.siguiente;
                primeraVez = false;
                return dato;
            }
        };
    }

}
