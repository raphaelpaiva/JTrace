package org.jtrace.io;

import static org.testng.Assert.assertEquals;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SceneIOUnitTest {

	private SceneIO io = new SceneIO();
	
	@Test
	public void testPoint3DDump() {
		Point3D pt = new Point3D(0, 0, -10);
		
		String ptStr = io.yaml().dump(pt);
		
		assertEquals(ptStr.trim(), "!pt {x: 0.0, y: 0.0, z: -10.0}");
	}
	
	@Test 
	public void testPoint3DLoad() {
		Point3D pt = new Point3D(0, 0, -10);
		
		Object obj = io.yaml().load("!pt {x: 0.0, y: 0.0, z: -10.0}");
		Assert.assertTrue(obj instanceof Point3D);
		
		Point3D ptLoaded = (Point3D) obj;
		Assert.assertEquals(ptLoaded, pt);
	}
	
	@Test
	public void testColorRGBDump() {
		ColorRGB color = new ColorRGB(1.0, 0.0, 0.0);
		
		String colorStr = io.yaml().dump(color);
		
		assertEquals(colorStr.trim(), "!color {blue: 0.0, green: 0.0, red: 1.0}");
	}
	
	@Test 
	public void testColorRGBLoad() {
		ColorRGB color = new ColorRGB(1.0, 0.0, 0.0);
				
		Object obj = io.yaml().load("!color {blue: 0.0, green: 0.0, red: 1.0}");
		Assert.assertTrue(obj instanceof ColorRGB);
		
		ColorRGB colorLoaded = (ColorRGB) obj;
		Assert.assertEquals(colorLoaded, color);
	}
	
	@Test 
	public void testColorRGBLoad_Attributes_Out_of_order() {
		ColorRGB color = new ColorRGB(1.0, 0.0, 0.0);
				
		Object obj = io.yaml().load("!color {red: 1.0, green: 0.0, blue: 0.0}");
		Assert.assertTrue(obj instanceof ColorRGB);
		
		ColorRGB colorLoaded = (ColorRGB) obj;
		Assert.assertEquals(colorLoaded, color);
	}
	
	@Test
	public void testVector3DDump() {
		Vector3D vec = new Vector3D(1.0, 2.0, 1.0);
		
		String vecStr = io.yaml().dump(vec);
		
		assertEquals(vecStr.trim(), "!vector {z: 1.0, y: 2.0, x: 1.0}");
	}
	
	@Test 
	public void testVector3DLoad() {
		Vector3D vec = new Vector3D(1.0, 2.0, 1.0);
				
		Object obj = io.yaml().load("!vector {z: 1.0, y: 2.0, x: 1.0}");
		Assert.assertTrue(obj instanceof Vector3D);
		
		Vector3D vecLoaded = (Vector3D) obj;
		Assert.assertEquals(vecLoaded, vec);
	}
	
}
