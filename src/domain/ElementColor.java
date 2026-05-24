package domain;

import java.io.Serializable;

/**
 * Representación de colores pura en el Dominio usando componentes RGB.
 */
public class ElementColor implements Serializable {
    private final int r, g, b;

    public ElementColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() { return r; }
    public int getG() { return g; }
    public int getB() { return b; }
}
