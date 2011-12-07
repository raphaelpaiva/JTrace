package org.jtrace.geometry;

import org.jtrace.Jay;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class Sphere {
	private Point3D center;
	private float radius;

	public Sphere(final Point3D center, final float radius) {
		this.center = center;
		this.radius = radius;
	}

	public boolean hit(final Jay jay) {
		Vector3D temp = new Vector3D(jay.getPoint().subtract(center));
		
		double a = jay.getDirection().dot();
		double b = temp.multiply(2).dot(jay.getDirection());
		double c = temp.dot() - radius * radius;

		double delta = b * b - 4 * a * c;

		if (delta < 0.0) {
			return false;
		} else {
			return true;
		}
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
