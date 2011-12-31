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
	
    private double red, green, blue;

    /**
     * Create a color from its components values.
     * 
     * @param paramR red component
     * @param paramG green component
     * @param paramB blue component
     */
    public ColorRGB(final double paramR, final double paramG, final double paramB) {
        setRed(paramR);
        setGreen(paramG);
        setBlue(paramB);
    }
    
    public int toInt() {
    	long rgb = Math.round(red * 255);
    	rgb = (rgb << 8) + Math.round(green * 255);
    	rgb = (rgb << 8) + Math.round(blue * 255);
    	
    	return (int) rgb;
    }
    
    public ColorRGB add(ColorRGB color2) {
    	return new ColorRGB(this.getRed() + color2.getRed(), this.getGreen() + color2.getGreen(), this.getBlue() + color2.getBlue());
    }

    public ColorRGB multiply(float multiplier) {
    	return new ColorRGB(this.getRed() * multiplier, this.getGreen() * multiplier, this.getBlue() * multiplier);
    }
    
    public double getRed() {
        return red;
    }

    public void setRed(final double r) {
        this.red = Math.min(MAX_COLOR_VALUE, r);
        this.red = Math.max(MIN_COLOR_VALUE, this.red);
    }

    public double getGreen() {
        return green;
    }

    public void setGreen(final double g) {
        this.green = Math.min(MAX_COLOR_VALUE, g);
        this.green = Math.max(MIN_COLOR_VALUE, this.green);
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue(final double b) {
        this.blue = Math.min(MAX_COLOR_VALUE, b);
        this.blue = Math.max(MIN_COLOR_VALUE, this.blue);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(blue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(green);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(red);
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
		if (Double.doubleToLongBits(blue) != Double.doubleToLongBits(other.blue))
			return false;
		if (Double.doubleToLongBits(green) != Double.doubleToLongBits(other.green))
			return false;
		if (Double.doubleToLongBits(red) != Double.doubleToLongBits(other.red))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Color(" + red + ", " + green + ", " + blue + ")";
	}
    
}