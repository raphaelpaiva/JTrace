package org.jtrace.lights;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;

public class PointLight extends Light {
	public PointLight(Point3D position, ColorRGB color) {
		super(position, color);
	}
	
	public PointLight(Point3D position) {
		super(position, ColorRGB.WHITE);
	}

	public PointLight(final double x, final double y, final double z) {
		super(new Point3D(x, y, z), ColorRGB.WHITE);
	}
	
	public PointLight(final double x, final double y, final double z, ColorRGB color) {
		super(new Point3D(x, y, z), color);
	}

	@Override
	public double getIntensity(double distance) {
		return 1;
	}
}
