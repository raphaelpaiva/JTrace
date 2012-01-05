package org.jtrace;

import static java.util.Arrays.asList;

import java.util.LinkedList;
import java.util.List;

import org.jtrace.cameras.Camera;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;

/**
 * Abstract class containing the common operation and fields of a Tracer.
 */
public class Tracer {

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
				finalColor = finalColor.add(calculateDiffuseLight(light, hitMin, jay, objectMaterial));
			}
		}
		
		return finalColor;
	}

	private ColorRGB calculateDiffuseLight(Light light, Hit hit, Jay jay, Material material) {
		ColorRGB objectColor = material.getColor();
		ReflectanceCoefficient kDiffuse = material.getkDiffuse();
		
		double dotLight = calculateDiffuseContribution(light, hit, jay);
		
		double red = kDiffuse.getRed() * objectColor.getRed() * dotLight;
		double green = kDiffuse.getGreen() * objectColor.getGreen() * dotLight;
		double blue = kDiffuse.getBlue() * objectColor.getBlue() * dotLight;
		
		return new ColorRGB(red, green, blue);
	}

	protected double calculateDiffuseContribution(Light light, Hit hit, Jay jay) {
		Point3D hitPoint = calculateHitPoint(jay, hit);
		
		Vector3D lightDirection = new Vector3D(hitPoint, light.getPosition()).normal();
		double dotLight = lightDirection.dot(hit.getNormal().normal());
		return Math.max(dotLight, 0);
	}
	
	protected Point3D calculateHitPoint(Jay jay, Hit hit) {
		double x = jay.getOrigin().getX();
		double y = jay.getOrigin().getY();
		double z = jay.getOrigin().getZ();
		
		x += jay.getDirection().getX() * hit.getT();
		y += jay.getDirection().getY() * hit.getT();
		z += jay.getDirection().getZ() * hit.getT();
		
		return new Point3D(x, y, z);
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
	 * Renders the scene.
	 * The {@link Jay} casting strategy is defined by the {@link Camera} used in the scene.
	 * @param scene the {@link Scene} to be rendered.
	 * @param viewPlane this should change =p.
	 */
	public void render(Scene scene, ViewPlane viewPlane) {
	  final int hres = viewPlane.getHres();
    final int vres = viewPlane.getVres();
    final Camera camera = scene.getCamera();

    fireStart(viewPlane);

    for (int r = 0; r < vres; r++) {
        for (int c = 0; c < hres; c++) {
            final Jay jay = camera.createJay(r, c, vres, hres);

            final ColorRGB color = cast(scene, jay);

            fireAfterTrace(color, c, r);
        }
    }

    fireFinish();
	}

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
