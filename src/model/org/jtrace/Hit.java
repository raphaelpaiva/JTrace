package org.jtrace;

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
	

