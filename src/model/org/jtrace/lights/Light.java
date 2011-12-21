package org.jtrace.lights;

import org.jtrace.primitives.Point3D;

public class Light {

	private Point3D posicao;

	public Light(Point3D posicao) {
		this.posicao = posicao;
	}

	public Point3D getPosicao() {
		return posicao;
	}

}
