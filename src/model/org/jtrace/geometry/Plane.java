package org.jtrace.geometry;

import org.jtrace.Constants;
import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;
import org.jtrace.NotHit;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class Plane extends GeometricObject {
	private Point3D point;
	private Vector3D normal;
	
	public Plane(Point3D point, Vector3D normal, Material material) {
		super(material);
		this.point = point;
		this.normal = normal;
	}

	@Override
	public Hit hit(Jay jay) {
		double b = new Vector3D(jay.getOrigin(), point).dot(normal);
		double a = jay.getDirection().dot(normal);
		
		double t = b / a;
		
		if (a != 0 && t > Constants.epsilon) {
			return new Hit(t, this.getNormal().normal());
		}
		
		return new NotHit();
	}

	public Point3D getPoint() {
		return point;
	}

	public void setPoint(Point3D point) {
		this.point = point;
	}

	public Vector3D getNormal() {
		return normal;
	}

	public void setNormal(Vector3D normal) {
		this.normal = normal;
	}
	
	@Override
	public String toString() {
		return "p" + point.toString() + ", n" + normal.toString();
	}
	
}
