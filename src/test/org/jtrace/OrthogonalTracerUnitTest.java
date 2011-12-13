package org.jtrace;

import org.jtrace.geometry.Sphere;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OrthogonalTracerUnitTest {
	
	private static final OrthogonalTracer TRACER = new OrthogonalTracer();
	private static final Vector3D JAY_DIRECTION = new Vector3D(0, 0, -1);
	private static final Point3D JAY_ORIGIN = new Point3D(0, 1, 0);
	private static final Jay JAY = new Jay(JAY_ORIGIN, JAY_DIRECTION);
	private static final int SPHERE_RADIUS = 1;
	
	@Test
	public void testRender_RedSphereInFrontOfViewPlane() {
		final Point3D center = new Point3D(0, 0, -5);
		final Sphere sphere = new Sphere(center, SPHERE_RADIUS, ColorRGB.RED);
		
		Scene scene = new Scene().add(sphere);

		Assert.assertEquals(TRACER.trace(scene, JAY), ColorRGB.RED);
	}
	
	@Test
	public void testRender_RedSphereBehindViewPlane() {
		final Point3D center = new Point3D(0, 0, 50);
		final Sphere sphere = new Sphere(center, SPHERE_RADIUS, ColorRGB.RED);
		
		Scene scene = new Scene().add(sphere).withBackground(ColorRGB.GREEN);
		
		Assert.assertEquals(TRACER.trace(scene, JAY), ColorRGB.GREEN);
	}
	
	@Test
	public void testRender_TwoSpheresRedInFront() {
		final Point3D centerRed = new Point3D(0, 0, -10);
		final Point3D centerBlue = new Point3D(0, 0, -20);
		
		final Sphere red = new Sphere(centerRed, SPHERE_RADIUS, ColorRGB.RED);
		final Sphere blue = new Sphere(centerBlue, SPHERE_RADIUS, ColorRGB.BLUE);
		
		Scene scene = new Scene().add(blue, red);
		
		Assert.assertEquals(TRACER.trace(scene, JAY), ColorRGB.RED);
		
		scene = new Scene().add(red, blue);
		
		Assert.assertEquals(TRACER.trace(scene, JAY), ColorRGB.RED);
	}
}
