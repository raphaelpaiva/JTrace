package org.jtrace;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.ReflectanceCoefficient;

public class Materials {

	public static Material matte(final ColorRGB color) {
		final ReflectanceCoefficient kAmbient = new ReflectanceCoefficient(0.07, 0.07, 0.07);
		final ReflectanceCoefficient kDiffuse = new ReflectanceCoefficient(0.3, 0.3, 0.3);

		return new Material(color, kAmbient, kDiffuse);
	}
	
	public static Material metallic(final ColorRGB color) {
		final ReflectanceCoefficient kAmbient = new ReflectanceCoefficient(0.07, 0.07, 0.07);
		final ReflectanceCoefficient kDiffuse = new ReflectanceCoefficient(0.3, 0.3, 0.3);
		final ReflectanceCoefficient kSpecular = new ReflectanceCoefficient(0.2, 0.2, 0.2);

		return new Material(color, kAmbient, kDiffuse, kSpecular);
	}

}
