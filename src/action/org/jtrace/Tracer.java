package org.jtrace;

import static java.util.Arrays.asList;

import java.util.LinkedList;
import java.util.List;

import org.jtrace.cameras.Camera;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.shader.Shader;

/**
 * Abstract class containing the common operation and fields of a Tracer.
 */
public class Tracer {

	private List<TracerListener> listeners = new LinkedList<TracerListener>();
	private List<Shader> shaders = new LinkedList<Shader>();

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
			finalColor = ColorRGB.BLACK;
			
			for (Light light : scene.getLigths()) {
				for (Shader shader : shaders) {
					ColorRGB shaderColor = shader.shade(light, hitMin, jay, hitObject);
					finalColor = finalColor.add(shaderColor);
				}
			}
		}
		
		return finalColor;
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
	
	
	/**
	 * Adds a {@link Shader} to the {@link Tracer}.
	 * 
	 * @param paramShaders one or more shaders.
	 */
	public void addShaders(Shader... paramShaders) {
		shaders.addAll(asList(paramShaders));
	}
}
