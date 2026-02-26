package org.jtrace.examples;

import static org.jtrace.primitives.ColorRGB.WHITE;
import static org.jtrace.primitives.Point3D.ORIGIN;
import static org.jtrace.primitives.Vector3D.UNIT_Y;

import java.io.IOException;

import org.jtrace.Materials;
import org.jtrace.MultiThreadTracer;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Sphere;
import org.jtrace.interceptor.ShadowInterceptor;
import org.jtrace.lights.DecayingPointLight;
import org.jtrace.lights.PointLight;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.Shaders;

public class BlackAndWhiteExample {
	public static void main(String[] args) throws IOException {
		Tracer tracer = new MultiThreadTracer();
		tracer.addListeners(new ImageListener("blackAndWhite.png", "png"), new TimeListener());
		tracer.addShaders(Shaders.diffuseShader(), Shaders.specularShader(32));
		tracer.addInterceptors(new ShadowInterceptor());
		
		tracer.render(createScene(), new ViewPlane(1920, 1080));
	}
	
	public static Scene createScene() throws IOException {
		Plane plane = new Plane(ORIGIN, UNIT_Y, Materials.matte(WHITE));
		Sphere sphere = new Sphere(new Point3D(0, 10, 0), 10, Materials.metallic(WHITE));
		
		Scene scene = new Scene();
		
		scene.add(new DecayingPointLight(-50, 50, 0, 5000));
		scene.add(plane, sphere);
		
		Camera camera = new PinHoleCamera(new Point3D(-100, 10, 100), Point3D.ORIGIN, Vector3D.UNIT_Y);
		
		camera.setZoomFactor(10);
		
		scene.setCamera(camera);
		
		return scene;
	}
}
