package org.jtrace.geometry;

import org.jtrace.Jay;
import org.jtrace.primitives.ColorRGB;

public abstract class GeometricObject {
	protected ColorRGB color;
	
	public GeometricObject(ColorRGB color) {
		this.color = color;
	}

	public abstract boolean hit(Jay jay);
	
	public ColorRGB getColor() {
		return color;
	}

}
