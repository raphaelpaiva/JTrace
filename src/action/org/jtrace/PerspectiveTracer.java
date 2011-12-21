package org.jtrace;

import java.io.IOException;

import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.listeners.ImageListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
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
		int hres = viewPlane.getHres();
		int vres = viewPlane.getVres();
		double pixelSize = viewPlane.getPixelSize();

		fireStart(viewPlane);

		for (int r = 0; r < vres; r++) {
			for (int c = 0; c < hres; c++) {
				Vector3D jayDirection = calculateJayDirection(hres, vres, pixelSize, r, c);

				Jay jay = new Jay(eyePoint, jayDirection);

				ColorRGB color = cast(scene, jay);

				fireAfterTrace(color, c, r);
			}
		}

		fireFinish();
	}

	/**
	 * Calculates the Jay Direction.
	 * 
	 * 
	 * @param hres
	 * @param vres
	 * @param pixelSize
	 * @param r
	 * @param c
	 * @return
	 */
	protected Vector3D calculateJayDirection(int hres, int vres, double pixelSize, int r, int c) {
		double xDirection = calculateRayCoordinate(hres, pixelSize, c);
		double yDirection = calculateRayCoordinate(vres, pixelSize, r);

		Vector3D jayDirection = new Vector3D(xDirection, yDirection, -viewPlaneDistance);
		
		return jayDirection.normal();
	}

	/**
	 * Calculates the Ray direction coordinate according to the res param. <br>
	 * 
	 * If res is vres, calculates the y coordinate. <br>
	 * If res is hres, calculates the x coordinate. <br>
	 * 
	 * @param res vres or hres.
	 * @param pixelSize the pixel size.
	 * @param viewPlaneCoordinate the coordinate on the viewPlane.
	 * @return a coordinate of the ray according to the res param passed.
	 */
	private double calculateRayCoordinate(int res, double pixelSize, int viewPlaneCoordinate) {
		return pixelSize * (viewPlaneCoordinate - 0.5 * (res - 1.0));
	}


	public static void main(String[] args) throws IOException {
		ViewPlane viewPlane = new ViewPlane(1024, 768, 0.05);
		
		final Point3D centerRed = new Point3D(10, 0, -10);
		final Point3D centerBlue = new Point3D(-10, 0, -20);
		
		final ReflectanceCoefficient kAmbient = new ReflectanceCoefficient(0.2, 0.2, 0.2);
		
		final Material redMaterial = new Material(ColorRGB.RED, kAmbient);
		final Material blueMaterial = new Material(ColorRGB.BLUE, kAmbient);
		
		final Sphere red = new Sphere(centerRed, 10, redMaterial);
		final Sphere blue = new Sphere(centerBlue, 10, blueMaterial);
		
		final Light light = new Light(0, 10, 0);
		
		Scene scene = new Scene().add(blue, red).add(light);
		
		Point3D eyePoint = new Point3D(0, 0, 10);
		double viewPlaneDistance = 10.0;
		
		PerspectiveTracer ot = new PerspectiveTracer(eyePoint, viewPlaneDistance);
		
		ot.addListeners(new ImageListener("result_Perspective.png", "png"));
		
		ot.render(scene, viewPlane);
	}

}
