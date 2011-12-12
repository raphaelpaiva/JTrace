package org.jtrace;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jtrace.geometry.GeometricObject;
import org.jtrace.geometry.Sphere;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class OrthogonalTracer {
	private static ColorRGB BACKGROUND_COLOR = ColorRGB.BLACK;
	private static ColorRGB DEFAULT_COLOR = ColorRGB.RED;

	public ColorRGB trace(Scene scene, Jay jay) {
		for (GeometricObject object : scene) {
			if (object.hit(jay)) {
				return DEFAULT_COLOR;
			}
		}

		return BACKGROUND_COLOR;
	}
	
	public void render(Scene scene, ViewPlane viewPlane) throws IOException
	{
		double x, y;
		int hres = viewPlane.getHres();
		int vres = viewPlane.getVres();
		double s = viewPlane.getPixelSize();
		
		Vector3D direction = new Vector3D(0, 0, -1);
		
		BufferedImage bi = new BufferedImage(hres, vres, BufferedImage.TYPE_INT_RGB);
		
		for (int r = 0; r < vres; r++) {
			for (int c = 0; c < hres; c++) {
				x = s*(c - 0.5 * (hres - 1.0));
				y = s*(r - 0.5 * (vres - 1.0));
				
				Point3D origin = new Point3D(x, y, 100);
				
				Jay jay = new Jay(origin, direction);
				
				ColorRGB color = trace(scene, jay);
				
				bi.setRGB(c, r, color.toInt());
			}
		}
		
		ImageIO.write(bi, "jpeg", new File("result.jpg"));
	}

	public static void main(String[] args) throws IOException {
		ViewPlane viewPlane = new ViewPlane(300, 300, 1.0);
		Point3D c = new Point3D(-42.5, 0, 0);
		Sphere s = new Sphere(c, 85.0f);
		
		Scene scene = new Scene();
		
		scene.add(s);
		
		OrthogonalTracer ot = new OrthogonalTracer();
		
		ot.render(scene, viewPlane);
	}
	
}
