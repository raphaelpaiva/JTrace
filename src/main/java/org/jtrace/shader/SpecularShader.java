package org.jtrace.shader;

import org.jtrace.tracer.Hit;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class SpecularShader implements Shader {

	private double specularFactor;
	
	public SpecularShader() {
		this.specularFactor = 1.0;
	}
	
	public SpecularShader(double specularFactor) {
		this.specularFactor = specularFactor;
	}
	
	@Override
	public ColorRGB shade(Light light, Hit hit, GeometricObject object) {
		if (object.getMaterial().getkSpecular() == null) {
			return ColorRGB.BLACK;
		}
		
		Point3D hitPoint = hit.getPoint(); 
		
		Vector3D pointToLight = new Vector3D(hitPoint, light.getPosition());
		
		Vector3D lightDirection = pointToLight.normal();
		
		Vector3D reflected = calculateSpecularLightReflection(lightDirection, hit.getNormal());
		
		Vector3D viewVector = new Vector3D(hitPoint, hit.getOrigin()).normal();
		
		double RdotV = reflected.dot(viewVector);
		
		if (RdotV > 0) {
			double distanceToLight = pointToLight.module();
			double lightIntensity = light.getIntensity(distanceToLight);
			
			double specularContribution = Math.pow(reflected.dot(viewVector), specularFactor);
			
			double r = light.getColor().getRed() * object.getMaterial().getkSpecular().getRed() * specularContribution;
			double g = light.getColor().getGreen() * object.getMaterial().getkSpecular().getRed() * specularContribution;
			double b = light.getColor().getBlue() * object.getMaterial().getkSpecular().getBlue() * specularContribution;
			
			return new ColorRGB(r, g, b).multiply(lightIntensity);
		} else {
			return ColorRGB.BLACK;
		}
	}
	
	protected Vector3D calculateSpecularLightReflection(final Vector3D lightVector, final Vector3D surfaceNormal)
	{
		double lightDotNormal = lightVector.dot(surfaceNormal);
		Vector3D an = surfaceNormal.multiply(lightDotNormal);
		
		Vector3D anTimes2 = an.multiply(2);
		
		Vector3D reflected = anTimes2.subtract(lightVector);
		
		return reflected;
	}
	
	public double getSpecularFactor() {
		return specularFactor;
	}
	
	public void setSpecularFactor(double specularFactor) {
		this.specularFactor = specularFactor;
	}

}
