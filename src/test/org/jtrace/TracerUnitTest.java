package org.jtrace;

import junit.framework.Assert;

import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.testng.annotations.Test;

public class TracerUnitTest {
	
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
	
	Tracer PERSPECTIVE_TRACER = new Tracer();
	
	@Test
	public void testAmbientLight_On() {
		Scene scene = new Scene().add(SPHERE);
		
		double red = ColorRGB.BLUE.getRed() * K_AMBIENT.getRed();
		double green = ColorRGB.BLUE.getGreen() * K_AMBIENT.getGreen();
		double blue = ColorRGB.BLUE.getBlue() * K_AMBIENT.getBlue();
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, PERSPECTIVE_TRACER.cast(scene, JAY));
	}
	
	@Test
	public void testAmbientLight_Off() {
		Scene scene = new Scene().add(SPHERE).turnOffAmbientLight();
		
		Assert.assertEquals(ColorRGB.BLACK, PERSPECTIVE_TRACER.cast(scene, JAY));
	}
	
	@Test
	public void testHitPoint() {
		Hit hit = SPHERE.hit(JAY);
		Point3D hitPoint = PERSPECTIVE_TRACER.calculateHitPoint(JAY, hit);
		
		Point3D expectedPoint = new Point3D(0, 0, -8);
		
		Assert.assertEquals(expectedPoint, hitPoint);
	}
	
	@Test
	public void testCalculusDiffuseContribution_0Degrees() {
		Hit hit = SPHERE.hit(JAY);
		
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_0DEGREES, hit, JAY);
		double expectedDiffuseContribution = 1.0f;
		
		Assert.assertEquals(expectedDiffuseContribution, diffuseCotribution);
	}
	
	@Test
	public void testCalculusDiffuseContribution_45Degrees() {
		Hit hit = SPHERE.hit(JAY);
		
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_45DEGREES, hit, JAY);
		double expectedDiffuseContribution = 1.0 / Math.sqrt(2.0);
		
		Assert.assertEquals(expectedDiffuseContribution, diffuseCotribution, 0.00000001);
	}
	
	@Test
	public void testCalculusDiffuseContribution_90Degrees() {
		Hit hit = SPHERE.hit(JAY);
		
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_90DEGREES, hit, JAY);
		double expectedDiffuseContribution = 0.0f;
		
		Assert.assertEquals(expectedDiffuseContribution, diffuseCotribution);
	}
	
	@Test
	public void testCalculusDiffuseContribution_Greater_90Degrees() {
		Hit hit = SPHERE.hit(JAY);
		
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_GREATER_90DEGREES, hit, JAY);
		double expectedDiffuseContribution = 0.0f;
		
		Assert.assertEquals(expectedDiffuseContribution, diffuseCotribution);
	}
	
	@Test
	public void testDiffuse_Light_Jay_0Degrees_With_AmbientLight_On() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_0DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_0DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_AMBIENT.getRed();
		double green = ColorRGB.BLUE.getGreen() * K_AMBIENT.getGreen();
		double blue = ColorRGB.BLUE.getBlue() * K_AMBIENT.getBlue();
		
		red += ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		green += ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		blue += ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, PERSPECTIVE_TRACER.cast(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_45Degrees_With_AmbientLight_On() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_45DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_45DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_AMBIENT.getRed();
		double green = ColorRGB.BLUE.getGreen() * K_AMBIENT.getGreen();
		double blue = ColorRGB.BLUE.getBlue() * K_AMBIENT.getBlue();
		
		red += ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		green += ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		blue += ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, PERSPECTIVE_TRACER.cast(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_90Degrees_With_AmbientLight_On() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_90DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_90DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_AMBIENT.getRed();
		double green = ColorRGB.BLUE.getGreen() * K_AMBIENT.getGreen();
		double blue = ColorRGB.BLUE.getBlue() * K_AMBIENT.getBlue();
		
		red += ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		green += ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		blue += ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, PERSPECTIVE_TRACER.cast(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_Greater_90Degrees_With_AmbientLight_On() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_GREATER_90DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_GREATER_90DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_AMBIENT.getRed();
		double green = ColorRGB.BLUE.getGreen() * K_AMBIENT.getGreen();
		double blue = ColorRGB.BLUE.getBlue() * K_AMBIENT.getBlue();
		
		red += ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		green += ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		blue += ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, PERSPECTIVE_TRACER.cast(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_0Degrees_With_AmbientLight_Off() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_0DEGREES).turnOffAmbientLight();
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_0DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, PERSPECTIVE_TRACER.cast(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_45Degrees_With_AmbientLight_Off() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_45DEGREES).turnOffAmbientLight();
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_45DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, PERSPECTIVE_TRACER.cast(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_90Degrees_With_AmbientLight_Off() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_90DEGREES).turnOffAmbientLight();
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_90DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, PERSPECTIVE_TRACER.cast(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_Greater_90Degrees_With_AmbientLight_Off() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_GREATER_90DEGREES).turnOffAmbientLight();
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = PERSPECTIVE_TRACER.calculateDiffuseContribution(LIGHT_GREATER_90DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Assert.assertEquals(expectedColor, PERSPECTIVE_TRACER.cast(scene, JAY));
	}
}
