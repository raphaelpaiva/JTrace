package org.jtrace.shader;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.ReflectanceCoefficient;

public class AmbientShader implements Shader {
	public ColorRGB shade(Light light, Hit hit, Jay jay, GeometricObject object) {
		Material material = object.getMaterial();
		ColorRGB objectColor = material.getColor();
		ReflectanceCoefficient kAmbient = material.getkAmbient();
		
		double red = light.getColor().getRed() * kAmbient.getRed() * objectColor.getRed();
		double green = light.getColor().getGreen() * kAmbient.getGreen() * objectColor.getGreen();
		double blue = light.getColor().getBlue() * kAmbient.getBlue() * objectColor.getBlue();
		
		return new ColorRGB(red, green, blue);
	}

}
