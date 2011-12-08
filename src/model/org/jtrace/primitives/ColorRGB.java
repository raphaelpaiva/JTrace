package org.jtrace.primitives;
/**
 * Basic class to represent a color in RGB format.
 * 
 * @author raphaelpaiva
 * @author brunocosta
 * @author flaviocdc
 *
 */
public class ColorRGB {
    private int r, g, b;

    /**
     * Create a color from its components values.
     * 
     * @param r red component
     * @param g green component
     * @param b blue component
     */
    public ColorRGB(final int r, final int g, final int b) {
        super();
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public void setR(final int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(final int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(final int b) {
        this.b = b;
    }
}