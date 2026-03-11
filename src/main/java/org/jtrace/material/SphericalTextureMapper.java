package org.jtrace.material;

import org.jtrace.Hit;
import org.jtrace.primitives.Vector3D;

public class SphericalTextureMapper implements TextureMapper{

  public UVMapping map(Hit hit) {
    Vector3D pole = Vector3D.UNIT_Y;
    Vector3D equator = Vector3D.UNIT_X;

    Vector3D normal = hit.getNormal();

    double normalDotPole = normal.dot(pole);
    double phi = Math.acos(normalDotPole);

    double v = phi / Math.PI;

    Vector3D poleCrossEquator = pole.cross(equator);

    double equatorDotNormal = equator.dot(normal);

    double theta = Math.acos(equatorDotNormal / Math.sin(phi)) / (2 * Math.PI);

    double u;

    if (poleCrossEquator.dot(normal) > 0) {
      u = theta;
    } else {
      u = 1 - theta;
    }

    return new UVMapping(u, v);
  }
}

