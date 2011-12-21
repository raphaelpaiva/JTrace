package org.jtrace.primitives;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ColorRGBUnitTest {
	
	@Test
	public void testToInt_Red() {
		ColorRGB c = new ColorRGB(1.0, 0.0, 0.0);
		
		Assert.assertEquals(c.toInt(), 0xFF0000);
	}
	
	@Test
	public void testToInt_Yellow() {
		ColorRGB c = new ColorRGB(1.0, 1.0, 0.0);
		
		Assert.assertEquals(c.toInt(), 0xFFFF00);
	}
	
	@Test
	public void testToInt_White() {
		ColorRGB c = new ColorRGB(1.0, 1.0, 1.0);
		
		Assert.assertEquals(c.toInt(), 0xFFFFFF);
	}
	
	@Test
	public void testAdd_ResultLessThan255_GreaterThan0() {
		ColorRGB c = new ColorRGB(0.3, 0.3, 0.3);
		ColorRGB c2 = new ColorRGB(0.3, 0.3, 0.3);
		
		ColorRGB expected = new ColorRGB(0.6, 0.6, 0.6);
		
		Assert.assertEquals(c.add(c2), expected);
	}
	
	@Test
	public void testAdd_ResultGreaterThan255() {
		ColorRGB c = new ColorRGB(1.0, 1.0, 1.0);
		ColorRGB c2 = new ColorRGB(0.3, 0.3, 0.3);
		
		ColorRGB expected = new ColorRGB(1.0, 1.0, 1.0);
		
		Assert.assertEquals(c.add(c2), expected);
	}
	
	@Test
	public void testConstructor_ResultLesserThanZero() {
		ColorRGB c = new ColorRGB(-1.0, -1.0, -1.0);
		
		ColorRGB expected = new ColorRGB(0.0, 0.0, 0.0);
		
		Assert.assertEquals(c, expected);
	}
}