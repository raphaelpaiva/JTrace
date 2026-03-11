package org.jtrace.material;

import org.jtrace.Hit;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class PlanarTextureMapper implements TextureMapper {
  @Override
  public UVMapping map(Hit hit) {
    var nonParallelToNormal = nonParallelTo(hit.getNormal()).normal();

    var e1 = hit.getNormal().cross(nonParallelToNormal);
    var e2 = hit.getNormal().cross(e1);

    var perpendicular = nonParallelToNormal.cross(hit.getNormal());
    var planePoint = new Point3D(perpendicular.getX(), perpendicular.getY(), perpendicular.getZ());

    var hitPointInPlane = hit.getPoint().subtract(planePoint);

    double u = hitPointInPlane.dot(e1);
    double v = hitPointInPlane.dot(e2);

    return new UVMapping(u, v);
  }

  private Vector3D nonParallelTo(Vector3D vector) {
    if (vector.isParallelTo(Vector3D.UNIT_X)) {
      return Vector3D.UNIT_Y;
    } else {
      return Vector3D.UNIT_X;
    }
  }

  private Vector3D longest(Vector3D a, Vector3D b, Vector3D c) {
    double aModule = a.module();
    double bModule = b.module();
    double cModule = c.module();

    if (aModule >= bModule && aModule >= cModule) {
      return a;
    } else if (bModule >= aModule && bModule >= cModule) {
      return b;
    } else {
      return c;
    }
  }
}
