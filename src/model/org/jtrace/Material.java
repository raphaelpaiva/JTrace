package org.jtrace;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.ReflectanceCoefficient;

public class Material {

	private ColorRGB color;
	
	private ReflectanceCoefficient kAmbient;

	public Material(ColorRGB color, ReflectanceCoefficient kAmbient) {
		this.color = color;
		this.kAmbient = kAmbient;
	}

	public ColorRGB getColor() {
		return color;
	}

	public ReflectanceCoefficient getkAmbient() {
		return kAmbient;
	}

}
