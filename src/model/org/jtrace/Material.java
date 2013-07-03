package org.jtrace;

import java.awt.image.BufferedImage;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.ReflectanceCoefficient;

public class Material {

	private ColorRGB color;
	
	private ReflectanceCoefficient kAmbient;
	
	private ReflectanceCoefficient kDiffuse;
	
	private ReflectanceCoefficient kSpecular;

	private BufferedImage texture;

	public Material(ColorRGB color, ReflectanceCoefficient kAmbient, ReflectanceCoefficient kDiffuse) {
		this.color = color;
		this.kAmbient = kAmbient;
		this.kDiffuse = kDiffuse;
	}
	
	public Material(ColorRGB color, ReflectanceCoefficient kAmbient, ReflectanceCoefficient kDiffuse, BufferedImage texture) {
        this.color = color;
        this.kAmbient = kAmbient;
        this.kDiffuse = kDiffuse;
        this.texture = texture;
    }
	
	public Material(ColorRGB color, ReflectanceCoefficient kAmbient, ReflectanceCoefficient kDiffuse, ReflectanceCoefficient kSpecular) {
		this.color = color;
		this.kAmbient = kAmbient;
		this.kDiffuse = kDiffuse;
		this.kSpecular = kSpecular;
	}
	
	public Material(ColorRGB color, ReflectanceCoefficient kAmbient, ReflectanceCoefficient kDiffuse, ReflectanceCoefficient kSpecular, BufferedImage texture) {
        this.color = color;
        this.kAmbient = kAmbient;
        this.kDiffuse = kDiffuse;
        this.kSpecular = kSpecular;
        this.texture = texture;
    }

	public ColorRGB getColor(double u, double v) {
        if (texture == null) {
            return getColor();
        }
 
        int x = (int) Math.round((texture.getWidth() - 1) * u);
        int y = (int) Math.round((texture.getHeight() - 1) * v);
 
        int intColor = texture.getRGB(x, y);
 
        ColorRGB color = new ColorRGB(intColor);
 
        return color;
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
