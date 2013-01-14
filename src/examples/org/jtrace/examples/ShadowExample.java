package org.jtrace.examples;

import org.jtrace.Materials;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Sphere;
import org.jtrace.interceptor.ShadowInterceptor;
import org.jtrace.lights.Light;
import org.jtrace.listeners.ImageListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.Shaders;

public class ShadowExample {
	public static void main(String[] args) {
		Sphere sphere = new Sphere(Point3D.ORIGIN, 50, Materials.matte(ColorRGB.GREEN));
		
		Light light = new Light(new Point3D(0, 100, -50));
		
		Scene scene = new Scene();
		
		scene.add(sphere).add(light).setCamera(new PinHoleCamera(new Point3D(0, 0, -100), Point3D.ORIGIN, Vector3D.UNIT_Y));
		
		Tracer tracer = new Tracer();
		
		tracer.addInterceptors(new ShadowInterceptor());
		tracer.addShaders(Shaders.ambientShader(), Shaders.diffuseShader(), Shaders.specularShader(16));
		tracer.addListeners(new ImageListener("ShadowExample.png", "png"));
		
		tracer.render(scene, new ViewPlane(1024, 768));
	}
}
