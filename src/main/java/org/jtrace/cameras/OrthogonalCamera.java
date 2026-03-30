package org.jtrace.cameras;

import org.jtrace.Constants;
import org.jtrace.Jay;
import org.jtrace.ViewPlane;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

/**
 * A camera that creates all rays orthogonally to the {@link ViewPlane}'s coordinates,
 * therefore there is no perspective notion in the images rendered with this {@link Camera}.
 * 
 * @author raphaelpaiva
 *
 */
public class OrthogonalCamera extends Camera {

  public OrthogonalCamera() {
  }

  public OrthogonalCamera(final Point3D eye, final Point3D lookAt, final Vector3D up) {
    this.eye = eye;
    this.lookAt = lookAt;
    this.up = up;

    computeUVW();
  }
  
  @Override
  public Jay createJay(int r, int c, int vres, int hres, double adjustMin, double adjustMax) {
    double hresD = hres;
    double vresD = vres;

    double adjustRange = adjustMax - adjustMin;

    double xAdjust = 0.5;
    double yAdjust = 0.5;
    if (adjustRange > Constants.epsilon) {
      xAdjust = adjustMin + adjustRange * Math.random();
      yAdjust = adjustMin + adjustRange * Math.random();
    }

    final double viewPlaneX = (c - hresD/2 + xAdjust) * 1 / getZoomFactor();
    final double viewPlaneY = (r - vresD/2 + yAdjust) * 1 / getZoomFactor();
    
    Point3D origin = new Point3D(viewPlaneX, viewPlaneY, viewPlaneDistance);

    final Vector3D jayDirection = new Vector3D(w.multiply(-1)).normal();

    return new Jay(origin, jayDirection);
  }

}
