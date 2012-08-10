package org.jtrace.shader;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class SpecularShader implements Shader {

	@Override
	public ColorRGB shade(Light light, Hit hit, Jay jay, GeometricObject object) {
		if (object.getMaterial().getkSpecular() == null) {
			return ColorRGB.BLACK;
		}
		
		Point3D hitPoint = jay.getOrigin().add( jay.getDirection().multiply(hit.getT()) ); 
		
		Vector3D lightVec = new Vector3D(hitPoint, light.getPosition()).normal();
		
		Vector3D an = hit.getNormal().multiply( lightVec.dot(hit.getNormal()) );
		Vector3D at = an.subtract(lightVec);
		
		Vector3D R = an.add(at);
		
		Vector3D v = new Vector3D(hitPoint, jay.getOrigin()).normal();
		
		double specularContribution = Math.pow(R.dot(v), 4);
		
		double r = object.getMaterial().getkSpecular().getRed() * specularContribution;
		double g = object.getMaterial().getkSpecular().getRed() * specularContribution;
		double b = object.getMaterial().getkSpecular().getBlue() * specularContribution;
		
		return new ColorRGB(r, g, b);
	}

}
