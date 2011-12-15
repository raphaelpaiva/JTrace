package org.jtrace;

import static java.util.Arrays.asList;

import java.util.LinkedList;
import java.util.List;

import org.jtrace.geometry.GeometricObject;
import org.jtrace.primitives.ColorRGB;

public abstract class Tracer {

	private List<TracerListener> listeners = new LinkedList<TracerListener>();

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
	
	public void addListeners(TracerListener... paramListeners) {
		listeners.addAll(asList(paramListeners));
	}
}
