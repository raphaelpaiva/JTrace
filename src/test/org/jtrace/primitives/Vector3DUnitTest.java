package org.jtrace.primitives;


import org.testng.Assert;
import org.testng.annotations.Test;

public class Vector3DUnitTest {

	private static final double PERPENDICULAR_VECTORS_DOT_PRODUCT_RESULT = 0;
	
	private static final double COLLINEAR_NON_NORMALIZED_VECTORS_DOT_PRODUCT_RESULT = 6;

	@Test
	public void testDotProduct_PerpendicularVectors()
	{
		final Vector3D v1 = new Vector3D(1, 0, 0);
		final Vector3D v2 = new Vector3D(0, 1, 0);

		Assert.assertEquals(v1.dot(v2), PERPENDICULAR_VECTORS_DOT_PRODUCT_RESULT);
	}

	@Test
	public void testDotProduct_CollinearVectors()
	{
		final Vector3D v1 = new Vector3D(1, 1, 1);
		final Vector3D v2 = new Vector3D(2, 2, 2);

		Assert.assertEquals(v1.dot(v2), COLLINEAR_NON_NORMALIZED_VECTORS_DOT_PRODUCT_RESULT);
	}
	
}
