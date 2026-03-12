package org.jtrace.interceptor;

import org.jtrace.tracer.Hit;
import org.jtrace.Jay;
import org.jtrace.Scene;
import org.jtrace.tracer.Tracer;
import org.jtrace.tracer.TracerInterceptor;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.Shader;

public class ShadowInterceptor implements TracerInterceptor {

	private transient Tracer tracer;
	private transient Scene scene;
	
	public ShadowInterceptor() {
	}
	
	@Override
	public void init(Tracer tracer, Scene scene) {
		this.tracer = tracer;
		this.scene = scene;
	}

	@Override
	public void beforeShade(Light light, ColorRGB color) {}

	@Override
	public boolean shouldShade(Shader shader, Light light, Hit hit, GeometricObject object) {
      Point3D hitPoint = hit.getPoint();
      Vector3D lightDirection = new Vector3D(hitPoint, light.getPosition());

      double hitPointToLightDistance = lightDirection.module();

      Jay lightJay = new Jay(hitPoint, lightDirection.normal());

      Hit hitLight = tracer.cast(scene, lightJay);

      if (hitLight.isHit()) {
          return !(hitLight.getT() <= hitPointToLightDistance);
      }
      
      return true;
	}

	@Override
	public void afterShade(Light light, ColorRGB color) {}

}
