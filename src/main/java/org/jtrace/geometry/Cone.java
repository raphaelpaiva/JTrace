package org.jtrace.geometry;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jtrace.material.Material;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class Cone extends CylinderFrustrum {

  public Cone(
      @JsonProperty("base") Point3D base,
      @JsonProperty("axis") Vector3D axis,
      @JsonProperty("radius") double radius,
      @JsonProperty("height") double height,
      @JsonProperty("material") Material material) {
    super(base, axis, radius, 0.0, height, material);
  }
}
