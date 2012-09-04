package org.jtrace;

import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.AmbientShader;
import org.jtrace.shader.DiffuseShader;
import org.jtrace.shader.Shader;
import org.testng.Assert;
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
	
	private static Shader AMBIENT_SHADER = new AmbientShader();
	private static DiffuseShader DIFFUSE_SHADER = new DiffuseShader();
	
	@Test
	public void testAmbientLight() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_0DEGREES);
		
		double red = ColorRGB.BLUE.getRed() * K_AMBIENT.getRed();
		double green = ColorRGB.BLUE.getGreen() * K_AMBIENT.getGreen();
		double blue = ColorRGB.BLUE.getBlue() * K_AMBIENT.getBlue();
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Tracer tracer = prepareTracerWithAmbientShader();
		
		Assert.assertEquals(tracer.trace(scene, JAY), expectedColor);
	}

	@Test
	public void testNoAmbientLight() {
		Scene scene = new Scene().add(SPHERE);
		
		Tracer tracer = new Tracer();
		
		Assert.assertEquals(tracer.trace(scene, JAY), ColorRGB.BLACK);
	}

	@Test
	public void testDiffuse_Light_Jay_0Degrees_With_AmbientLight() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_0DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_0DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_AMBIENT.getRed();
		double green = ColorRGB.BLUE.getGreen() * K_AMBIENT.getGreen();
		double blue = ColorRGB.BLUE.getBlue() * K_AMBIENT.getBlue();
		
		red += ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		green += ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		blue += ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Tracer tracer = prepareTracerWithAmbientAndDiffuseShaders();
		
		Assert.assertEquals(tracer.trace(scene, JAY), expectedColor);
	}

	@Test
	public void testDiffuse_Light_Jay_45Degrees_With_AmbientLight() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_45DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_45DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_AMBIENT.getRed();
		double green = ColorRGB.BLUE.getGreen() * K_AMBIENT.getGreen();
		double blue = ColorRGB.BLUE.getBlue() * K_AMBIENT.getBlue();
		
		red += ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		green += ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		blue += ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Tracer tracer = prepareTracerWithAmbientAndDiffuseShaders();
		
		Assert.assertEquals(expectedColor, tracer.trace(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_90Degrees_With_AmbientLight() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_90DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_90DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_AMBIENT.getRed();
		double green = ColorRGB.BLUE.getGreen() * K_AMBIENT.getGreen();
		double blue = ColorRGB.BLUE.getBlue() * K_AMBIENT.getBlue();
		
		red += ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		green += ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		blue += ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Tracer tracer = prepareTracerWithAmbientAndDiffuseShaders();
		
		Assert.assertEquals(expectedColor, tracer.trace(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_Greater_90Degrees_With_AmbientLight_On() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_GREATER_90DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_GREATER_90DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_AMBIENT.getRed();
		double green = ColorRGB.BLUE.getGreen() * K_AMBIENT.getGreen();
		double blue = ColorRGB.BLUE.getBlue() * K_AMBIENT.getBlue();
		
		red += ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		green += ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		blue += ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Tracer tracer = prepareTracerWithAmbientAndDiffuseShaders();
		
		Assert.assertEquals(expectedColor, tracer.trace(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_0Degrees_With_No_AmbientLight() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_0DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_0DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Tracer tracer = prepareTracerWithDiffuseShader();
		
		Assert.assertEquals(expectedColor, tracer.trace(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_45Degrees_With_No_AmbientLight() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_45DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_45DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Tracer tracer = prepareTracerWithDiffuseShader();
		
		Assert.assertEquals(expectedColor, tracer.trace(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_90Degrees_With_No_AmbientLight() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_90DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_90DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Tracer tracer = prepareTracerWithDiffuseShader();
		
		Assert.assertEquals(expectedColor, tracer.trace(scene, JAY));
	}
	
	@Test
	public void testDiffuse_Light_Jay_Greater_90Degrees_With_No_AmbientLight() {
		Scene scene = new Scene().add(SPHERE).add(LIGHT_GREATER_90DEGREES);
		
		Hit hit = SPHERE.hit(JAY);
		double diffuseCotribution = DIFFUSE_SHADER.calculateDiffuseContribution(LIGHT_GREATER_90DEGREES, hit, JAY);
		
		double red = ColorRGB.BLUE.getRed() * K_DIFFUSE.getRed() * diffuseCotribution;
		double green = ColorRGB.BLUE.getGreen() * K_DIFFUSE.getGreen() * diffuseCotribution;
		double blue = ColorRGB.BLUE.getBlue() * K_DIFFUSE.getBlue() * diffuseCotribution;
		
		ColorRGB expectedColor = new ColorRGB(red, green, blue);
		
		Tracer tracer = prepareTracerWithDiffuseShader();
		
		Assert.assertEquals(expectedColor, tracer.trace(scene, JAY));
	}
	
	private Tracer prepareTracerWithAmbientAndDiffuseShaders() {
		Tracer tracer = new Tracer();
		
		tracer.addShaders(AMBIENT_SHADER, DIFFUSE_SHADER);
		return tracer;
	}
	
	private Tracer prepareTracerWithAmbientShader() {
		Tracer tracer = new Tracer();
		
		tracer.addShaders(AMBIENT_SHADER);
		return tracer;
	}
	
	private Tracer prepareTracerWithDiffuseShader() {
		Tracer tracer = new Tracer();
		
		tracer.addShaders(DIFFUSE_SHADER);
		return tracer;
	}
}
