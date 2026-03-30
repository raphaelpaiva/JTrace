package org.jtrace.geometry;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jtrace.Jay;
import org.jtrace.Section;
import org.jtrace.material.Material;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.tracer.Hit;
import org.jtrace.tracer.NotHit;

import java.util.List;

public class Vector extends GeometricObject {

  public static final double DEFAULT_RADIUS = 0.25;
  private Cylinder body;
  private Cone tip;

  public Vector(
      @JsonProperty("vector") Vector3D vector,
      @JsonProperty(value = "origin") Point3D origin,
      @JsonProperty(value = "radius") Double radius,
      @JsonProperty(value = "size") Double size,
      @JsonProperty("material") Material material
  ) {
    super(material);
    if (origin == null) {
      origin = Point3D.ORIGIN;
    }

    if (radius == null) {
      radius = DEFAULT_RADIUS;
    }

    if (size != null) {
      vector = vector.normal().multiply(size);
    } else {
      size = vector.module();
    }

    this.body = new Cylinder(origin, vector, radius, size, material);
    this.tip = new Cone(origin.add(vector), vector, radius * 2, radius * 4, material);
  }

  @Override
  public Hit hit(Jay jay) {
    Hit bodyHit = body.hit(jay);
    Hit tipHit = tip.hit(jay);

    if (bodyHit.isHit() && tipHit.isHit()) {
      return bodyHit.getT() < tipHit.getT() ? bodyHit : tipHit;
    } else if (bodyHit.isHit()) {
      return bodyHit;
    } else if (tipHit.isHit()) {
      return tipHit;
    } else {
      return NotHit.INSTANCE;
    }
  }

  @Override
  public List<Section> sections(Jay jay) {
    return List.of();
  }
}
