package org.jtrace;

import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public abstract class Camera {
	protected Point3D eye, lookAt;
	protected Vector3D up;
	protected Vector3D u,v,w;
	
	protected void computeUVW() {
		w = new Vector3D(lookAt, eye).normal();
		u = up.cross(w).normal();
		v = w.cross(u);
	}
}
