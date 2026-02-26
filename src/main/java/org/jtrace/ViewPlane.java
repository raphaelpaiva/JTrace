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
	 * Creates a {@link ViewPlane}.
	 * 
	 * @param hres the horizontal resolution.
	 * @param vres the vertical resolution.
	 * @param pixelSize the scale pixel size.
	 */
	public ViewPlane(int hres, int vres) {
		this.hres = hres;
		this.vres = vres;
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
	
}
