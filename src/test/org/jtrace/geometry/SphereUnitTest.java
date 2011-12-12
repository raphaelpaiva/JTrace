package org.jtrace.geometry;

import org.jtrace.Jay;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SphereUnitTest {
	
	private static final int SPHERE_RADIUS = 1;
	public static final Point3D SPHERE_CENTER = new Point3D(0, 0, -5);
	public static final Sphere SPHERE = new Sphere(SPHERE_CENTER, SPHERE_RADIUS, ColorRGB.RED);
	
	@Test
	public void testHit_NoHit()
	{
		Point3D  jayOrigin    = new Point3D(0, 0, 0);
		Vector3D jayDirection = new Vector3D(1, 10, 0);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Assert.assertFalse(SPHERE.hit(jay), "Expected no hit!");
	}
	
	@Test
	public void testHit_OneHit()
	{
		Point3D  jayOrigin    = new Point3D(0, 1, 0);
		Vector3D jayDirection = new Vector3D(0, 0, -1);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Assert.assertTrue(SPHERE.hit(jay), "Expected one hit!");
	}
	
	@Test
	public void testHit_TwoHits()
	{
		Point3D  jayOrigin    = new Point3D(0, 0, 0);
		Vector3D jayDirection = new Vector3D(0, 0, -1);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Assert.assertTrue(SPHERE.hit(jay), "Expected two hits!");
	}

}
