package org.jtrace;

import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.shader.Shader;

public interface TracerInterceptor {
	public void init(Tracer tracer, Scene scene);
	
	public void beforeShade(Light light, ColorRGB color);
	public boolean shouldShade(Shader shader, Light light, Hit hit, Jay jay, GeometricObject object);
	public void afterShade(Light light, ColorRGB color);
	
}
