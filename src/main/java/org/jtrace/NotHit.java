package org.jtrace;

public class NotHit extends Hit {

	public NotHit() {
		super();
	}
	
	@Override
	public double getT() {
		throw new IllegalStateException("Didn't hit anything!");
	}
	
}
