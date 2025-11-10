src\ecotrack\ComparadorPrioridad.java
package ecotrack;

import java.util.Comparator;

public class ComparadorPrioridad implements Comparator<Residuo> {
    @Override
    public int compare(Residuo r1, Residuo r2) {
        // menor número = mayor prioridad
        return Integer.compare(r1.getPrioridad(), r2.getPrioridad());
    }
}

