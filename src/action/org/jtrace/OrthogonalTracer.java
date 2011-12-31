package org.jtrace;

import java.io.IOException;

import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Sphere;
import org.jtrace.listeners.ImageListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;

/**
 * The simplest Tracer, it traces Rays that are all parallel to each other from
 * a fixed ViewPlane at z = 100.
 * 
 */
public class OrthogonalTracer extends Tracer {
	private static final int VIEW_PLANE_POSITION = 100;

	/**
	 * Traces Rays that are all parallel to each other from a fixed ViewPlane at
	 * z = 100.
	 */
	public void render(Scene scene, ViewPlane viewPlane) {
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

				Point3D origin = new Point3D(x, y, VIEW_PLANE_POSITION);

				Jay jay = new Jay(origin, direction);

				ColorRGB color = cast(scene, jay);

				fireAfterTrace(color, c, r);
			}
		}

		fireFinish();
	}

	public static void main(String[] args) throws IOException {
		ViewPlane viewPlane = new ViewPlane(1024, 768, 0.5);

		final Point3D centerRed = new Point3D(0, 0, -10);
		final Point3D centerBlue = new Point3D(0, 0, -100);
		final Point3D planePoint = new Point3D(0, 0, -300);
		final Vector3D planeNormal = new Vector3D(0, 0, 1);

		final ReflectanceCoefficient kAmbient = new ReflectanceCoefficient(1, 1, 1);
		final ReflectanceCoefficient kDiffuse = new ReflectanceCoefficient(1.0, 1.0, 1.0);
		
		final Material redMaterial = new Material(ColorRGB.RED, kAmbient, kDiffuse);
		final Material blueMaterial = new Material(ColorRGB.BLUE, kAmbient, kDiffuse);
		final Material purpleMaterial = new Material(ColorRGB.PURPLE, kAmbient, kDiffuse);
		
		final Sphere red = new Sphere(centerRed, 100, redMaterial);
		final Sphere blue = new Sphere(centerBlue, 180, blueMaterial);
		final Plane purple = new Plane(planePoint, planeNormal, purpleMaterial);

		Scene scene = new Scene().add(blue, red, purple);

		OrthogonalTracer ot = new OrthogonalTracer();

		ot.addListeners(new ImageListener("result.png", "png"));

		ot.render(scene, viewPlane);
	}

}
