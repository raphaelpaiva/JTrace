package org.jtrace.shader;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;
import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DiffuseShaderUnitTest {

	private static final Light LIGHT_GREATER_90DEGREES = new Light(0, 10, -20);
	private static final Light LIGHT_90DEGREES = new Light(0, 10, -8);
	private static final Light LIGHT_45DEGREES = new Light(0, 8, 0);
	private static final Light LIGHT_0DEGREES = new Light(0, 0, 15);
	private static Point3D CENTER = new Point3D(0, 0, -10);
	private static float RADIUS = 2.0f;
	private static ReflectanceCoefficient K_AMBIENT = new ReflectanceCoefficient(0.2, 0.2, 0.2);
	private static ReflectanceCoefficient K_DIFFUSE = new ReflectanceCoefficient(0.3, 0.3, 0.3);
	private static Material MATERIAL = new Material(ColorRGB.BLUE, K_AMBIENT, K_DIFFUSE);
	
	private static Sphere SPHERE = new Sphere(CENTER, RADIUS, MATERIAL);
	
	private static Vector3D DIRECTION = new Vector3D(0, 0, -1);
	private static Point3D ORIGIN = new Point3D(0, 0, 0);
	
	private static Jay JAY = new Jay(ORIGIN, DIRECTION);
	
	private static DiffuseShader DIFFUSE_SHADER = new DiffuseShader();
	
	@Test
	public void testCalculusDiffuseContribution_0Degrees() {
		Hit hit = SPHERE.hit(JAY);
		
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_0DEGREES, hit, JAY);
		double expectedDiffuseContribution = 1.0f;
		
		Assert.assertEquals(expectedDiffuseContribution, diffuseCotribution);
	}
	
	@Test
	public void testCalculusDiffuseContribution_45Degrees() {
		Hit hit = SPHERE.hit(JAY);
		
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_45DEGREES, hit, JAY);
		double expectedDiffuseContribution = 1.0 / Math.sqrt(2.0);
		
		Assert.assertEquals(expectedDiffuseContribution, diffuseCotribution, 0.00000001);
	}
	
	@Test
	public void testCalculusDiffuseContribution_90Degrees() {
		Hit hit = SPHERE.hit(JAY);
		
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_90DEGREES, hit, JAY);
		double expectedDiffuseContribution = 0.0f;
		
		Assert.assertEquals(expectedDiffuseContribution, diffuseCotribution);
	}
	
	@Test
	public void testCalculusDiffuseContribution_Greater_90Degrees() {
		Hit hit = SPHERE.hit(JAY);
		
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_GREATER_90DEGREES, hit, JAY);
		double expectedDiffuseContribution = 0.0f;
		
		Assert.assertEquals(expectedDiffuseContribution, diffuseCotribution);
	}
	
	@Test
	public void testDiffuse_Light_Jay_0Degrees() {
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_0DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, DIFFUSE_SHADER.shade(LIGHT_0DEGREES, hit, JAY, SPHERE));
	}
	
	@Test
	public void testDiffuse_Light_Jay_45Degrees() {
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_45DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, DIFFUSE_SHADER.shade(LIGHT_45DEGREES, hit, JAY, SPHERE));
	}
	
	@Test
	public void testDiffuse_Light_Jay_90Degrees() {
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_90DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, DIFFUSE_SHADER.shade(LIGHT_90DEGREES, hit, JAY, SPHERE));
	}
	
	@Test
	public void testDiffuse_Light_Jay_Greater_90Degrees() {
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_GREATER_90DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, DIFFUSE_SHADER.shade(LIGHT_GREATER_90DEGREES, hit, JAY, SPHERE));
	}
	
}
