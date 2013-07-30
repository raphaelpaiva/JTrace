package org.jtrace.examples;

import org.jtrace.Material;
import org.jtrace.Materials;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Quadrilateral;
import org.jtrace.lights.Light;
import org.jtrace.lights.PointLight;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.AmbientShader;
import org.jtrace.shader.DiffuseShader;

public class QuadrilateralExample {
	
	private static final Material MATERIAL = Materials.metallic(ColorRGB.BLUE);

	private static final Point3D P0 = new Point3D(-1, -1, 0);
	private static final Point3D P1 = new Point3D( 1, -1, 0);
	private static final Point3D P2 = new Point3D( 1,  1, 0);
	private static final Point3D P3 = new Point3D(-1,  1, 0);

	private static final Quadrilateral QUADRILATERAL = new Quadrilateral(P0,P1,P2,P3, MATERIAL);
	
	public static void main(String[] args) {
		
		final ViewPlane viewPlane = new ViewPlane(1024, 768);

        final Point3D eye = new Point3D(0, 0, 10);
        final Point3D lookAt = new Point3D(0, 0, 0);
        final Vector3D up = new Vector3D(0, 1, 0);

        final Light light = new PointLight(0, 0, 10);

        final Camera pinHoleCamera = new PinHoleCamera(eye, lookAt, up);
        pinHoleCamera.setZoomFactor(100);

        final Scene scene = new Scene().add(QUADRILATERAL).add(light).setCamera(pinHoleCamera);

        final Tracer tracer = new Tracer();

        tracer.addListeners(new ImageListener("quadrilateral.png", "png"), new TimeListener());
        
        tracer.addShaders(new AmbientShader(), new DiffuseShader());

        tracer.render(scene, viewPlane);
	}
}

