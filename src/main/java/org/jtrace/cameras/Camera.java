package org.jtrace.cameras;

import org.jtrace.Jay;
import org.jtrace.Scene;
import org.jtrace.ViewPlane;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

/**
 * The abstract Camera. <br>
 * 
 * The Camera's main function is to create Jays to the {@link ViewPlane}'s coordinates.<br>
 * 
 * A {@link Camera} should be added to the {@link Scene} in order to render the latter.
 * 
 * @author raphaelpaiva
 *
 */
public abstract class Camera {
	/**
	 * The {@link Camera}'s position in space.
	 * 
	 */
    protected Point3D eye;
    
    /**
     * The point to look at. Used to compute the direction that the {@link Camera} is looking.
     */
    protected Point3D lookAt;
    
    /**
     * The direction of the {@link Camera}'s "above". Used to compute rotation orthogonal to the looking direction. 
     */
    protected Vector3D up;

    /**
     * The normal vector in the inverse look direction.
     * 
     */
    protected Vector3D w;
    
    /**
     * Coplanar and perpendicular to w.<br>
     * 
     * {@link #u} is the normalized cross product between {@link #up} and {@link #w}.
     */
    protected Vector3D u;
    
    /**
     * Represents the camera's normalized "above".
     * 
     * {@link #v} is normalized the cross product between {@link #w} and {@link #u}.
     */
    protected Vector3D v;
   
    /**
     * The distance between the {@link #eye} to the {@link ViewPlane}.
     */
    protected double viewPlaneDistance;

    private double zoomFactor = 1;

    /**
     * Computes the camera's coordinate System.
     * This method <b>must</b> be called in any concrete {@link Camera} constructor.
     */
    protected void computeUVW() {
        final Vector3D lookVector = new Vector3D(lookAt, eye);
        viewPlaneDistance = lookVector.module(); // TODO: make is costumizable.

        if (lookVector.isParallelTo(up)) {
            if (eye.getY() > lookAt.getY()) {
                u = new Vector3D(0, 0, 1);
                v = new Vector3D(1, 0, 0);
                w = new Vector3D(0, 1, 0);
            } else {
                u = new Vector3D(0, 0, 1);
                v = new Vector3D(1, 0, 0);
                w = new Vector3D(0, -1, 0);
            }
        } else {
            w = lookVector.normal();
            u = up.cross(w).normal();
            v = w.cross(u);
        }
    }

    /**
     * Creates a {@link Jay} to to intersect a point P(c,r) in the {@link ViewPlane}'s coordinates.
     * 
     * @param r the vertical coordinate of P.
     * @param c the horizontal coordinate of P.
     * @param vres the vertical resolution of the {@link ViewPlane}.
     * @param hres the horizontal resolution of the {@link ViewPlane}.
     * @return A {@link Jay} intersecting P.
     */
    public abstract Jay createJay(int r, int c, int vres, int hres);

    /**
     * The {@link #zoomFactor} usage depends on the concrete implementation of the {@link Camera}.
     * 
     * @return The {@link #zoomFactor} of the {@link Camera}.
     */
    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(final double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    /**
     * @return The {@link Camera}'s eye position in space.
     */
    public Point3D getEye() {
        return eye;
    }

    /**
     * Updates the {@link Camera}'s coordinate system.
     * 
     * This method <b>must</b> be called if there were any changes to the {@link Camera} orientation or eye position.
     */
    public void update() {
        computeUVW();
    }

}
