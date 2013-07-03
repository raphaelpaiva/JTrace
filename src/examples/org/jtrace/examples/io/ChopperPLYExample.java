package org.jtrace.examples.io;

import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

import org.jtrace.Materials;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.TriangleMesh;
import org.jtrace.io.PlyReader;
import org.jtrace.lights.Light;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.Shaders;

public class ChopperPLYExample {
	public static void main(String[] args) throws IOException {
		
		InputStream is = ChopperPLYExample.class.getResourceAsStream("chopper.ply");
		TriangleMesh chopper;

		chopper = PlyReader.read(is, Materials.metallic(ColorRGB.WHITE));
		Scene scene = new Scene();

		scene.add(chopper);
		scene.add(new Light(0, 10000, 10000));

		Camera camera = new PinHoleCamera(new Point3D(-200, -400, -100),
				new Point3D(-40, 60, -10), Vector3D.UNIT_Z);

		camera.setZoomFactor(10);

		scene.setCamera(camera);

		Tracer tracer = new Tracer();

		tracer.addShaders(Shaders.ambientShader(), Shaders.diffuseShader(),
				Shaders.specularShader(64));

		tracer.addListeners(new ImageListener("chopper.png", "png"),
				new TimeListener());

		tracer.render(scene, new ViewPlane(1280, 768));

		Toolkit.getDefaultToolkit().beep();
	}
}
