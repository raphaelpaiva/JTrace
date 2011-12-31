package org.jtrace.lights;

import org.jtrace.primitives.Point3D;

public class Light {

	private Point3D position;

	public Light(Point3D position) {
		this.position = position;
	}
	
	public Light(final double x, final double y, final double z) {
		this.position = new Point3D(x, y, z);
	}

	public Point3D getPosition() {
		return position;
	}

}
