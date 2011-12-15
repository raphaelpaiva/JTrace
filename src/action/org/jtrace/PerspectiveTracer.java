package org.jtrace;

import java.io.IOException;

import org.jtrace.geometry.Sphere;
import org.jtrace.listeners.ImageListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class PerspectiveTracer extends Tracer {
	
	private Point3D eyePoint;
	
	private double viewPlaneDistance;
	
	public PerspectiveTracer(Point3D eyePoint, double viewPlaneDistance) {
		super();
		this.eyePoint = eyePoint;
		this.viewPlaneDistance = viewPlaneDistance;
	}

	public void render(Scene scene, ViewPlane viewPlane) {
		double xDirection, yDirection;
		int hres = viewPlane.getHres();
		int vres = viewPlane.getVres();
		double s = viewPlane.getPixelSize();

		fireStart(viewPlane);

		for (int r = 0; r < vres; r++) {
			for (int c = 0; c < hres; c++) {
				xDirection = s * (c - 0.5 * (hres - 1.0));
				yDirection = s * (r - 0.5 * (vres - 1.0));

				Vector3D jayDirection = new Vector3D(xDirection, yDirection, -viewPlaneDistance);

				Jay jay = new Jay(eyePoint, jayDirection);

				ColorRGB color = cast(scene, jay);

				fireAfterTrace(color, c, r);
			}
		}

		fireFinish();
	}


	public static void main(String[] args) throws IOException {
		ViewPlane viewPlane = new ViewPlane(24, 16, 0.05);
		
		final Point3D centerRed = new Point3D(0, 0, -10);
		final Point3D centerBlue = new Point3D(0, 0, -100);
		
		final Sphere red = new Sphere(centerRed, 10, ColorRGB.RED);
		final Sphere blue = new Sphere(centerBlue, 18, ColorRGB.BLUE);
		
		Scene scene = new Scene().add(blue, red);
		
		Point3D eyePoint = new Point3D(0, 0, 10);
		double viewPlaneDistance = 20.0;
		
		PerspectiveTracer ot = new PerspectiveTracer(eyePoint, viewPlaneDistance);
		
		ot.addListeners(new ImageListener("result_Perspective.png", "png"));

		ot.render(scene, viewPlane);
	}

}
