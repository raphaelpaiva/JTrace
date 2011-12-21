package org.jtrace;

import static java.util.Arrays.asList;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;

public class Scene {
	private Set<GeometricObject> objects = new LinkedHashSet<GeometricObject>();
	private boolean hasAmbientLight = true;
	private Set<Light> lights = new LinkedHashSet<Light>();
	private ColorRGB backgroundColor = ColorRGB.BLACK;

	public Scene withBackground(ColorRGB color) {
		backgroundColor = color;
		return this;
	}
	
	public Scene turnOffAmbientLight() {
		hasAmbientLight = false;
		return this;
	}
	
	public Scene turnOnAmbientLight() {
		hasAmbientLight = true;
		return this;
	}
	
	public Scene add(Light light) {
		lights.add(light);
		return this;
	}
	
	public Scene add(Light... paramLights) {
		lights.addAll(asList(paramLights));
		return this;
	}
	
	public Scene add(GeometricObject object) {
		objects.add(object);
		return this;
	}
	
	public Scene add(GeometricObject... paramObjects) {
		objects.addAll(asList(paramObjects));
		return this;
	}

	public Set<GeometricObject> getObjects() {
		return objects;
	}
	
	public Set<Light> getLigths() {
		return lights;
	}
	
 	public ColorRGB getBackgroundColor() {
		return backgroundColor;
	}
	
	public boolean isAmbientLightOn() {
		return hasAmbientLight;
	}
}
