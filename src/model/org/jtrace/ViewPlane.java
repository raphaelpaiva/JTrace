package org.jtrace;

/**
 * Class representing the view plane.
 * 
 * @author raphaelpaiva
 *
 */
public class ViewPlane {
	/**
	 * The horizontal resolution.
	 */
	private int hres;
	/**
	 * The vertical resolution.
	 */
	private int vres;
	
	/**
	 * Pixel scale size.
	 */
	private double pixelSize;

	/**
	 * Creates a {@link ViewPlane}.
	 * 
	 * @param hres the horizontal resolution.
	 * @param vres the vertical resolution.
	 * @param pixelSize the scale pixel size.
	 */
	public ViewPlane(int hres, int vres, double pixelSize) {
		this.hres = hres;
		this.vres = vres;
		this.pixelSize = pixelSize;
	}
	public int getHres() {
		return hres;
	}
	public void setHres(int hres) {
		this.hres = hres;
	}
	public int getVres() {
		return vres;
	}
	public void setVres(int vres) {
		this.vres = vres;
	}
	public double getPixelSize() {
		return pixelSize;
	}
	public void setPixelSize(double pixelSize) {
		this.pixelSize = pixelSize;
	}
	
}
