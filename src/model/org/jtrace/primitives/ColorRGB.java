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
	public static ColorRGB BLACK   = new ColorRGB(0, 0, 0);
	public static ColorRGB RED     = new ColorRGB(255, 0, 0);
	public static ColorRGB GREEN   = new ColorRGB(0, 255, 0);
	public static ColorRGB BLUE    = new ColorRGB(0, 0, 255);
	public static ColorRGB YELLOW  = new ColorRGB(255, 255, 0);
	public static ColorRGB PURPLE   = new ColorRGB(128, 0, 128);
	
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
    
    public int toInt() {
    	int rgb = r;
    	rgb = (rgb << 8) + g;
    	rgb = (rgb << 8) + b;
    	
    	return rgb;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColorRGB other = (ColorRGB) obj;
		if (b != other.b)
			return false;
		if (g != other.g)
			return false;
		if (r != other.r)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Color(" + r + ", " + g + ", " + b + ")";
	}
    
}