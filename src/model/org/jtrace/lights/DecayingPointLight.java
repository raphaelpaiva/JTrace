package org.jtrace.lights;

import static org.jtrace.primitives.ColorRGB.WHITE;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;

public class DecayingPointLight extends Light {

	final double initialIntensity;
	
	public DecayingPointLight(Point3D position, ColorRGB color, double initialIntensity) {
		super(position, color);
		
		this.initialIntensity = initialIntensity;
	}

	public DecayingPointLight(double x, double y, double z, double initialIntensity) {
		super(new Point3D(x, y, z));
		
		this.initialIntensity = initialIntensity;
	}
	
	public DecayingPointLight(Point3D position, double initialIntensity) {
		super(position, WHITE);
		
		this.initialIntensity = initialIntensity;
	}
	
	@Override
	public double getIntensity(double distance) {
		
		return initialIntensity/(distance*distance);
	}
	
}
