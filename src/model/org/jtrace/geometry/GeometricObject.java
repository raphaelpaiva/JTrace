package org.jtrace.geometry;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;

public abstract class GeometricObject {
	
	private Material material;
	
	public GeometricObject(Material material) {
		this.material = material;
	}

	public abstract Hit hit(Jay jay);
	
	public Material getMaterial() {
		return material;
	}

}
