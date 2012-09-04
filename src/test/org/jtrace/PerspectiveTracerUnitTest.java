package org.jtrace;

import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.AmbientShader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PerspectiveTracerUnitTest {
	
	private static final Point3D EYE_POINT = new Point3D(0, 0, 10);
	private static final Point3D ORIGIN = new Point3D(0, 0, 0);
	private static final Vector3D UNIT_Y = new Vector3D(0, 1, 0);
	
	private static final ReflectanceCoefficient KAMBIENT = new ReflectanceCoefficient(1.0, 1.0, 1.0);
	private static final ReflectanceCoefficient KDIFFUSE = new ReflectanceCoefficient(1.0, 1.0, 1.0);
	private static final int SPHERE_RADIUS = 1;
	
	private static final Camera PIN_HOLE_CAMERA = new PinHoleCamera(EYE_POINT, ORIGIN, UNIT_Y);
	
	@Test
	public void testRender_RedSphereInFrontOfViewPlane() {
		final Point3D center = new Point3D(0, 0, -5);
		final Material material = new Material(ColorRGB.RED, KAMBIENT, KDIFFUSE);
		
		final Sphere sphere = new Sphere(center, SPHERE_RADIUS, material);
		final int hres = 1;
		final int vres = 1;
		
		Light light = new Light(new Point3D(0, 0, 0));
		
		Scene scene = new Scene().add(sphere).add(light);

		Vector3D jayDirection = PIN_HOLE_CAMERA.createJay(0, 0, vres, hres).getDirection();
		Jay jay = new Jay(EYE_POINT, jayDirection);
		
		Tracer tracer = new Tracer();
		tracer.addShaders(new AmbientShader());
		
		Assert.assertEquals(tracer.trace(scene, jay), ColorRGB.RED);
	}
	
	@Test
	public void testRender_RedSphereInFrontOfViewPlane_NoAmbientLight() {
		final Point3D center = new Point3D(0, 0, -5);
		final Material material = new Material(ColorRGB.RED, KAMBIENT, KDIFFUSE);
		
		final Sphere sphere = new Sphere(center, SPHERE_RADIUS, material);
		final int hres = 1;
		final int vres = 1;

		Scene scene = new Scene().add(sphere);

		Vector3D jayDirection = PIN_HOLE_CAMERA.createJay(0, 0, vres, hres).getDirection();
		Jay jay = new Jay(EYE_POINT, jayDirection);
		
		Assert.assertEquals(new Tracer().trace(scene, jay), ColorRGB.BLACK);
	}
	
	@Test
	public void testRender_RedSphereBehindViewPlane() {
		final Point3D center = new Point3D(0, 0, 50);
		final Material material = new Material(ColorRGB.RED, KAMBIENT, KDIFFUSE);
		
		final Sphere sphere = new Sphere(center, SPHERE_RADIUS, material);
		final int hres = 1;
		final int vres = 1;
		
		Scene scene = new Scene().add(sphere).withBackground(ColorRGB.GREEN);
		
		Vector3D jayDirection = PIN_HOLE_CAMERA.createJay(0, 0, vres, hres).getDirection();
		Jay jay = new Jay(EYE_POINT, jayDirection);
		
		Tracer tracer = new Tracer();
		tracer.addShaders(new AmbientShader());
		
		Assert.assertEquals(tracer.trace(scene, jay), ColorRGB.GREEN);
	}
}
