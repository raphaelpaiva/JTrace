package org.jtrace.shader;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AmbientShader.class, name = "AmbientShader"),
    @JsonSubTypes.Type(value = DiffuseShader.class, name = "DiffuseShader"),
    @JsonSubTypes.Type(value = SpecularShader.class, name = "SpecularShader")
})
public interface Shader {
	public ColorRGB shade(Light light, Hit hit, Jay jay, GeometricObject object);
}
