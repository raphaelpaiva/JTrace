package org.jtrace;

import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector;

public class Jay {
	private final Point3D point;
	private final Vector direction;

	public Jay(final Point3D point, final Vector direction) {
		this.point = point;
		this.direction = direction;
	}

	public Point3D getPoint() {
		return point;
	}

	public Vector getDirection() {
		return direction;
	}

}
