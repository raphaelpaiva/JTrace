package org.jtrace;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.ReflectanceCoefficient;

public class Material {

	private ColorRGB color;
	
	private ReflectanceCoefficient kAmbient;
	
	private ReflectanceCoefficient kDiffuse;
	
	private ReflectanceCoefficient kSpecular;

	public Material(ColorRGB color, ReflectanceCoefficient kAmbient, ReflectanceCoefficient kDiffuse) {
		this.color = color;
		this.kAmbient = kAmbient;
		this.kDiffuse = kDiffuse;
	}
	
	public Material(ColorRGB color, ReflectanceCoefficient kAmbient, ReflectanceCoefficient kDiffuse, ReflectanceCoefficient kSpecular) {
		this.color = color;
		this.kAmbient = kAmbient;
		this.kDiffuse = kDiffuse;
		this.kSpecular = kSpecular;
	}

	public ColorRGB getColor() {
		return color;
	}

	public ReflectanceCoefficient getkAmbient() {
		return kAmbient;
	}

	public ReflectanceCoefficient getkDiffuse() {
		return kDiffuse;
	}

	public ReflectanceCoefficient getkSpecular() {
		return kSpecular;
	}

	public void setkSpecular(ReflectanceCoefficient kSpecular) {
		this.kSpecular = kSpecular;
	}
	
}
