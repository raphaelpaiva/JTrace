package org.jtrace.geometry;

import java.util.List;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;
import org.jtrace.NotHit;
import org.jtrace.Section;
import org.jtrace.primitives.Point3D;

/**
 * Represents a quadrilateral in three-dimensional space.
 * 
 * A quadrilateral is not necessarily planar and is formed by two
 * {@link Triangle} instances.
 * 
 * @author raphaelpaiva
 * @author ethiago
 */
public class Quadrilateral extends GeometricObject {

	private Triangle t1;
	private Triangle t2;
	
	/**
	 * Creates a {@link Quadrilateral} given it's four edges in
	 * counter-clockwise order.
	 * 
	 * @param p0 the first edge in counter-clockwise order
	 * @param p1 the second edge in counter-clockwise order
	 * @param p2 the third edge in counter-clockwise order
	 * @param p3 the fourth edge in counter-clockwise order
	 * @param material the {@link Quadrilateral}'s {@link Material}.
	 */
	public Quadrilateral(Point3D p0, Point3D p1, Point3D p2, Point3D p3, Material material) {
		super(material);
		
		t1 = new Triangle(p0, p1, p2, material);
		t2 = new Triangle(p2, p3, p0, material);
	}

	@Override
	public Hit hit(Jay jay) {
		Hit hitT1 = t1.hit(jay);
		Hit hitT2 = t2.hit(jay);
		
		if (hitT1.isHit()) {
			if (hitT2.isHit()) {
				if (hitT1.getT() < hitT2.getT()) {
					return hitT1;
				} else {
					return hitT2;
				}
			}
			
			return hitT1;
		} else {
			if (hitT2.isHit()) {
				return hitT2;
			}
		}
		
		return new NotHit();
	}

	@Override
	public List<Section> sections(Jay jay) {
		// TODO Auto-generated method stub
		return null;
	}

}
