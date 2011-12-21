package org.jtrace;

import static java.util.Arrays.asList;

import java.util.LinkedList;
import java.util.List;

import org.jtrace.geometry.GeometricObject;
import org.jtrace.primitives.ColorRGB;

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
		double tmin = Double.MAX_VALUE;
		ColorRGB finalColor = scene.getBackgroundColor();

		for (GeometricObject object : scene) {
			Hit hit = object.hit(jay);

			if (hit.isHit() && hit.getT() < tmin) {
				tmin = hit.getT();
				finalColor = object.getColor();
			}
		}

		return finalColor;
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
