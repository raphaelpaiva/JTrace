package org.jtrace.examples;

import org.jtrace.Materials;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class LookingDownExample {
    public static void main(final String[] args) {
        final ViewPlane viewPlane = new ViewPlane(1920, 1080);

        final Point3D lookAt = Point3D.ORIGIN;
        final Point3D eye = new Point3D(0, 100, 0);
        final Vector3D up = new Vector3D(0, 1, 0);

        final Point3D lightPosition = new Point3D(0, 0, 0);

        final Point3D centerRed  = new Point3D(-30, 0, 0);
        final Point3D centerBlue = new Point3D(30, 0, 0);

        final Sphere red = new Sphere(centerRed, 10, Materials.matte(ColorRGB.RED));
        final Sphere blue = new Sphere(centerBlue, 10, Materials.matte(ColorRGB.BLUE));
        final Sphere originSphere = new Sphere(Point3D.ORIGIN, 0.3f, Materials.matte(ColorRGB.PURPLE));
        final Sphere lightSphere = new Sphere(lightPosition, 0.3f, Materials.matte(ColorRGB.WHITE));

        final Light light = new Light(lightPosition);

        final Camera pinHoleCamera = new PinHoleCamera(eye, lookAt, up);
        pinHoleCamera.setZoomFactor(10);

        final Scene scene = new Scene().add(blue, red, originSphere, lightSphere).add(light).setCamera(pinHoleCamera);

        final Tracer tracer = new Tracer();

        tracer.addListeners(new ImageListener("lookDown.png", "png"), new TimeListener());

        tracer.render(scene, viewPlane);
    }
}
