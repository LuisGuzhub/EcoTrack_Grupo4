src\ecotrack\ComparadorPeso.java
package ecotrack;

import java.util.Comparator;

public class ComparadorPeso implements Comparator<Residuo> {
    @Override
    public int compare(Residuo r1, Residuo r2) {
        return Double.compare(r1.getPeso(), r2.getPeso());
    }
}

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
