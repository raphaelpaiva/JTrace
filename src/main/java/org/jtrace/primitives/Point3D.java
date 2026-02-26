package org.jtrace.primitives;

/**
 * Basic class to represent a point in a three-dimensional space.
 * 
 * @author raphaelpaiva
 * @author brunocosta
 * @author flaviocdc
 * 
 */
public class Point3D {
	public static final Point3D ORIGIN = new Point3D(0, 0, 0);

	private double x, y, z;

	private Point3D() {
	}

	/**
	 * Create a point from its coordinates
	 * 
	 * @param x
	 *            axis value
	 * @param y
	 *            axis value
	 * @param z
	 *            axis value
	 */
	public Point3D(final double x, final double y, final double z) {
		this();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Add vector operation
	 * 
	 * Performs the basic vector sum.<br>
	 * Suppose the point A(x, y, z) and the vector a(u, v, w).<br>
	 * 
	 * A + a = C(x + u, y + v, z + w)
	 * 
	 * @param vector
	 *            the vector to sum.
	 * @return a new {@link Point3D} equivalent to <code>(this + vector)</code>
	 */
	public Point3D add(final Vector3D vector) {
		return new Point3D(x + vector.getX(), y + vector.getY(), z
				+ vector.getZ());
	}

	/**
	 * Subtract vector operation
	 * 
	 * Performs the basic vector subtraction.<br>
	 * Suppose the point A and the vector a. <br>
	 * 
	 * A - a = A + a*(-1)
	 * 
	 * @param vector
	 *            the vector to subtract.
	 * @return a new {@link Point3D} equivalent to
	 *         <code>(this.add(vector.multiply(-1)))</code>
	 */
	public Point3D subtract(final Vector3D vector) {
		return add(vector.multiply(-1));
	}

	/**
	 * Subtract point operation
	 * 
	 * Performs the basic point subtraction.<br>
	 * Suppose the points A(x, y, z) and B(u, v, w).<br>
	 * 
	 * A - B = c(x - u, y - v, z - w)
	 * 
	 * @param otherPoint
	 *            the point to subtract.
	 * @return a new {@link Vector3D} equivalent to
	 *         <code>(this - otherPoint)</code>
	 */
	public Vector3D subtract(final Point3D otherPoint) {
		return new Vector3D(x - otherPoint.getX(), y - otherPoint.getY(), z
				- otherPoint.getZ());
	}

	/**
	 * Multiply by scalar operation
	 * 
	 * Suppose the points A(x, y, z) and a scalar 'f'.<br>
	 * 
	 * A * f = C(x * f, y * f, z * f)
	 * 
	 * @param factor
	 *            scalar to multiply
	 * @return a new {@link Point3D} equivalent to <code>(this * factor)</code>
	 */
	public Point3D multiply(final double factor) {
		return new Point3D(x * factor, y * factor, z * factor);
	}

	public double getX() {
		return x;
	}

	public void setX(final double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(final double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(final double z) {
		this.z = z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ temp >>> 32);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Point3D other = (Point3D) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		if (z != other.z) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}