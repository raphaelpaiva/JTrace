package org.jtrace.primitives;

import org.testng.Assert;
import org.testng.annotations.Test;

public class Vector3DUnitTest {

	private static final double MODULE_NON_ORIGIN_VECTOR = Math.sqrt(12);

	private static final double MODULE_ORIGIN_VECTOR = 0;

	private static final double PERPENDICULAR_VECTORS_DOT_PRODUCT_RESULT = 0;

	private static final double COLLINEAR_NON_NORMALIZED_VECTORS_DOT_PRODUCT_RESULT = 6;

	@Test
	public void testDotProduct_PerpendicularVectors() {
		final Vector3D v1 = new Vector3D(1, 0, 0);
		final Vector3D v2 = new Vector3D(0, 1, 0);

		Assert.assertEquals(v1.dot(v2),
				PERPENDICULAR_VECTORS_DOT_PRODUCT_RESULT);
	}

	@Test
	public void testDotProduct_CollinearVectors() {
		final Vector3D v1 = new Vector3D(1, 1, 1);
		final Vector3D v2 = new Vector3D(2, 2, 2);

		Assert.assertEquals(v1.dot(v2),
				COLLINEAR_NON_NORMALIZED_VECTORS_DOT_PRODUCT_RESULT);
	}

	@Test
	public void testModule_Vector_NonOrigin() {
		final Vector3D v = new Vector3D(2, 2, 2);

		Assert.assertEquals(v.module(), MODULE_NON_ORIGIN_VECTOR);
	}

	@Test
	public void testModule_Vector_Origin() {
		final Vector3D v = new Vector3D(0, 0, 0);

		Assert.assertEquals(v.module(), MODULE_ORIGIN_VECTOR);
	}

	@Test
	public void testNormalize_UnitVector() {
		final Vector3D v = new Vector3D(1, 0, 0);
		final Vector3D normalizedV = v.normal();

		final Vector3D expected = new Vector3D(1, 0, 0);

		Assert.assertEquals(normalizedV, expected);
	}

	@Test
	public void testNormalize_NotUnitVector() {
		final Vector3D v = new Vector3D(2, 3, 4);
		final Vector3D normalizedV = v.normal();

		final double module = Math.sqrt(29);
		final Vector3D expected = new Vector3D(2 / module, 3 / module,
				4 / module);

		Assert.assertEquals(normalizedV, expected);
	}

	@Test
	public void testTwoPointsConstructor_trivialCase() {
		Point3D a = new Point3D(0, 0, 0);
		Point3D b = new Point3D(2, 2, 2);

		Assert.assertEquals(new Vector3D(a, b), new Vector3D(2, 2, 2));
	}

	@Test
	public void testMultiply_By2() {
		Vector3D v = new Vector3D(1, 1, 1);
		Vector3D expected = new Vector3D(2, 2, 2);

		Assert.assertEquals(v.multiply(2), expected);
	}

	@Test
	public void testCross_byNilVector() {
		Vector3D base = new Vector3D(1, 1, 1);
		Vector3D nil = new Vector3D(0, 0, 0);

		Assert.assertEquals(base.cross(nil), nil);
	}

	@Test
	public void testCross_sameVector() {
		Vector3D vector = new Vector3D(2, 2, 2);
		Vector3D expected = new Vector3D(0, 0, 0);

		Assert.assertEquals(vector.cross(vector), expected);
	}

	@Test
	public void testCross_differentVectors() {
		Vector3D vector = new Vector3D(1, 2, 3);
		Vector3D otherVector = new Vector3D(4, 5, 6);
		Vector3D expected = new Vector3D(-3, 6, -3);

		Assert.assertEquals(vector.cross(otherVector), expected);
	}

	@Test
	public void testAdd_CollinearVectors() {
		final Vector3D v1 = new Vector3D(1, 1, 1);
		final Vector3D v2 = new Vector3D(2, 2, 2);

		final Vector3D expected = new Vector3D(3, 3, 3);

		Assert.assertEquals(v1.add(v2), expected);
	}

	@Test
	public void testAdd_PerpendicularVectors() {
		final Vector3D v1 = new Vector3D(1, 0, 0);
		final Vector3D v2 = new Vector3D(0, 1, 0);

		final Vector3D expected = new Vector3D(1, 1, 0);

		Assert.assertEquals(v1.add(v2), expected);
	}

	@Test
	public void testSubtract_CollinearVectors() {
		final Vector3D v1 = new Vector3D(2, 2, 2);
		final Vector3D v2 = new Vector3D(3, 3, 3);

		final Vector3D expected = new Vector3D(1, 1, 1);

		Assert.assertEquals(v2.subtract(v1), expected);
	}

	@Test
	public void testSubtract_PerpendicularVectors() {
		final Vector3D v1 = new Vector3D(1, 0, 0);
		final Vector3D v2 = new Vector3D(0, 1, 0);

		final Vector3D expected = new Vector3D(1, -1, 0);

		Assert.assertEquals(v1.subtract(v2), expected);
	}

	@Test
	public void testDivide_By2() {
		Vector3D v = new Vector3D(4, 4, 4);
		Vector3D expected = new Vector3D(2, 2, 2);

		Assert.assertEquals(v.divide(2), expected);
	}

	@Test
	public void testIsParalell_antiParallelVectors() {
		Vector3D vector = new Vector3D(0, 1, 0);
		Vector3D otherVector = new Vector3D(0, -42, 0);

		Assert.assertTrue(vector.isParallelTo(otherVector),
				"Expected vectors to be parallel!");
	}

	@Test
	public void testIsParalell_parallelVectors() {
		Vector3D vector = new Vector3D(0, 1, 0);
		Vector3D otherVector = new Vector3D(0, 42, 0);

		Assert.assertTrue(vector.isParallelTo(otherVector),
				"Expected vectors to be parallel!");
	}

	@Test
	public void testIsParalell_notParallelVectors() {
		Vector3D vector = new Vector3D(0, 1, 0);
		Vector3D otherVector = new Vector3D(1, 1, 0);

		Assert.assertFalse(vector.isParallelTo(otherVector),
				"Expected vectors not to be parallel!");
	}
	
	@Test
	public void testAngleBetween_0Degrees() {
		double theta = Vector3D.UNIT_X.angleBetween(Vector3D.UNIT_X);
		
		double expectedTheta = 0;
		
		Assert.assertEquals(theta, expectedTheta);
	}
	
	@Test
	public void testAngleBetween_90Degrees() {
		double theta = Vector3D.UNIT_X.angleBetween(Vector3D.UNIT_Y);
		
		double expectedTheta = Math.PI / 2;
		
		Assert.assertEquals(theta, expectedTheta);
	}
	
	@Test
	public void testAngleBetween_45Degrees() {
		double theta = Vector3D.UNIT_X.angleBetween( new Vector3D(1, 0, 1) );
		
		double expectedTheta = Math.PI / 4;
		
		Assert.assertEquals(theta, expectedTheta, 0.000000000000001);
	}


	@Test
	public void testAngleBetween() {
		double theta = new Vector3D(1, 5, 3).angleBetween( new Vector3D(4, 1, 2) );
		
		double expectedTheta = Math.acos( Math.sqrt(15) / 7 );
		
		Assert.assertEquals(theta, expectedTheta);
	}
	
}
