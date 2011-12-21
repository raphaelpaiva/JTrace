package org.jtrace;

import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CameraUnitTest {
	
	@Test
	public void testComputeUVW() {
		Point3D  eye = new Point3D(0, 0, -10);
		Vector3D up = new Vector3D(0, 3, 0);
		Point3D lookAt = new Point3D(0, 5, 0);

		Vector3D u = new Vector3D(-1.0, 0.0, 0.0);
		Vector3D v = new Vector3D(0, 2/Math.sqrt(5), -1/Math.sqrt(5));
		Vector3D w = new Vector3D(0, -5, -10).normal();
		
		Camera camera = new DummyCamera(eye, lookAt, up);

		Assert.assertEquals(camera.w, w);
		Assert.assertEquals(camera.v, v);
		Assert.assertEquals(camera.u.getX(), u.getX());
	}
	
	private class DummyCamera extends Camera {
		public DummyCamera(Point3D eye, Point3D lookAt, Vector3D up) {
			this.eye = eye;
			this.lookAt = lookAt;
			this.up = up;
			
			computeUVW();
		}
	}

}
