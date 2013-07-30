package org.jtrace.shader;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;

public class DiffuseShader implements Shader {
	public ColorRGB shade(Light light, Hit hit, Jay jay, GeometricObject object) {
		Material material = object.getMaterial();
		ColorRGB objectColor = object.getColor(hit.getPoint(jay));
		ReflectanceCoefficient kDiffuse = material.getkDiffuse();
		
		Vector3D pointToLight = new Vector3D(hit.getPoint(jay), light.getPosition());
		
		double dotLight = calculateDiffuseContribution(pointToLight, hit.getNormal().normal());
		
		double distanceToLight = pointToLight.module();
		
		double lightIntensity = light.getIntensity(distanceToLight);
		
		double red = light.getColor().getRed() * kDiffuse.getRed() * objectColor.getRed() * dotLight;
		double green = light.getColor().getGreen() * kDiffuse.getGreen() * objectColor.getGreen() * dotLight;
		double blue = light.getColor().getBlue() * kDiffuse.getBlue() * objectColor.getBlue() * dotLight;
		
		return new ColorRGB(red, green, blue).multiply(lightIntensity);
	}
	
	public double calculateDiffuseContribution(Vector3D pointToLightVector, Vector3D surfaceNormal) {
		Vector3D lightDirection = pointToLightVector.normal();
		double dotLight = lightDirection.dot(surfaceNormal);

		return Math.max(dotLight, 0);
	}
	
}
