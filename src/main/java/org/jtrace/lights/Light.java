package org.jtrace.lights;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Basic class representing a point light in three-dimensional space.
 * 
 * @author raphaelpaiva
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PointLight.class, name = "PointLight"),
    @JsonSubTypes.Type(value = DecayingPointLight.class, name = "DecayingPointLight")
})
public abstract class Light {

	private Point3D position;

	private ColorRGB color;
	
	public Light() {
		this.position = new Point3D(0, 0, 0);
		this.color = ColorRGB.WHITE;
	}

	/**
	 * Calculates the intensity of the light based on the distance to the light.
	 * 
	 * @param distance the distance from the point being illuminated to the light source.
	 * @return the intensity of light, a double value between 0 and 1.
	 */
	public abstract double getIntensity(double distance);

	public Light(Point3D position) {
		this.position = position;
		this.color = ColorRGB.WHITE;
	}

	public Light(Point3D position, ColorRGB color) {
		this.position = position;
		this.color = color;
	}

	public Point3D getPosition() {
		return position;
	}
	
	public void setPosition(Point3D position) {
		this.position = position;
	}

	public ColorRGB getColor() {
		return color;
	}

	public void setColor(ColorRGB color) {
		this.color = color;
	}

}
