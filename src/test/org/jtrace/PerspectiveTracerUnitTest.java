package org.jtrace;

import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Sphere;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PerspectiveTracerUnitTest {
	
	private static final Point3D EYE_POINT = new Point3D(0, 0, 10);
	private static final double VIEW_DISTANCE = 10.0;
	private static final PerspectiveTracer TRACER = new PerspectiveTracer(EYE_POINT, VIEW_DISTANCE);
	
	private static final int SPHERE_RADIUS = 1;
	
	@Test
	public void testCalculateJayDirection_CenterJay() {
		final int hres = 1;
		final int vres = 1;
		final double pixelSize = 0.5; 
		
		Vector3D jayDirection = TRACER.calculateJayDirection(hres, vres, pixelSize, 0, 0);
		Vector3D expectedJay = new Vector3D(0, 0, -1);
		
		Assert.assertEquals(jayDirection, expectedJay);
	}
	
	@Test
	public void testCalculateJayDirection_TopLeftJay() {
		final int hres = 10;
		final int vres = 10;
		final double pixelSize = 1; 
		
		Vector3D jayDirection = TRACER.calculateJayDirection(hres, vres, pixelSize, 9, 0);
		Vector3D expectedJay = new Vector3D(-4.5, 4.5, -10).normal();
		
		Assert.assertEquals(jayDirection.getCoordinate().getX(), -0.379642, 0.000001);
		Assert.assertEquals(jayDirection.getCoordinate().getY(), 0.379642, 0.000001);
		Assert.assertEquals(jayDirection.getCoordinate().getZ(), -0.843649, 0.000001);

		Assert.assertEquals(jayDirection, expectedJay);
	}
	
	@Test
	public void testRender_RedSphereInFrontOfViewPlane() {
		final Point3D center = new Point3D(0, 0, -5);
		final Sphere sphere = new Sphere(center, SPHERE_RADIUS, ColorRGB.RED);
		final int hres = 1;
		final int vres = 1;
		final double pixelSize = 0.5; 
		
		Scene scene = new Scene().add(sphere);

		Vector3D jayDirection = TRACER.calculateJayDirection(hres, vres, pixelSize, 0, 0);
		Jay jay = new Jay(EYE_POINT, jayDirection);
		
		Assert.assertEquals(TRACER.cast(scene, jay), ColorRGB.RED);
	}
	
	/*
	@Test
	public void testRender_RedSphereBehindViewPlane() {
		final Point3D center = new Point3D(0, 0, 50);
		final Sphere sphere = new Sphere(center, SPHERE_RADIUS, ColorRGB.RED);
		
		Scene scene = new Scene().add(sphere).withBackground(ColorRGB.GREEN);
		
		Assert.assertEquals(TRACER.cast(scene, JAY), ColorRGB.GREEN);
	}
	
	@Test
	public void testRender_TwoSpheresRedInFront() {
		final Point3D centerRed = new Point3D(0, 0, -10);
		final Point3D centerBlue = new Point3D(0, 0, -20);
		
		final Sphere red = new Sphere(centerRed, SPHERE_RADIUS, ColorRGB.RED);
		final Sphere blue = new Sphere(centerBlue, SPHERE_RADIUS, ColorRGB.BLUE);
		
		Scene scene = new Scene().add(blue, red);
		
		Assert.assertEquals(TRACER.cast(scene, JAY), ColorRGB.RED);
		
		scene = new Scene().add(red, blue);
		
		Assert.assertEquals(TRACER.cast(scene, JAY), ColorRGB.RED);
	}
	
	@Test
	public void testRender_PlaneInFrontOfViewPlane() {
		final Point3D point = new Point3D(0, 0, -5);
		final Vector3D normal = new Vector3D(0, 0, 1);
		
		final Plane plane = new Plane(point, normal, ColorRGB.YELLOW);
		
		Scene scene = new Scene().add(plane);

		Assert.assertEquals(TRACER.cast(scene, JAY), ColorRGB.YELLOW);
	}
	
	@Test
	public void testRender_PlaneBehindViewPlane() {
		final Point3D point = new Point3D(0, 0, 5);
		final Vector3D normal = new Vector3D(0, 0, 1);
		
		final Plane plane = new Plane(point, normal, ColorRGB.YELLOW);
		
		Scene scene = new Scene().add(plane);

		Assert.assertEquals(TRACER.cast(scene, JAY), ColorRGB.BLACK);
	}
	*/
}
