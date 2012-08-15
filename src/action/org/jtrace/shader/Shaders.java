package org.jtrace.shader;

public class Shaders {
	public static Shader ambientShader() {
		return new AmbientShader();
	}
	
	public static Shader diffuseShader() {
		return new DiffuseShader();
	}
	
	public static Shader specularShader(double specularFactor) {
		return new SpecularShader(specularFactor);
	}
}
