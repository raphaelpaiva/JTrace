package org.jtrace.lights;

import org.jtrace.primitives.Point3D;

public class Light {

	private Point3D posicao;

	public Light(Point3D posicao) {
		this.posicao = posicao;
	}
	
	public Light(final double x, final double y, final double z) {
		this.posicao = new Point3D(x, y, z);
	}

	public Point3D getPosicao() {
		return posicao;
	}

}
