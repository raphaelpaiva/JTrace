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
	public void testMultiply_ResultLessThan255_GreaterThan0() {
		float multiplier = 0.3f;
		ColorRGB c = new ColorRGB(0.3, 0.3, 0.3).multiply(multiplier);
		
		ColorRGB expected = new ColorRGB(0.09, 0.09, 0.09);
		
		Assert.assertEquals(c.getRed(), expected.getRed(), 0.000001);
		Assert.assertEquals(c.getGreen(), expected.getGreen(), 0.000001);
		Assert.assertEquals(c.getBlue(), expected.getBlue(), 0.000001);
	}
	
	@Test
	public void testMultiply_ResultGreaterThan255() {
		ColorRGB c = new ColorRGB(0.5, 0.5, 0.5);
		float multiplier = 2.0f;
		
		ColorRGB expected = new ColorRGB(1.0, 1.0, 1.0);
		
		Assert.assertEquals(c.multiply(multiplier), expected);
	}
	
	@Test
	public void testConstructor_ResultLesserThanZero() {
		ColorRGB c = new ColorRGB(-1.0, -1.0, -1.0);
		
		ColorRGB expected = new ColorRGB(0.0, 0.0, 0.0);
		
		Assert.assertEquals(c, expected);
	}
}