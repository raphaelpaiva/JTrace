package org.jtrace.primitives;

public class Vector3D {
	private Point3D coordinate;

	public Vector3D(final Point3D coordinate) {
		this.coordinate = coordinate;
	}

	public Vector3D(final float x, final float y, final float z) {
		this(new Point3D(x, y, z));
	}

	public Point3D getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(final Point3D coordinate) {
		this.coordinate = coordinate;
	}

	public double dot(final Vector3D v2) {
		double result = coordinate.getX() * v2.getCoordinate().getX();
		result += coordinate.getY() * v2.getCoordinate().getY();
		result += coordinate.getZ() * v2.getCoordinate().getZ();
		return result;
	}

}
