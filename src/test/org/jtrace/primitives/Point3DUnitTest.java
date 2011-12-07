package org.jtrace.primitives;

import org.testng.Assert;
import org.testng.annotations.Test;

public class Point3DUnitTest {

	@Test
	public void testSubtract_TrivialCase()
	{
		Point3D a = new Point3D(0, 0, 0);
		Point3D b = new Point3D(2, 2, 2);
		
		Assert.assertEquals(b.subtract(a), new Point3D(2, 2, 2));
	}
	
	@Test
	public void testSubtract_NonTrivialCase()
	{
		Point3D a = new Point3D(1, 2, 3);
		Point3D b = new Point3D(4, 5, 6);
		
		Assert.assertEquals(b.subtract(a), new Point3D(3, 3, 3));
	}
}
