package org.jtrace.cameras;

import org.jtrace.Jay;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public abstract class Camera {
	protected Point3D eye, lookAt;
	protected Vector3D up;
	protected Vector3D u,v,w;
	
	protected double viewPlaneDistance;
	
	protected void computeUVW() {
		Vector3D lookVector = new Vector3D(lookAt, eye);
		viewPlaneDistance = lookVector.module();
		
		w = lookVector.normal();
		u = up.cross(w).normal();
		v = w.cross(u);
	}

	public abstract Jay createJay(int r, int c, int vres, int hres);
	
}
