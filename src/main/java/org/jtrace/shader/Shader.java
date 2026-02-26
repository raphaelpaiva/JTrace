package org.jtrace.shader;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;

public interface Shader {
	public ColorRGB shade(Light light, Hit hit, Jay jay, GeometricObject object);
}
