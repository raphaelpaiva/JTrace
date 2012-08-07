package org.jtrace.examples;

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
import org.jtrace.shader.AmbientShader;
import org.jtrace.shader.DiffuseShader;

public class OrthogonalCameraExample {
    public static void main(final String[] args) {
        final ViewPlane viewPlane = new ViewPlane(1024, 768);

        final Point3D eye = new Point3D(0, 0, 200);
        final Point3D lookAt = new Point3D(0, 0, 100);
        final Vector3D up = new Vector3D(0, 1, 0);

        final Camera camera = new OrthogonalCamera(eye, lookAt, up);

        final Light light = new Light(-100, -50, -10);

        final Point3D centerRed = new Point3D(-50, 0, -10);
        final Point3D centerBlue = new Point3D(50, 0, -1000);

        final Sphere red = new Sphere(centerRed, 50, Materials.matte(ColorRGB.RED));
        final Sphere blue = new Sphere(centerBlue, 50, Materials.matte(ColorRGB.BLUE));

        final Scene scene = new Scene().add(blue, red).add(light).setCamera(camera);

        final Tracer ot = new Tracer();

        ot.addListeners(new ImageListener("result.png", "png"), new TimeListener());
        
        ot.addShaders(new AmbientShader(), new DiffuseShader());

        ot.render(scene, viewPlane);
    }
}
