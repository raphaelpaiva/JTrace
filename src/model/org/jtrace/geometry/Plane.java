package org.jtrace.geometry;

import java.util.ArrayList;
import java.util.List;

import org.jtrace.Constants;
import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;
import org.jtrace.NotHit;
import org.jtrace.Section;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

/**
 * Basic class representing a infinite plane in three-dimensional space.
 * 
 * @author raphaelpaiva
 *
 */
public class Plane extends GeometricObject {
	private Point3D point;
	private Vector3D normal;
	
	/**
	 * Creates a {@link Plane} from a {@link Point3D} contained on it and its normal {@link Vector3D}.
	 * 
	 * @param point a {@link Point3D} that belongs to the {@link Plane}.
	 * @param normal the {@link Plane}'s normal {@link Vector3D}.
	 * @param material the {@link Plane}'s {@link Material}.
	 */
	public Plane(Point3D point, Vector3D normal, Material material) {
		super(material);
		this.point = point;
		this.normal = normal;
	}

	@Override
	public Hit hit(Jay jay) {
		double b = new Vector3D(jay.getOrigin(), point).dot(normal);
		double a = jay.getDirection().dot(normal);
		
		if (a != 0) {
			double t = b / a;

			if (t > Constants.epsilon) {
				return new Hit(t, this.getNormal().normal());
			}
		}
		
		return new NotHit();
	}
	
	@Override
	public List<Section> sections(Jay jay) {
		ArrayList<Section> sections = new ArrayList<Section>();
		
		double b = new Vector3D(jay.getOrigin(), point).dot(normal);
		double a = jay.getDirection().dot(normal);
		
		
		if (a != 0) {
			double t = b / a;
			Hit hit = new Hit(t, this.getNormal().normal());
			
			sections.add(new Section(hit, hit));
		}
		
		return sections;
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
