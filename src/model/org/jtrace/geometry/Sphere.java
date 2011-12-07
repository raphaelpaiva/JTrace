package org.jtrace.geometry;

import org.jtrace.Jay;
import org.jtrace.primitives.Point3D;

public class Sphere {
	private Point3D center;
	private float radius;

	public Sphere(final Point3D center, final float radius) {
		super();
		this.center = center;
		this.radius = radius;
	}

	public boolean hit(final Jay jay)
	{

		return false;
	}

	public Point3D getCenter() {
		return center;
	}

	public void setCenter(final Point3D center) {
		this.center = center;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(final float radius) {
		this.radius = radius;
	}

}
