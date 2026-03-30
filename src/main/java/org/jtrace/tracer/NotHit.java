package org.jtrace.tracer;

public class NotHit extends Hit {

  public static final NotHit INSTANCE = new NotHit();

	private NotHit() {
		super();
	}
	
	@Override
	public double getT() {
		throw new IllegalStateException("Didn't hit anything!");
	}
	
}
