package org.jtrace;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jtrace.geometry.GeometricObject;
import org.jtrace.geometry.Sphere;
import org.jtrace.listeners.ImageListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class OrthogonalTracer {
	private static ColorRGB BACKGROUND_COLOR = ColorRGB.BLACK;
	private static ColorRGB DEFAULT_COLOR = ColorRGB.RED;

	private List<TracerListener> listeners = new LinkedList<TracerListener>();

	public ColorRGB trace(Scene scene, Jay jay) {
		for (GeometricObject object : scene) {
			if (object.hit(jay)) {
				return DEFAULT_COLOR;
			}
		}

		return BACKGROUND_COLOR;
	}

	public void render(Scene scene, ViewPlane viewPlane) throws IOException {
		double x, y;
		int hres = viewPlane.getHres();
		int vres = viewPlane.getVres();
		double s = viewPlane.getPixelSize();

		Vector3D direction = new Vector3D(0, 0, -1);

		fireStart(viewPlane);

		for (int r = 0; r < vres; r++) {
			for (int c = 0; c < hres; c++) {
				x = s * (c - 0.5 * (hres - 1.0));
				y = s * (r - 0.5 * (vres - 1.0));

				Point3D origin = new Point3D(x, y, 100);

				Jay jay = new Jay(origin, direction);

				ColorRGB color = trace(scene, jay);

				fireAfterTrace(color, c, r);
			}
		}

		fireFinish();
	}

	private void fireFinish() {
		for (TracerListener listener : listeners) {
			listener.finish();
		}
	}

	private void fireAfterTrace(ColorRGB color, int c, int r) {
		for (TracerListener listener : listeners) {
			listener.afterTrace(color, c, r);
		}
	}

	private void fireStart(ViewPlane viewPlane) {
		for (TracerListener listener : listeners) {
			listener.start(viewPlane);
		}
	}
	
	public void addListeners(TracerListener... paramListeners) {
		listeners.addAll(asList(paramListeners));
	}

	public static void main(String[] args) throws IOException {
		ViewPlane viewPlane = new ViewPlane(300, 300, 1.0);
		Point3D c = new Point3D(-42.5, 0, 0);
		Sphere s = new Sphere(c, 85.0f);

		Scene scene = new Scene();

		scene.add(s);

		OrthogonalTracer ot = new OrthogonalTracer();
		
		ot.addListeners(new ImageListener("result.jpg", "jpeg"));

		ot.render(scene, viewPlane);
	}

}
