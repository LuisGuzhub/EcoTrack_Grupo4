package ecotrack;

import java.util.Comparator;
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

    // Iterador Hacia atrás
public Iterator<Residuo> iteradorAtras() {
    return new Iterator<Residuo>() {
        private Nodo actual = (inicio != null) ? inicio.anterior : null;
        private boolean primeraVez = true;

        @Override
        public boolean hasNext() {
            if (inicio == null) return false;
            return primeraVez || actual != inicio.anterior;
        }

        @Override
        public Residuo next() {
            Residuo dato = actual.dato;
            actual = actual.anterior; // Movimiento hacia atrás
            primeraVez = false;
            return dato;
        }
    };
}
// MÉTODO DE ORDENAMIENTO PROPIO 
    // Permite cambiar el criterio de ordenamiento sin usar ArrayLis
    public void ordenar(Comparator<Residuo> comp) {
        if (inicio == null || inicio.siguiente == inicio) return;

        boolean intercambiado;
        do {
            intercambiado = false;
            Nodo actual = inicio;
            do {
                Nodo siguiente = actual.siguiente;
                // Comparamos usando el comparador pasado por parámetro (Peso, Tipo o Prioridad) 
                if (comp.compare(actual.dato, siguiente.dato) > 0) {
                    // Intercambio de datos del Residuo entre nodos
                    Residuo temp = actual.dato;
                    actual.dato = siguiente.dato;
                    siguiente.dato = temp;
                    intercambiado = true;
                }
                actual = siguiente;
            } while (actual.siguiente != inicio); // Recorrido completo de la circular
        } while (intercambiado);
    }

public boolean eliminar(int id) {
    if (inicio == null) return false;

    Nodo actual = inicio;
    do {
        if (actual.dato.getId() == id) {
            if (actual == inicio && actual.siguiente == inicio) {
                inicio = null; // Era el único elemento
            } else {
                actual.anterior.siguiente = actual.siguiente;
                actual.siguiente.anterior = actual.anterior;
                if (actual == inicio) inicio = actual.siguiente;
            }
            return true;
        }
        actual = actual.siguiente;
    } while (actual != inicio);
    return false;
}

}
