package org.jtrace.shader;

import org.jtrace.tracer.Hit;
import org.jtrace.material.Material;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.ReflectanceCoefficient;

public class AmbientShader implements Shader {

	public AmbientShader() {
	}

	@Override
    public ColorRGB shade(Light light, Hit hit, GeometricObject object) {
        Material material = object.getMaterial();
        ColorRGB objectColor = object.getColor(hit);
        ReflectanceCoefficient kAmbient = material.getkAmbient();

        double red = light.getColor().getRed() * kAmbient.getRed() * objectColor.getRed();
        double green = light.getColor().getGreen() * kAmbient.getGreen() * objectColor.getGreen();
        double blue = light.getColor().getBlue() * kAmbient.getBlue() * objectColor.getBlue();

        return new ColorRGB(red, green, blue);
    }

}
