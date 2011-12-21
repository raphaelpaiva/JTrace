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

public class PlaneUnitTest {
	
	private static final ReflectanceCoefficient KAMBIENT = new ReflectanceCoefficient(0.2, 0.2, 0.2);
	private static final ReflectanceCoefficient KDIFFUSE = new ReflectanceCoefficient(1.0, 1.0, 1.0);
	private static final Material GREEN_MATERIAL = new Material(ColorRGB.GREEN, KAMBIENT, KDIFFUSE);
	
	@Test(expectedExceptions={IllegalStateException.class})
	public void testHit_NotHit() {
		Point3D planePoint = new Point3D(0, 1, 0);
		Vector3D normal = new Vector3D(planePoint);
		
		Plane plane = new Plane(planePoint, normal, GREEN_MATERIAL);
		
		Point3D jayOrigin = new Point3D(0, 0, 0);
		Vector3D jayDirection = new Vector3D(0, 0, -1);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Hit hit = plane.hit(jay);
		
		Assert.assertFalse(hit.isHit(), "Expected no hit!");
		Assert.assertTrue(hit instanceof NotHit, "Expected Hit instanceof NoHit");
		hit.getT();
	}
	
	@Test
	public void testHit() {
		Point3D planePoint = new Point3D(0, 0, -10);
		Vector3D normal = new Vector3D(0, 0, 1);
		
		Plane plane = new Plane(planePoint, normal, GREEN_MATERIAL);
		
		Point3D jayOrigin = new Point3D(0, 0, 0);
		Vector3D jayDirection = new Vector3D(0, 0, -1);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Hit hit = plane.hit(jay);
		
		Assert.assertTrue(hit.isHit(), "Expected one hit!");
		Assert.assertFalse(hit instanceof NotHit, "Expected Hit not to be an instanceof NoHit");
		Assert.assertEquals(hit.getT(), 10.0);
	}
	
	@Test
	public void testNormal()
	{
		Point3D planePoint = new Point3D(0, 0, -10);
		Vector3D normal = new Vector3D(0, 0, 1);
		
		Plane plane = new Plane(planePoint, normal, GREEN_MATERIAL);
		
		Point3D jayOrigin = new Point3D(0, 0, 0);
		Vector3D jayDirection = new Vector3D(0, 0, -1);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Hit hit = plane.hit(jay);
		
		Vector3D expected = new Vector3D(0, 0, 1);
		
		Assert.assertTrue(hit.isHit(), "Expected one hit!");
		Assert.assertFalse(hit instanceof NotHit, "Expected Hit not to be an instanceof NoHit");
		Assert.assertEquals(hit.getT(), 10.0);
		Assert.assertEquals(hit.getNormal().normal(), expected);
	}
}
