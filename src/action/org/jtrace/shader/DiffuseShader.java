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
		ColorRGB objectColor = material.getColor();
		ReflectanceCoefficient kDiffuse = material.getkDiffuse();
		
		double dotLight = calculateDiffuseContribution(light, hit, jay);
		
		double red = kDiffuse.getRed() * objectColor.getRed() * dotLight;
		double green = kDiffuse.getGreen() * objectColor.getGreen() * dotLight;
		double blue = kDiffuse.getBlue() * objectColor.getBlue() * dotLight;
		
		return new ColorRGB(red, green, blue);
	}
	
	public double calculateDiffuseContribution(Light light, Hit hit, Jay jay) {
		Point3D hitPoint = calculateHitPoint(jay, hit);
		
		Vector3D lightDirection = new Vector3D(hitPoint, light.getPosition()).normal();
		double dotLight = lightDirection.dot(hit.getNormal().normal());
		return Math.max(dotLight, 0);
	}
	
	protected Point3D calculateHitPoint(Jay jay, Hit hit) {
		double x = jay.getOrigin().getX();
		double y = jay.getOrigin().getY();
		double z = jay.getOrigin().getZ();
		
		x += jay.getDirection().getX() * hit.getT();
		y += jay.getDirection().getY() * hit.getT();
		z += jay.getDirection().getZ() * hit.getT();
		
		return new Point3D(x, y, z);
	}
}
