package org.jtrace;

import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.shader.Shader;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = org.jtrace.interceptor.ShadowInterceptor.class, name = "ShadowInterceptor")
})
public interface TracerInterceptor {
	public void init(Tracer tracer, Scene scene);
	
	public void beforeShade(Light light, ColorRGB color);
	public boolean shouldShade(Shader shader, Light light, Hit hit, Jay jay, GeometricObject object);
	public void afterShade(Light light, ColorRGB color);
	
}
