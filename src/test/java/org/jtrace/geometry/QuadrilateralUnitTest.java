package org.jtrace.geometry;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;
import org.jtrace.Materials;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.testng.Assert;
import org.testng.annotations.Test;

public class QuadrilateralUnitTest {
	
	private static final Material MATERIAL = Materials.metallic(ColorRGB.BLUE);

	private static final Point3D P0 = new Point3D(-1, -1, 0);
	private static final Point3D P1 = new Point3D( 1, -1, 0);
	private static final Point3D P2 = new Point3D( 1,  1, 0);
	private static final Point3D P3 = new Point3D(-1,  1, 0);

	private static final Point3D JAY_ORIGIN = new Point3D(0, 0, -5);
	
	private static final Quadrilateral QUADRILATERAL = new Quadrilateral(P0,P1,P2,P3, MATERIAL);
	
	@Test
	public void testHit_FrontalJay_ShouldHit() {

		Vector3D expectedNormal = Vector3D.UNIT_Z;
		
		Jay jay = new Jay(JAY_ORIGIN, Vector3D.UNIT_Z);
		
		Hit hit = QUADRILATERAL.hit(jay);
		
		Assert.assertTrue(hit.isHit(), "Expected to hit the Quadrilateral.");
		Assert.assertEquals(hit.getT(), 5.0);
		Assert.assertEquals(hit.getNormal(), expectedNormal);
	}
	
	@Test
	public void testHit_FrontalShiftedJay_ShouldHit() {

		Vector3D expectedNormal = Vector3D.UNIT_Z;
		
		Jay jay = new Jay(new Point3D(0.5, 0.5, -5), Vector3D.UNIT_Z);
		
		Hit hit = QUADRILATERAL.hit(jay);
		
		Assert.assertTrue(hit.isHit(), "Expected to hit the Quadrilateral.");
		Assert.assertEquals(hit.getT(), 5.0);
		Assert.assertEquals(hit.getNormal(), expectedNormal);
	}
	
	@Test
	public void testHit_ParallelJay_ShouldNotHit() {
		
		Jay jay = new Jay(JAY_ORIGIN , Vector3D.UNIT_X);
		
		Hit hit = QUADRILATERAL.hit(jay);
		
		Assert.assertFalse(hit.isHit());
	}
}
