package org.jtrace;

import java.io.IOException;

import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class PerspectiveTracer extends Tracer {

	public PerspectiveTracer() {
		super();
	}

	@Override
	public void render(final Scene scene, final ViewPlane viewPlane) {
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

	public static void main(final String[] args) throws IOException {
		final ViewPlane viewPlane = new ViewPlane(1024, 768);

		final Point3D lookAt = new Point3D(0, 0, 0);
		final Point3D eye = new Point3D(0, -100, -15);
		final Vector3D up = new Vector3D(0, 1, 0);

		final Point3D centerRed  = new Point3D(0, 0, -30);
		final Point3D centerBlue = new Point3D(0, 0, -30);

		final Point3D planePoint = new Point3D(0, 20, 0);
		final Vector3D planeNormal = new Vector3D(0, -1, 0);

		final Sphere red = new Sphere(centerRed, 10, Materials.matte(ColorRGB.RED));
		final Sphere blue = new Sphere(centerBlue, 10, Materials.matte(ColorRGB.BLUE));

		final Plane plane = new Plane(planePoint, planeNormal, Materials.matte(ColorRGB.YELLOW));
		final Light light = new Light(0, -20, 10);

		final Camera pinHoleCamera = new PinHoleCamera(eye, lookAt, up);

		final Scene scene = new Scene().add(blue, red, plane).add(light).setCamera(pinHoleCamera);

		final PerspectiveTracer ot = new PerspectiveTracer();

		ot.addListeners(new ImageListener("result_Perspective.png", "png"), new TimeListener());

		ot.render(scene, viewPlane);
	}

}
