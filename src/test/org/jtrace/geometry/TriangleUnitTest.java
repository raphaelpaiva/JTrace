package org.jtrace.geometry;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.NotHit;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TriangleUnitTest {
	
	private static Point3D LEFT_VERTEX = new Point3D(-1, 0, 0);
	private static Point3D RIGHT_VERTEX = new Point3D(1, 0, 0);
	private static Point3D TOP_VERTEX = new Point3D(0, 2, 0);
	
	private static Triangle TRIANGLE = new Triangle(LEFT_VERTEX, RIGHT_VERTEX, TOP_VERTEX);
	
	@Test(expectedExceptions={IllegalStateException.class})
	public void testHit_NoHit_HitsPlane()
	{
		Point3D jayOrigin = new Point3D(0, -1, 15);
		Vector3D jayDirection = new Vector3D(0, 0, -1);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Hit hit = TRIANGLE.hit(jay);
		
		
		Assert.assertFalse(hit.isHit(), "Expected no hit!");
		Assert.assertTrue(hit instanceof NotHit, "Expected Hit instanceof NoHit");
		hit.getT();
	}

	
	@Test(expectedExceptions={IllegalStateException.class})
	public void testHit_NotHit_DontHitPlane()
	{
		Point3D jayOrigin = new Point3D(0, 1, 15);
		Vector3D jayDirection = new Vector3D(0, -1, 0);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Hit hit = TRIANGLE.hit(jay);
		
		
		Assert.assertFalse(hit.isHit(), "Expected no hit!");
		Assert.assertTrue(hit instanceof NotHit, "Expected Hit instanceof NoHit");
		hit.getT();
	}
	
	@Test
	public void testHit_HitTriagle()
	{
		Point3D jayOrigin = new Point3D(0, 1, 15);
		Vector3D jayDirection = new Vector3D(0, 0, -1);
		
		Jay jay = new Jay(jayOrigin, jayDirection);
		
		Hit hit = TRIANGLE.hit(jay);
		
		Assert.assertTrue(hit.isHit(), "Expected one hit!");
		Assert.assertFalse(hit instanceof NotHit, "Expected Hit not to be an instanceof NoHit");
		Assert.assertEquals(hit.getT(), 15.0);
	}
}
