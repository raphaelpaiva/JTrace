package org.jtrace.primitives;

public class Vector {
	private Point3D coordinate;

	public Vector(final Point3D coordinate) {
		this.coordinate = coordinate;
	}

	public Vector(final float x, final float y, final float z) {
		this(new Point3D(x, y, z));
	}

	public Point3D getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(final Point3D coordinate) {
		this.coordinate = coordinate;
	}

}
