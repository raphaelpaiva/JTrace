package org.jtrace.lights;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;

/**
 * Basic class representing a point light in three-dimensional space.
 * 
 * @author raphaelpaiva
 * 
 */
public abstract class Light {

	private Point3D position;

	private ColorRGB color;

	/**
	 * Calculates the intensity of the light based on the distance to the light.
	 * 
	 * @param distance the distance from the point being illuminated to the light source.
	 * @return the intensity of light, a double value between 0 and 1.
	 */
	public abstract double getIntensity(double distance);

	public Light(Point3D position) {
		this.position = position;
		this.color = ColorRGB.WHITE;
	}

	public Light(Point3D position, ColorRGB color) {
		this.position = position;
		this.color = color;
	}

	public Point3D getPosition() {
		return position;
	}

	public ColorRGB getColor() {
		return color;
	}

	public void setColor(ColorRGB color) {
		this.color = color;
	}

}
