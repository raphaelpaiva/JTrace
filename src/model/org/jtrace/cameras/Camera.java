package org.jtrace.cameras;

import org.jtrace.Jay;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public abstract class Camera {
	protected Point3D eye, lookAt;
	protected Vector3D up;
	protected Vector3D u,v,w;
	
	protected double viewPlaneDistance;
	
	private double zoomFactor = 1;
	
	protected void computeUVW() {
		Vector3D lookVector = new Vector3D(lookAt, eye);
		viewPlaneDistance = lookVector.module();
		
		if (lookVector.isParallelTo(up)) {
		  if (eye.getY() > lookAt.getY()) {
		    u = new Vector3D(0, 0, 1);
		    v = new Vector3D(1, 0, 0);
		    w = new Vector3D(0, 1, 0);
		  } else {
		    u = new Vector3D(0, 0, 1);
		    v = new Vector3D(1, 0, 0);
		    w = new Vector3D(0, -1, 0);
		  }
		} else {
		  w = lookVector.normal();
	    u = up.cross(w).normal();
	    v = w.cross(u);
		}
	}

	public abstract Jay createJay(int r, int c, int vres, int hres);

	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}
	
}
