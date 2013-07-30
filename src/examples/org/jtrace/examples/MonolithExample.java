package org.jtrace.examples;

import org.jtrace.Materials;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Triangle;
import org.jtrace.lights.Light;
import org.jtrace.lights.PointLight;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.Shaders;

public class MonolithExample {
	
	private static Point3D LEFT_VERTEX = new Point3D(-10, 0, 0);
	private static Point3D RIGHT_VERTEX = new Point3D(10, 0, 0);
	private static Point3D TOP_VERTEX = new Point3D(0, 20, 0);
	
	private static Triangle TRIANGLE = new Triangle(LEFT_VERTEX, RIGHT_VERTEX, TOP_VERTEX, Materials.metallic(ColorRGB.WHITE));
	
	public static void main(String[] args) {
		
		final ViewPlane viewPlane = new ViewPlane(1920, 1080);

        final Point3D eye = new Point3D(-1000, 0, 500);
        final Point3D lookAt = TOP_VERTEX;
        final Vector3D up = new Vector3D(0, 1, 0);

        final Light light = new PointLight(0, 0, 10);

        final Camera pinHoleCamera = new PinHoleCamera(eye, lookAt, up);
        pinHoleCamera.setZoomFactor(20);

        Point3D t2v1 = TOP_VERTEX;
        Point3D t2v2 = new Point3D(10, 40, 0);
        Point3D t2v3 = new Point3D(-10, 40, 0);
        
        Triangle t2 = new Triangle(t2v1, t2v2, t2v3, Materials.metallic(ColorRGB.WHITE));
        
        Point3D t3v1 = new Point3D(10, 40, 0);
        Point3D t3v2 = TOP_VERTEX;
        Point3D t3v3 = new Point3D(10, 0, 0);
        
        Triangle t3 = new Triangle(t3v1, t3v2, t3v3, Materials.metallic(ColorRGB.WHITE));
        
        Point3D t4v1 = new Point3D(-10, 0, 0);
        Point3D t4v2 = TOP_VERTEX;
        Point3D t4v3 = new Point3D(-10, 40, 0);
        
        Triangle t4 = new Triangle(t4v1, t4v2, t4v3, Materials.metallic(ColorRGB.WHITE));
        
        final Scene scene = new Scene().add(TRIANGLE, t2, t3, t4).add(light).setCamera(pinHoleCamera);

        final Tracer tracer = new Tracer();

        tracer.addListeners(new ImageListener("monolith.png", "png"), new TimeListener());
        
        tracer.addShaders(Shaders.ambientShader(), Shaders.diffuseShader(), Shaders.specularShader(4));

        tracer.render(scene, viewPlane);
	}
}

