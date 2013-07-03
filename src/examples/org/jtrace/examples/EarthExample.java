package org.jtrace.examples;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jtrace.Material;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Sphere;
import org.jtrace.interceptor.ShadowInterceptor;
import org.jtrace.lights.Light;
import org.jtrace.listeners.ImageListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.Shaders;

public class EarthExample {
	public static void main(String[] args) throws IOException {
		Scene scene = new Scene();
		
		BufferedImage earthTexture = ImageIO.read(EarthExample.class.getResourceAsStream("earthExample.jpg"));
		BufferedImage moonTexture = ImageIO.read(EarthExample.class.getResourceAsStream("moon.jpg"));
		Material earthMaterial = new Material(ColorRGB.WHITE, new ReflectanceCoefficient(0.07, 0.07, 0.07), new ReflectanceCoefficient(0.7, 0.7, 0.7), earthTexture);
		Material moonMaterial = new Material(ColorRGB.WHITE, new ReflectanceCoefficient(0.07, 0.07, 0.07), new ReflectanceCoefficient(0.7, 0.7, 0.7), moonTexture);
		
		Sphere earth = new Sphere(Point3D.ORIGIN, 10, earthMaterial);
		Sphere moon = new Sphere(new Point3D(-20, 7, 13), 10 * 0.273, moonMaterial);
		
		scene.add(earth);
		scene.add(moon);
		
		scene.add(new Light(new Point3D(-50, 50, -50)));
		
		Camera camera = new PinHoleCamera(new Point3D(0, 0, -20), Point3D.ORIGIN, Vector3D.UNIT_Y);
		camera.setZoomFactor(12);
		
		scene.setCamera(camera);
		
		Tracer tracer = new Tracer();
		tracer.addInterceptors(new ShadowInterceptor());
		tracer.addListeners(new ImageListener("earth.png", "png"));
		tracer.addShaders(Shaders.ambientShader(), Shaders.diffuseShader(), Shaders.specularShader(64));
		
		tracer.render(scene, new ViewPlane(1920, 1080));
		
	}
}
