package org.jtrace.cameras;

import org.jtrace.Jay;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class OrthogonalCamera extends Camera {

  public OrthogonalCamera(final Point3D eye, final Point3D lookAt, final Vector3D up) {
    this.eye = eye;
    this.lookAt = lookAt;
    this.up = up;

    computeUVW();
  }
  
  @Override
  public Jay createJay(int r, int c, int vres, int hres) {
    double hresD = hres;
    double vresD = vres;
    final double viewPlaneX = (c - hresD/2 + 0.5) * 1 / getZoomFactor();
    final double viewPlaneY = (r - vresD/2 + 0.5) * 1 / getZoomFactor();
    
    Point3D origin = new Point3D(viewPlaneX, viewPlaneY, viewPlaneDistance);

    final Vector3D jayDirection = new Vector3D(w.multiply(-1)).normal();

    return new Jay(origin, jayDirection);
  }

}
