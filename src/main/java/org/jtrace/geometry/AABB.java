package org.jtrace.geometry;

import org.apache.commons.lang3.DoubleRange;
import org.jtrace.Jay;
import org.jtrace.primitives.Point3D;

import java.awt.*;

public class AABB {

  private final DoubleRange[] bounds = new DoubleRange[3];

  public AABB(Point3D a, Point3D b) {
    DoubleRange xBounds = a.getX() <= b.getX() ? DoubleRange.of(a.getX(), b.getX()) : DoubleRange.of(b.getX(), a.getX());
    DoubleRange yBounds = a.getY() <= b.getY() ? DoubleRange.of(a.getY(), b.getY()) : DoubleRange.of(b.getY(), a.getY());
    DoubleRange zBounds = a.getZ() <= b.getZ() ? DoubleRange.of(a.getZ(), b.getZ()) : DoubleRange.of(b.getZ(), a.getZ());

    this.bounds[0] = xBounds;
    this.bounds[1] = yBounds;
    this.bounds[2] = zBounds;
  }

  public boolean hit(Jay jay) {
    // t0 = (min - rayOrigin) / rayDirection
    // t1 = (max - rayOrigin) / rayDirection
    double[] rayOrigin = {jay.getOrigin().getX(), jay.getOrigin().getY(), jay.getOrigin().getZ()};
    double[] rayDirection = {
        jay.getDirection().getX() == 0 ? 1e-8 : jay.getDirection().getX(),
        jay.getDirection().getY() == 0 ? 1e-8 : jay.getDirection().getY(),
        jay.getDirection().getZ()== 0 ? 1e-8 : jay.getDirection().getZ()
    }; // Avoid division by zero


    double[] t0 = new double[3]; // close
    double[] t1 = new double[3]; // far

    for (int axis = 0; axis < 3; axis++) {
      if (rayDirection[axis] >= 0) {
        t0[axis] = (bounds[axis].getMinimum() - rayOrigin[axis]) / rayDirection[axis];
        t1[axis] = (bounds[axis].getMaximum() - rayOrigin[axis]) / rayDirection[axis];
      } else {
        t0[axis] = (bounds[axis].getMaximum() - rayOrigin[axis]) / rayDirection[axis];
        t1[axis] = (bounds[axis].getMinimum() - rayOrigin[axis]) / rayDirection[axis];
      }
    }

    double tMin = Math.max(Math.max(t0[0], t0[1]), t0[2]);
    double tMax = Math.min(Math.min(t1[0], t1[1]), t1[2]);

    return tMin <= tMax;
  }
}
