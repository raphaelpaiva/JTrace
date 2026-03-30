package org.jtrace.geometry;

import org.jtrace.Jay;
import org.jtrace.Section;
import org.jtrace.material.Material;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;


public class Axis3D extends CompositeGeometricObject {
  public Axis3D() {
    double DEFAULT_LENGTH    = 20d;
    double DEFAULT_THICKNESS = .25d;

    Vector xAxis = new Vector(Vector3D.UNIT_X, Point3D.ORIGIN, DEFAULT_THICKNESS, DEFAULT_LENGTH, material(ColorRGB.RED));
    Vector yAxis = new Vector(Vector3D.UNIT_Y, Point3D.ORIGIN, DEFAULT_THICKNESS, DEFAULT_LENGTH, material(ColorRGB.GREEN));
    Vector zAxis = new Vector(Vector3D.UNIT_Z, Point3D.ORIGIN, DEFAULT_THICKNESS, DEFAULT_LENGTH, material(ColorRGB.BLUE));
    super(xAxis, yAxis, zAxis);
  }

  private static Material material(ColorRGB color) {
    return new Material(
        color,
        new ReflectanceCoefficient(.5),
        new ReflectanceCoefficient(1)
    );
  }
}
