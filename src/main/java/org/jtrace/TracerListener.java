package org.jtrace;

import org.jtrace.primitives.ColorRGB;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A listener interface that respond to {@link Tracer} events.
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = org.jtrace.listeners.ImageListener.class, name = "ImageListener"),
    @JsonSubTypes.Type(value = org.jtrace.listeners.TimeListener.class, name = "TimeListener"),
    @JsonSubTypes.Type(value = org.jtrace.swing.SwingListener.class, name = "SwingListener")
})
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
