package org.jtrace.primitives;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ColorRGBUnitTest {
	
	@Test
	public void testToInt_Red() {
		ColorRGB c = new ColorRGB(255, 0, 0);
		
		Assert.assertEquals(c.toInt(), 0xFF0000);
	}
	
	@Test
	public void testToInt_Yellow() {
		ColorRGB c = new ColorRGB(255, 255, 0);
		
		Assert.assertEquals(c.toInt(), 0xFFFF00);
	}
	
	@Test
	public void testToInt_White() {
		ColorRGB c = new ColorRGB(255, 255, 255);
		
		Assert.assertEquals(c.toInt(), 0xFFFFFF);
	}
}
