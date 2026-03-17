package org.jtrace.geometry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jtrace.Jay;
import org.jtrace.Section;
import org.jtrace.material.Material;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.tracer.Hit;
import org.jtrace.tracer.NotHit;

import java.util.List;

public class Disk extends GeometricObject {
  private final double radius;
  private Point3D center;
  private Vector3D normal;

  private Material material;

  private Plane internalPlane;
  private double radiusSquared;

  @JsonCreator
  public Disk(
      @JsonProperty("center") Point3D center,
      @JsonProperty("normal") Vector3D normal,
      @JsonProperty("radius") double radius,
      @JsonProperty("material") Material material) {
    super(material);
    this.center = center;
    this.normal = normal.normal();
    this.radius = radius;
    this.radiusSquared = radius * radius;
    this.internalPlane = new Plane(this.center, this.normal, material);
  }

  @Override
  public Hit hit(Jay jay) {
    Hit planeHit = internalPlane.hit(jay);

    if (planeHit.isHit()) {
      Point3D hitPoint = planeHit.getPoint();
      Vector3D toHitPoint = new Vector3D(center, hitPoint);
      double squaredDistanceFromCenter = toHitPoint.dot();

      if (squaredDistanceFromCenter <= radiusSquared) {
        return planeHit;
      }
    }

    return new NotHit();
  }

  @Override
  public List<Section> sections(Jay jay) {
    return List.of();
  }
}
