package org.jtrace.examples;

import org.jtrace.Materials;
import org.jtrace.PerspectiveTracer;
import org.jtrace.Scene;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class FourSpheresTwoPlanesPerspectiveExample {
    public static void main(final String[] args) {
        final ViewPlane viewPlane = new ViewPlane(1920, 1080);

        final Point3D lookAt = Point3D.ORIGIN;
        final Point3D eye = new Point3D(100, 0, -100);
        final Vector3D up = new Vector3D(0, 1, 0);

        final Point3D lightPosition = new Point3D(0, -20, 10);

        final Point3D centerRed  = new Point3D(0, 0, -30);
        final Point3D centerBlue = new Point3D(0, 0, 30);

        final Point3D planePoint = new Point3D(0, 10.1, 0);
        final Vector3D planeNormal = new Vector3D(0, -1, 0);

        final Sphere red = new Sphere(centerRed, 10, Materials.matte(ColorRGB.RED));
        final Sphere blue = new Sphere(centerBlue, 10, Materials.matte(ColorRGB.BLUE));
        final Sphere originSphere = new Sphere(Point3D.ORIGIN, 0.3f, Materials.matte(ColorRGB.PURPLE));
        final Sphere lightSphere = new Sphere(lightPosition, 0.3f, Materials.matte(ColorRGB.WHITE));

        final Plane lowerPlane = new Plane(planePoint, planeNormal, Materials.matte(ColorRGB.YELLOW));
        final Plane upperPlane = new Plane(new Point3D(0, -30, 0), planeNormal.multiply(-1), Materials.matte(ColorRGB.GREEN));
        final Light light = new Light(lightPosition);

        final Camera pinHoleCamera = new PinHoleCamera(eye, lookAt, up);

        final Scene scene = new Scene().add(blue, red, originSphere, lightSphere, lowerPlane, upperPlane).add(light).setCamera(pinHoleCamera);

        final PerspectiveTracer ot = new PerspectiveTracer();

        ot.addListeners(new ImageListener("result_Perspective.png", "png"), new TimeListener());

        ot.render(scene, viewPlane);
    }
}
