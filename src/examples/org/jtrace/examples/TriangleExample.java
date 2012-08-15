package org.jtrace.examples;

import org.jtrace.Materials;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Triangle;
import org.jtrace.lights.Light;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.AmbientShader;
import org.jtrace.shader.DiffuseShader;

public class TriangleExample {
	
	private static Point3D LEFT_VERTEX = new Point3D(-10, 0, 0);
	private static Point3D RIGHT_VERTEX = new Point3D(10, 0, 0);
	private static Point3D TOP_VERTEX = new Point3D(0, 20, 0);
	
	private static Triangle TRIANGLE = new Triangle(LEFT_VERTEX, RIGHT_VERTEX, TOP_VERTEX, Materials.matte(ColorRGB.WHITE));
	
	public static void main(String[] args) {
		
		final ViewPlane viewPlane = new ViewPlane(1024, 768);

        final Point3D eye = new Point3D(0, 0, 200);
        final Point3D lookAt = new Point3D(0, 0, 100);
        final Vector3D up = new Vector3D(0, 1, 0);

        final Light light = new Light(0, 0, 10);

        final Camera pinHoleCamera = new PinHoleCamera(eye, lookAt, up);
        pinHoleCamera.setZoomFactor(10);

        final Scene scene = new Scene().add(TRIANGLE).add(light).setCamera(pinHoleCamera);

        final Tracer tracer = new Tracer();

        tracer.addListeners(new ImageListener("triangle.png", "png"), new TimeListener());
        
        tracer.addShaders(new AmbientShader(), new DiffuseShader());

        tracer.render(scene, viewPlane);
	}
}

