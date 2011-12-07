package org.jtrace.primitives;

public class Vector3D {
	private Point3D coordinate;

	public Vector3D(final Point3D coordinate) {
		this.coordinate = coordinate;
	}

	public Vector3D(final double x, final double y, final double z) {
		this(new Point3D(x, y, z));
	}

	public Vector3D(final Vector3D v2) {
		this(v2.getCoordinate());
	}

	public Vector3D(Point3D a, Point3D b) {
		this(b.subtract(a));
	}

	public Vector3D multiply(double multiplier) {
		return new Vector3D(coordinate.getX() * multiplier, coordinate.getY()
				* multiplier, coordinate.getZ() * multiplier);
	}

	public double dot(final Vector3D v2) {
		double result = coordinate.getX() * v2.getCoordinate().getX();
		result += coordinate.getY() * v2.getCoordinate().getY();
		result += coordinate.getZ() * v2.getCoordinate().getZ();
		return result;
	}

	public double dot() {
		return dot(this);
	}

	public double module() {
		double result = coordinate.getX() * coordinate.getX();
		result += coordinate.getY() * coordinate.getY();
		result += coordinate.getZ() * coordinate.getZ();
		return Math.sqrt(result);
	}

	public Vector3D normal() {
		double module = module();

		double x = coordinate.getX() / module;
		double y = coordinate.getY() / module;
		double z = coordinate.getZ() / module;

		return new Vector3D(x, y, z);
	}

	public Point3D getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(final Point3D coordinate) {
		this.coordinate = coordinate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coordinate == null) ? 0 : coordinate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3D other = (Vector3D) obj;
		if (coordinate == null) {
			if (other.coordinate != null)
				return false;
		} else if (!coordinate.equals(other.coordinate))
			return false;
		return true;
	}

}
