package org.jtrace.primitives;

public class ColorRGB {
	private int r, g, b;

	public ColorRGB(final int r, final int g, final int b) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public int getR() {
		return r;
	}

	public void setR(final int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(final int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(final int b) {
		this.b = b;
	}
}