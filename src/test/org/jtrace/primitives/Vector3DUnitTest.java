package org.jtrace.primitives;


import org.testng.Assert;
import org.testng.annotations.Test;

public class Vector3DUnitTest {

	private static final int PERPENDICULAR_VECTORS_DOT_PRODUCT_RESULT = 0;

	@Test
	public void testDotProduct_PerpendicularVectors()
	{
		final Vector3D v1 = new Vector3D(1, 0, 0);
		final Vector3D v2 = new Vector3D(0, 1, 0);

		System.out.println("HEHEHEH");

		Assert.assertEquals(v1.dot(v2), PERPENDICULAR_VECTORS_DOT_PRODUCT_RESULT);
	}

}
