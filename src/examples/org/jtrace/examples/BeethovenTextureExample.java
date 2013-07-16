package org.jtrace.examples;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.jtrace.Material;
import org.jtrace.MultiThreadTracer;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.examples.io.BeethovenPLYExample;
import org.jtrace.geometry.TriangleMesh;
import org.jtrace.io.PlyReader;
import org.jtrace.lights.Light;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.Shaders;

public class BeethovenTextureExample {
	public static void main(String[] args) throws IOException {
		Tracer tracer = new MultiThreadTracer();
		tracer.addListeners(new ImageListener("beethoven-marble.png", "png"), new TimeListener());
		tracer.addShaders(Shaders.ambientShader(), Shaders.diffuseShader(), Shaders.specularShader(64));
		
		tracer.render(createScene(), new ViewPlane(1920, 1080));
	}
	
	public static Scene createScene() throws IOException {
		InputStream is = BeethovenPLYExample.class.getResourceAsStream("beethoven.ply");
		BufferedImage earthTexture = ImageIO.read(EarthExample.class.getResourceAsStream("marble.jpg"));
		Material material = new Material(new ReflectanceCoefficient(0.07, 0.07, 0.07), new ReflectanceCoefficient(0.7, 0.7, 0.7), earthTexture);
		TriangleMesh beethoven;
 
		beethoven = PlyReader.read(is, material);
		Scene scene = new Scene();
		
		scene.add(beethoven);
		scene.add(new Light(0, 0, 5));
		
		Camera camera = new PinHoleCamera(new Point3D(0, 0, 10), Point3D.ORIGIN, Vector3D.UNIT_Y);
		
		camera.setZoomFactor(50);
		
		scene.setCamera(camera);
		
		return scene;
	}
}
