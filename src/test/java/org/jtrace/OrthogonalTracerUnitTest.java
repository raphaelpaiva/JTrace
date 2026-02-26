package org.jtrace;

import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.lights.PointLight;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.AmbientShader;
import org.jtrace.shader.DiffuseShader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OrthogonalTracerUnitTest {
	
	private static final Tracer TRACER = prepareTracer();
	private static final Vector3D JAY_DIRECTION = new Vector3D(0, 0, -1);
	private static final Point3D JAY_ORIGIN = new Point3D(0, 1, 0);
	private static final Jay JAY = new Jay(JAY_ORIGIN, JAY_DIRECTION);
	private static final int SPHERE_RADIUS = 1;
	private static final ReflectanceCoefficient KAMBIENT = new ReflectanceCoefficient(1.0, 1.0, 1.0);
	private static final ReflectanceCoefficient KDIFFUSE = new ReflectanceCoefficient(1.0, 1.0, 1.0);
	private static final Light LIGHT = new PointLight(new Point3D(0, 0, 50));
	
	@Test
	public void testRender_RedSphereInFrontOfViewPlane() {
		final Point3D center = new Point3D(0, 0, -5);
		final Material material = new Material(ColorRGB.RED, KAMBIENT, KDIFFUSE);
		final Sphere sphere = new Sphere(center, SPHERE_RADIUS, material);
		
		Scene scene = new Scene().add(sphere).add(LIGHT);

		Assert.assertEquals(TRACER.trace(scene, JAY), ColorRGB.RED);
	}
	
	private static Tracer prepareTracer() {
		Tracer tracer = new Tracer();
		
		tracer.addShaders(new AmbientShader(), new DiffuseShader());
		return tracer;
	}

	@Test
	public void testRender_RedSphereBehindViewPlane() {
		final Point3D center = new Point3D(0, 0, 50);
		final Material material = new Material(ColorRGB.RED, KAMBIENT, KDIFFUSE);		
		final Sphere sphere = new Sphere(center, SPHERE_RADIUS, material);
		
		Scene scene = new Scene().add(sphere).withBackground(ColorRGB.GREEN);
		
		Assert.assertEquals(TRACER.trace(scene, JAY), ColorRGB.GREEN);
	}
	
	@Test
	public void testRender_TwoSpheresRedInFront() {
		final Point3D centerRed = new Point3D(0, 0, -10);
		final Point3D centerBlue = new Point3D(0, 0, -20);
		
		final Material blueMaterial = new Material(ColorRGB.BLUE, KAMBIENT, KDIFFUSE);
		final Material redMaterial = new Material(ColorRGB.RED, KAMBIENT, KDIFFUSE);
		
		final Sphere red = new Sphere(centerRed, SPHERE_RADIUS, redMaterial);
		final Sphere blue = new Sphere(centerBlue, SPHERE_RADIUS, blueMaterial);
		
		Scene scene = new Scene().add(blue, red).add(LIGHT);
		
		Assert.assertEquals(TRACER.trace(scene, JAY), ColorRGB.RED);
		
		scene = new Scene().add(red, blue).add(LIGHT);
		
		Assert.assertEquals(TRACER.trace(scene, JAY), ColorRGB.RED);
	}
	
	@Test
	public void testRender_PlaneInFrontOfViewPlane() {
		final Point3D point = new Point3D(0, 0, -5);
		final Vector3D normal = new Vector3D(0, 0, 1);
		
		Material material = new Material(ColorRGB.YELLOW, KAMBIENT, KDIFFUSE);
		
		final Plane plane = new Plane(point, normal, material);
		
		Scene scene = new Scene().add(plane).add(LIGHT);

		Assert.assertEquals(TRACER.trace(scene, JAY), ColorRGB.YELLOW);
	}
	
	@Test
	public void testRender_PlaneBehindViewPlane() {
		final Point3D point = new Point3D(0, 0, 5);
		final Vector3D normal = new Vector3D(0, 0, 1);
		
		final Material material = new Material(ColorRGB.YELLOW, KAMBIENT, KDIFFUSE);
		
		final Plane plane = new Plane(point, normal, material);
		
		Scene scene = new Scene().add(plane);

		Assert.assertEquals(TRACER.trace(scene, JAY), ColorRGB.BLACK);
	}
}
