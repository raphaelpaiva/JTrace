package org.jtrace.lights;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;

public class Light {

	private Point3D position;
	
	private ColorRGB color;

	public Light(Point3D position) {
		this.position = position;
		this.color = ColorRGB.WHITE;
	}
	
	public Light(Point3D position, ColorRGB color) {
		this.position = position;
		this.color = color;
	}
	
	public Light(final double x, final double y, final double z) {
		this.position = new Point3D(x, y, z);
		this.color = ColorRGB.WHITE;
	}
	
	public Light(final double x, final double y, final double z, ColorRGB color) {
		this.position = new Point3D(x, y, z);
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
