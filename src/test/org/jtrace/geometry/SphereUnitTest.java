package org.jtrace.geometry;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;
import org.jtrace.NotHit;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SphereUnitTest {
	
	private static final double FRONTAL_JAY_PARAMETER = 4.0;
	private static final double TANGENTIAL_JAY_PARAMETER = 5.0;
	private static final int SPHERE_RADIUS = 1;
	private static final Point3D SPHERE_CENTER = new Point3D(0, 0, -5);
	private static final ReflectanceCoefficient KAMBIENT = new ReflectanceCoefficient(0.2, 0.2, 0.2);
	private static final Material RED_MATERIAL = new Material(ColorRGB.RED, KAMBIENT);
	private static final Sphere SPHERE = new Sphere(SPHERE_CENTER, SPHERE_RADIUS, RED_MATERIAL);
	
	@Test(expectedExceptions={IllegalStateException.class})
	public void testHit_NoHit()
	{
		Point3D  jayOrigin    = new Point3D(0, 0, 0);
		Vector3D jayDirection = new Vector3D(1, 10, 0);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Hit hit = SPHERE.hit(jay);
		
		Assert.assertFalse(hit.isHit(), "Expected no hit!");
		Assert.assertTrue(hit instanceof NotHit, "Expected Hit instanceof NoHit");
		hit.getT();
	}
	
	@Test
	public void testHit_OneHit()
	{
		Point3D  jayOrigin    = new Point3D(0, 1, 0);
		Vector3D jayDirection = new Vector3D(0, 0, -1);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Hit hit = SPHERE.hit(jay);
		
		Assert.assertTrue(hit.isHit(), "Expected one hit!");
		Assert.assertFalse(hit instanceof NotHit, "Expected Hit not to be an instanceof NoHit");
		Assert.assertEquals(hit.getT(), TANGENTIAL_JAY_PARAMETER);
	}
	
	@Test
	public void testHit_TwoHits()
	{
		Point3D  jayOrigin    = new Point3D(0, 0, 0);
		Vector3D jayDirection = new Vector3D(0, 0, -1);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Hit hit = SPHERE.hit(jay);
		
		Assert.assertTrue(hit.isHit(), "Expected two hits!");
		Assert.assertFalse(hit instanceof NotHit, "Expected Hit not to be an instanceof NoHit");
		Assert.assertEquals(hit.getT(), FRONTAL_JAY_PARAMETER);
	}
	
	@Test
	public void testNormal()
	{
		Point3D  jayOrigin    = new Point3D(0, 0, 0);
		Vector3D jayDirection = new Vector3D(0, 0, -1);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Hit hit = SPHERE.hit(jay);
		
		Vector3D normal = new Vector3D(0, 0, 1);
		
		Assert.assertTrue(hit.isHit(), "Expected two hits!");
		Assert.assertFalse(hit instanceof NotHit, "Expected Hit not to be an instanceof NoHit");
		Assert.assertEquals(hit.getT(), FRONTAL_JAY_PARAMETER);
		Assert.assertEquals(hit.getNormal().normal(), normal);
	}
}
