package org.jtrace.examples.io;

import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

import org.jtrace.Materials;
import org.jtrace.MultiThreadTracer;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.TriangleMesh;
import org.jtrace.interceptor.ShadowInterceptor;
import org.jtrace.io.PlyReader;
import org.jtrace.lights.PointLight;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.Shaders;

public class BeethovenPLYExample {
	public static void main(String[] args) throws IOException {
		InputStream is = BeethovenPLYExample.class.getResourceAsStream("beethoven.ply");
		TriangleMesh beethoven;
 
		beethoven = PlyReader.read(is, Materials.metallic(ColorRGB.WHITE));
		Scene scene = new Scene();
		
		scene.add(beethoven);
		scene.add(new PointLight(0, 0, 5));
		
		Camera camera = new PinHoleCamera(new Point3D(0, 0, 10), Point3D.ORIGIN, Vector3D.UNIT_Y);
		
		camera.setZoomFactor(50);
		
		scene.setCamera(camera);
		
		Tracer tracer = new MultiThreadTracer();
		
		tracer.addShaders(Shaders.ambientShader(), Shaders.diffuseShader(), Shaders.specularShader(64));
		
		tracer.addInterceptors(new ShadowInterceptor());
		
		tracer.addListeners(new ImageListener("beethoven.png", "png"), new TimeListener());
		
		tracer.render(scene, new ViewPlane(1280, 768));
		
		Toolkit.getDefaultToolkit().beep();
	}

}
