package org.jtrace;

import org.jtrace.geometry.GeometricObject;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

/**
 * Describes a collision or a non-collision of a {@link Jay} and a
 * {@link GeometricObject}.
 * 
 * @author raphaelpaiva
 * 
 */
public class Hit {

	private double t;

	private boolean hit;

	private Vector3D normal;

	private GeometricObject object;

	private final Point3D origin;

	private final Point3D point;

	public Hit(double t, Vector3D normal, Point3D origin, Point3D point) {
		this.t = t;
		this.normal = normal;
		this.origin = origin;
		this.point = point;
		this.hit = true;
	}

	protected Hit() {
		this.t = 0;
		this.normal = null;
		this.origin = null;
		this.point = null;
		this.hit = false;
	}

	public Point3D getPoint() {
		return point;
	}

	public Point3D getOrigin() {
		return origin;
	}

	/**
	 * @return The {@link Jay} parameter where the collision took place.
	 * @throws IllegalStateException
	 *             if there wasn't collision.
	 */
	public double getT() {
		return t;
	}

	/**
	 * @return The {@link GeometricObject}'s normal {@link Vector3D} at the
	 *         point of the collision, or <code>null</code> if there wasn't
	 *         collision.
	 * 
	 */
	public Vector3D getNormal() {
		return normal;
	}

	public void invertNormal() {
		normal = normal.multiply(-1);
	}
	
	/**
	 * @return Whether it was a hit or a not.
	 */
	public boolean isHit() {
		return hit;
	}

	/**
	 * @return The {@link GeometricObject} collided with, or <code>null</code>
	 *         if there wasn't collision.
	 */
	public GeometricObject getObject() {
		return object;
	}

	public void setObject(GeometricObject object) {
		this.object = object;
	}

}
