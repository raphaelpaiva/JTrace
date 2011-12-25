package org.jtrace;

import static java.util.Arrays.asList;

import java.util.LinkedList;
import java.util.List;

import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;

/**
 * Abstract class containing the common operation and fields of a Tracer.
 */
public abstract class Tracer {

	private List<TracerListener> listeners = new LinkedList<TracerListener>();

	/**
	 * Casts the given {@link Jay}.
	 * 
	 * @param scene
	 *            the {@link Scene} containing the objects to rendered.
	 * @param jay
	 *            the {@link Jay} to be casted.
	 * @return the color of the object hit by the {@link Jay} or the
	 *         {@link Scene}'s background color if there was no hit.
	 */
	public ColorRGB cast(Scene scene, Jay jay) {
		double tMin = Double.MAX_VALUE;
		GeometricObject hitObject = null;
		Hit hitMin = null;
		ColorRGB finalColor = scene.getBackgroundColor();

		for (GeometricObject object : scene.getObjects()) {
			Hit hit = object.hit(jay);

			if (hit.isHit() && hit.getT() < tMin) {
				tMin = hit.getT();
				hitObject = object;
				hitMin = hit;
			}
		}

		// if there was a collision, calculate illumination
		if (hitObject != null) {
			Material objectMaterial = hitObject.getMaterial();
			finalColor = ColorRGB.BLACK;
			
			//ambient light
			if (scene.isAmbientLightOn()) {
				finalColor = finalColor.add(calculateAmbientLight(objectMaterial));
			}

			//diffuse light
			for (Light light : scene.getLigths()) {
				finalColor = finalColor.add(calculateDiffuseLight(light, hitMin, objectMaterial));
			}
		}
		
		return finalColor;
	}

	private ColorRGB calculateDiffuseLight(Light light, Hit hit, Material material) {
		ColorRGB objectColor = material.getColor();
		ReflectanceCoefficient kDiffuse = material.getkDiffuse();
		
		Vector3D lightDirection = new Vector3D(light.getPosicao());
		double dotLight = lightDirection.dot(hit.getNormal());
		
		double red = kDiffuse.getRed() * objectColor.getRed() * dotLight;
		double green = kDiffuse.getGreen() * objectColor.getGreen() * dotLight;
		double blue = kDiffuse.getBlue() * objectColor.getBlue() * dotLight;
		
		return new ColorRGB(red, green, blue);
	}

	private ColorRGB calculateAmbientLight(Material material) {
		ColorRGB objectColor = material.getColor();
		ReflectanceCoefficient kAmbient = material.getkAmbient();
		
		double red = kAmbient.getRed() * objectColor.getRed();
		double green = kAmbient.getGreen() * objectColor.getGreen();
		double blue = kAmbient.getBlue() * objectColor.getBlue();
		
		return new ColorRGB(red, green, blue);
	}

	/**
	 * Abstract method that should define the {@link Jay} casting strategy.
	 * 
	 * @param scene the {@link Scene} to be rendered.
	 * @param viewPlane this should change =p.
	 */
	public abstract void render(Scene scene, ViewPlane viewPlane);

	protected void fireFinish() {
		for (TracerListener listener : listeners) {
			listener.finish();
		}
	}

	protected void fireAfterTrace(ColorRGB color, int c, int r) {
		for (TracerListener listener : listeners) {
			listener.afterTrace(color, c, r);
		}
	}

	protected void fireStart(ViewPlane viewPlane) {
		for (TracerListener listener : listeners) {
			listener.start(viewPlane);
		}
	}

	/**
	 * Adds a {@link TracerListener} to the {@link Tracer}.
	 * 
	 * @param paramListeners one or more listeners.
	 */
	public void addListeners(TracerListener... paramListeners) {
		listeners.addAll(asList(paramListeners));
	}
}
