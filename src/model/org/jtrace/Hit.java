package org.jtrace;

public class Hit {
	private double t;
	
	private boolean hit;

	public Hit(double t) {
		this.t = t;
		this.hit = true;
	}
	
	protected Hit() {
		this.t = 0;
		this.hit = false;
	}

	public double getT() {
		return t;
	}

	public boolean isHit() {
		return hit;
	}
	
}	
	

