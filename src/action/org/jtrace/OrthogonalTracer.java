package org.jtrace;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jtrace.geometry.GeometricObject;
import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Sphere;
import org.jtrace.listeners.ImageListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class OrthogonalTracer {
	private static final int DEFAULT_VIEW_PLANE_POSITION = 100;
	private List<TracerListener> listeners = new LinkedList<TracerListener>();
	
	private double viewPlanePosition = DEFAULT_VIEW_PLANE_POSITION; 

	public OrthogonalTracer() {}
	
	public OrthogonalTracer(double viewPlanePosition) {
		this.viewPlanePosition = viewPlanePosition;
	}

	public ColorRGB trace(Scene scene, Jay jay) {
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

				Point3D origin = new Point3D(x, y, viewPlanePosition);

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

	public double getViewPlanePosition() {
		return viewPlanePosition;
	}

	public void setViewPlanePosition(double viewPlanePosition) {
		this.viewPlanePosition = viewPlanePosition;
	}

	public static void main(String[] args) throws IOException {
		ViewPlane viewPlane = new ViewPlane(1024, 768, 0.5);
		
		final Point3D centerRed = new Point3D(0, 0, -100);
		final Point3D centerBlue = new Point3D(0, 0, -380);
		final Point3D planePoint = new Point3D(0, 0, -300);
		final Vector3D planeNormal = new Vector3D(0, 0, 1);
		
		final Sphere red = new Sphere(centerRed, 100, ColorRGB.RED);
		final Sphere blue = new Sphere(centerBlue, 180, ColorRGB.BLUE);
		final Plane purple = new Plane(planePoint, planeNormal, ColorRGB.PURPLE);
		
		Scene scene = new Scene().add(blue, red, purple);

		OrthogonalTracer ot = new OrthogonalTracer();
		
		ot.addListeners(new ImageListener("result.png", "png"));

		ot.render(scene, viewPlane);
	}

}
