package org.jtrace;

import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

/**
 * Basic Class representing a casted Ray.
 * 
 * @author raphaelpaiva
 * @author brunocosta
 * @author flaviocdc
 *
 */
public class Jay {
	private final Point3D origin;
	private final Vector3D direction;

	/**
	 * Creates a {@link Jay} from its origin coordinates and a {@link Vector3D} indicating its direction.
	 * 
	 * @param origin a {@link Point3D} representing the origin coordinates of the {@link Jay}.
	 * @param direction a {@link Vector3D} representing the {@link Jay} direction.
	 */
	public Jay(final Point3D origin, final Vector3D direction) {
		this.origin = origin;
		this.direction = direction;
	}
	
	/**
	 * Calculates the point in the line that the {@link Jay} describes.
	 * 
	 * @param t the line parameter.
	 * @return The equivalent to {@link Jay#getOrigin()} + t * {@link Jay#getDirection()}
	 */
	public Point3D getPointAt(double t) {
		return origin.add(direction.multiply(t));
	}

	public Point3D getOrigin() {
		return origin;
	}

	public Vector3D getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return "o" + origin.toString() + ", d" + direction.toString();
	}
	
}
