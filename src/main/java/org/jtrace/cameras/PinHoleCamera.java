package org.jtrace.cameras;

import org.jtrace.Jay;
import org.jtrace.ViewPlane;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

/**
 * Creates rays in from the eye point straight to the center of the {@link ViewPlane} pixels.<br>
 * 
 * This is the simplest camera with perspective notion.
 * 
 * @author raphaelpaiva
 *
 */
public class PinHoleCamera extends Camera {

	public PinHoleCamera(final Point3D eye, final Point3D lookAt, final Vector3D up) {
		this.eye = eye;
		this.lookAt = lookAt;
		this.up = up;

		computeUVW();
	}


	@Override
	public Jay createJay(final int r, final int c, int vres, int hres) {
	  double hresD = hres;
	  double vresD = vres;
		final double viewPlaneX = (c - hresD/2 + 0.5) * 1 / getZoomFactor();
		final double viewPlaneY = (r - vresD/2 + 0.5) * 1 / getZoomFactor();

		final Vector3D dU = u.multiply(viewPlaneX);
		final Vector3D dV = v.multiply(viewPlaneY);
		final Vector3D dW = w.multiply(viewPlaneDistance);

		final Vector3D jayDirection = dU.add(dV).subtract(dW).normal();

		return new Jay(eye, jayDirection);
	}
}
