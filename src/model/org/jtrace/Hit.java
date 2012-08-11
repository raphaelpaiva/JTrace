package org.jtrace;

import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class Hit {
	
	private double t;
	
	private boolean hit;
	
	private Vector3D normal;
	
	public Hit(double t, Vector3D normal) {
		this.t = t;
		this.normal = normal;
		this.hit = true;
	}
	
	protected Hit() {
		this.t = 0;
		this.normal = null;
		this.hit = false;
	}

	public Point3D getPoint(Jay jay) {
		Point3D point = jay.getOrigin();
		
		Vector3D direction = jay.getDirection().multiply(getT());
		
		return point.add(direction);
	}
	
	public double getT() {
		return t;
	}
	
	public Vector3D getNormal() {
		return normal;
	}

	public boolean isHit() {
		return hit;
	}
	
}	
	

