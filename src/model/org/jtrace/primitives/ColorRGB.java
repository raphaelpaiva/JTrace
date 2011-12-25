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
	private static double MAX_COLOR_VALUE = 1;
	private static double MIN_COLOR_VALUE = 0;
	
	public static ColorRGB BLACK   = new ColorRGB(0.0, 0.0, 0.0);
	public static ColorRGB RED     = new ColorRGB(1.0, 0.0, 0.0);
	public static ColorRGB GREEN   = new ColorRGB(0.0, 1.0, 0.0);
	public static ColorRGB BLUE    = new ColorRGB(0.0, 0.0, 1.0);
	public static ColorRGB YELLOW  = new ColorRGB(1.0, 1.0, 0.0);
	public static ColorRGB PURPLE  = new ColorRGB(0.5, 0.0, 0.5);
	
    private double r, g, b;

    /**
     * Create a color from its components values.
     * 
     * @param paramR red component
     * @param paramG green component
     * @param paramB blue component
     */
    public ColorRGB(final double paramR, final double paramG, final double paramB) {
        setR(paramR);
        setG(paramG);
        setB(paramB);
    }
    
    public int toInt() {
    	long rgb = Math.round(r * 255);
    	rgb = (rgb << 8) + Math.round(g * 255);
    	rgb = (rgb << 8) + Math.round(b * 255);
    	
    	return (int) rgb;
    }
    
    public ColorRGB add(ColorRGB color2) {
    	return new ColorRGB(this.getR() + color2.getR(), this.getG() + color2.getG(), this.getB() + color2.getB());
    }

    public ColorRGB multiply(float multiplier) {
    	return new ColorRGB(this.getR() * multiplier, this.getG() * multiplier, this.getB() * multiplier);
    }
    
    public double getR() {
        return r;
    }

    public void setR(final double r) {
        this.r = Math.min(MAX_COLOR_VALUE, r);
        this.r = Math.max(MIN_COLOR_VALUE, this.r);
    }

    public double getG() {
        return g;
    }

    public void setG(final double g) {
        this.g = Math.min(MAX_COLOR_VALUE, g);
        this.g = Math.max(MIN_COLOR_VALUE, this.g);
    }

    public double getB() {
        return b;
    }

    public void setB(final double b) {
        this.b = Math.min(MAX_COLOR_VALUE, b);
        this.b = Math.max(MIN_COLOR_VALUE, this.b);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(b);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(g);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(r);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (Double.doubleToLongBits(b) != Double.doubleToLongBits(other.b))
			return false;
		if (Double.doubleToLongBits(g) != Double.doubleToLongBits(other.g))
			return false;
		if (Double.doubleToLongBits(r) != Double.doubleToLongBits(other.r))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Color(" + r + ", " + g + ", " + b + ")";
	}
    
}