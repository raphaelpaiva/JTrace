package org.jtrace;

import org.jtrace.primitives.ColorRGB;

/**
 * A listener interface that respond to {@link Tracer} events.
 * 
 */
public interface TracerListener {
	/**
	 * Called at the Tracer initialization, just before it begin the rendering.
	 * 
	 * @param viewPlane
	 *            the {@link ViewPlane} used to render the scene.
	 */
	public void start(ViewPlane viewPlane);

	/**
	 * Called just after the color calculation, whether the {@link Jay} has or
	 * hasn't collided with an object.
	 * 
	 * @param color
	 *            the color of the pixel
	 * @param x
	 *            the pixel column at the {@link ViewPlane}
	 * @param y
	 *            the pixel row at the {@link ViewPlane}
	 */
	public void afterTrace(ColorRGB color, int x, int y);

	/**
	 * Called after the rendering was complete.
	 */
	public void finish();
}
