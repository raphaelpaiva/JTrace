package org.jtrace.shader;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;

public class DiffuseShader implements Shader {
	public ColorRGB shade(Light light, Hit hit, Jay jay, GeometricObject object) {
		Material material = object.getMaterial();
		ColorRGB objectColor = object.getColor(hit.getPoint(jay));
		ReflectanceCoefficient kDiffuse = material.getkDiffuse();
		
		double dotLight = calculateDiffuseContribution(light, hit, jay);
		
		double red = light.getColor().getRed() * kDiffuse.getRed() * objectColor.getRed() * dotLight;
		double green = light.getColor().getGreen() * kDiffuse.getGreen() * objectColor.getGreen() * dotLight;
		double blue = light.getColor().getBlue() * kDiffuse.getBlue() * objectColor.getBlue() * dotLight;
		
		return new ColorRGB(red, green, blue);
	}
	
	public double calculateDiffuseContribution(Light light, Hit hit, Jay jay) {
		Point3D hitPoint = hit.getPoint(jay);
		
		Vector3D lightDirection = new Vector3D(hitPoint, light.getPosition()).normal();
		double dotLight = lightDirection.dot(hit.getNormal().normal());
		return Math.max(dotLight, 0);
	}
	
}
