package org.jtrace.geometry;

import org.jtrace.Jay;
import org.jtrace.material.Material;
import org.jtrace.tracer.Hit;
import org.jtrace.tracer.NotHit;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CylinderUnitTest {

  private static final double RADIUS = 2.0;
  private static final double HEIGHT = 10.0;
  private static final Point3D BASE = new Point3D(0, 0, 0);
  private static final Vector3D AXIS = new Vector3D(0, 1, 0);
  private static final ReflectanceCoefficient KAMBIENT = new ReflectanceCoefficient(0.2, 0.2, 0.2);
  private static final ReflectanceCoefficient KDIFFUSE = new ReflectanceCoefficient(1.0, 1.0, 1.0);
  private static final Material RED_MATERIAL = new Material(ColorRGB.RED, KAMBIENT, KDIFFUSE);
  private static final Cylinder CYLINDER = new Cylinder(BASE, AXIS, RADIUS, HEIGHT, RED_MATERIAL);

  @Test
  public void testHit_BodyFromFront() {
    Jay jay = new Jay(new Point3D(0, 5, -10), new Vector3D(0, 0, 1));

    Hit hit = CYLINDER.hit(jay);

    Assert.assertTrue(hit.isHit(), "Expected hit on cylinder body");
    Assert.assertFalse(hit instanceof NotHit, "Expected Hit not to be NotHit");
    Assert.assertTrue(hit.getT() > 0, "Expected positive t value");
  }

  @Test
  public void testHit_BodyNormalPointsOutward() {
    Jay jay = new Jay(new Point3D(0, 5, -10), new Vector3D(0, 0, 1));

    Hit hit = CYLINDER.hit(jay);

    Assert.assertTrue(hit.isHit(), "Expected hit");
    Vector3D normal = hit.getNormal();
    Assert.assertTrue(normal.dot(jay.getDirection()) < 0,
        "Normal should point against ray direction");
  }

  @Test
  public void testHit_UpperCap() {
    Jay jay = new Jay(new Point3D(0, 15, -5), new Vector3D(0, -1, 1).normal());

    Hit hit = CYLINDER.hit(jay);

    Assert.assertTrue(hit.isHit(), "Expected hit on upper cap");
    double hitHeight = new Vector3D(BASE, hit.getPoint()).dot(AXIS);
    Assert.assertTrue(hitHeight >= HEIGHT - 0.1, "Expected hit on upper cap");
  }

  @Test
  public void testHit_LowerCap() {
    Jay jay = new Jay(new Point3D(0, -15, -15), new Vector3D(0, 1, 1).normal());

    Hit hit = CYLINDER.hit(jay);

    Assert.assertTrue(hit.isHit(), "Expected hit on lower cap");
    double hitHeight = new Vector3D(BASE, hit.getPoint()).dot(AXIS);
    Assert.assertTrue(hitHeight <= 0.1, "Expected hit on lower cap");
  }

  @Test
  public void testHit_Miss() {
    Jay jay = new Jay(new Point3D(10, 5, -10), new Vector3D(0, 0, 1));

    Hit hit = CYLINDER.hit(jay);

    Assert.assertFalse(hit.isHit(), "Expected no hit");
  }

  @Test
  public void testHit_MissAbove() {
    Jay jay = new Jay(new Point3D(0, 15, -5), new Vector3D(0, 0, 1));

    Hit hit = CYLINDER.hit(jay);

    Assert.assertFalse(hit.isHit(), "Expected no hit when ray is above cylinder");
  }

  @Test
  public void testHit_MissBelow() {
    Jay jay = new Jay(new Point3D(0, -15, -5), new Vector3D(0, 0, 1));

    Hit hit = CYLINDER.hit(jay);

    Assert.assertFalse(hit.isHit(), "Expected no hit when ray is below cylinder");
  }

  @Test
  public void testHit_BodyCloserThanCap() {
    Jay jay = new Jay(new Point3D(0, 10, -10), new Vector3D(0, -1, 1));

    Hit hit = CYLINDER.hit(jay);

    Assert.assertTrue(hit.isHit(), "Expected hit");
    double hitHeight = new Vector3D(BASE, hit.getPoint()).dot(AXIS);
    Assert.assertTrue(hitHeight > 0 && hitHeight < HEIGHT,
        "Expected hit on body, not caps");
  }

  @Test
  public void testHit_InfiniteCylinder() {
    Cylinder infiniteCyl = new Cylinder(BASE, AXIS, RADIUS, 0.0, RED_MATERIAL);
    Jay jay = new Jay(new Point3D(0, 0, -10), new Vector3D(0, 0, 1));

    Hit hit = infiniteCyl.hit(jay);

    Assert.assertTrue(hit.isHit(), "Expected hit on infinite cylinder");
  }

  @Test
  public void testHit_TangentRay() {
    Jay jay = new Jay(new Point3D(0, 5, -RADIUS), new Vector3D(0, 0, 1));

    Hit hit = CYLINDER.hit(jay);

    Assert.assertTrue(hit.isHit(), "Expected tangent hit");
  }

  @Test
  public void testHit_RayParallelToAxis() {
    Jay jay = new Jay(new Point3D(0, -20, 0), new Vector3D(0, 1, 0));

    Hit hit = CYLINDER.hit(jay);

    Assert.assertTrue(hit.isHit(), "Expected no hit for ray parallel to axis");
  }
}
