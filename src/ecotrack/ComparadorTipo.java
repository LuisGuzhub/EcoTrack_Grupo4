src\ecotrack\ComparadorTipo.java
package ecotrack;

import java.util.Comparator;

public class ComparadorTipo implements Comparator<Residuo> {
    @Override
    public int compare(Residuo r1, Residuo r2) {
        return r1.getTipo().compareToIgnoreCase(r2.getTipo());
    }
}
