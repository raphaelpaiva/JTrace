package org.jtrace.examples;

import java.io.IOException;

import org.jtrace.Materials;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.OrthogonalCamera;
import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class OrthogonalCameraExample {
  public static void main(String[] args) throws IOException {
    ViewPlane viewPlane = new ViewPlane(1024, 768);

    Point3D eye = new Point3D(0, 0, 200);
    Point3D lookAt = new Point3D(0, 0, 100);
    Vector3D up = new Vector3D(0, 1, 0);
    
    Camera camera = new OrthogonalCamera(eye, lookAt, up);
    
    Light light = new Light(-100, -50, -10);
    
    final Point3D centerRed = new Point3D(-50, 0, -10);
    final Point3D centerBlue = new Point3D(50, 0, -100);

    final Sphere red = new Sphere(centerRed, 50, Materials.matte(ColorRGB.RED));
    final Sphere blue = new Sphere(centerBlue, 50, Materials.matte(ColorRGB.BLUE));

    Scene scene = new Scene().add(blue, red).add(light).setCamera(camera);

    Tracer ot = new Tracer();

    ot.addListeners(new ImageListener("result.png", "png"), new TimeListener());

    ot.render(scene, viewPlane);
  }
}
