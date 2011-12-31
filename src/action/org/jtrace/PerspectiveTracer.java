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
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;

public class PerspectiveTracer extends Tracer {
	
	public PerspectiveTracer() {
		super();
	}

	public void render(Scene scene, ViewPlane viewPlane) {
		int hres = viewPlane.getHres();
		int vres = viewPlane.getVres();
		Camera camera = scene.getCamera();

		fireStart(viewPlane);

		for (int r = 0; r < vres; r++) {
			for (int c = 0; c < hres; c++) {
				Jay jay = camera.createJay(r, c, vres, hres);

				ColorRGB color = cast(scene, jay);

				fireAfterTrace(color, c, r);
			}
		}

		fireFinish();
	}

	public static void main(String[] args) throws IOException {
		ViewPlane viewPlane = new ViewPlane(1024, 768);
		
		final Point3D lookAt = new Point3D(0, 0, 0);
		final Point3D eye = new Point3D(-15, -15, 100);
		final Vector3D up = new Vector3D(0, 1, 0);
		
		final Point3D centerRed  = new Point3D(0, 0, -10);
		final Point3D centerBlue = new Point3D(-10, 0, -20);
		
		final Point3D planePoint = new Point3D(0, 20, 0);
		final Vector3D planeNormal = new Vector3D(0, -1, 0);
		
		final ReflectanceCoefficient kAmbient = new ReflectanceCoefficient(0.07, 0.07, 0.07);
		final ReflectanceCoefficient kDiffuse = new ReflectanceCoefficient(0.3, 0.3, 0.3);
		
		final Material redMaterial = new Material(ColorRGB.RED, kAmbient, kDiffuse);
		final Material blueMaterial = new Material(ColorRGB.BLUE, kAmbient, kDiffuse);
		final Material planeMaterial = new Material(ColorRGB.YELLOW, kAmbient, kDiffuse);
		
		final Sphere red = new Sphere(centerRed, 10, redMaterial);
		final Sphere blue = new Sphere(centerBlue, 10, blueMaterial);
		
		final Plane plane = new Plane(planePoint, planeNormal, planeMaterial);
		final Light light = new Light(0, -20, 10);
		
		final Camera pinHoleCamera = new PinHoleCamera(eye, lookAt, up);
		
		Scene scene = new Scene().add(blue, red, plane).add(light).setCamera(pinHoleCamera);
		
		PerspectiveTracer ot = new PerspectiveTracer();
		
		ot.addListeners(new ImageListener("result_Perspective.png", "png"), new TimeListener());
		
		ot.render(scene, viewPlane);
	}

}
