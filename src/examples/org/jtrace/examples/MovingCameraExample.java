package org.jtrace.examples;

import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Triangle;
import org.jtrace.lights.Light;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.AmbientShader;
import org.jtrace.shader.DiffuseShader;

public class MovingCameraExample {
    private static Point3D LEFT_VERTEX = new Point3D(-10, 0, 0);
    private static Point3D RIGHT_VERTEX = new Point3D(10, 0, 0);
    private static Point3D TOP_VERTEX = new Point3D(0, 20, 0);

    private static Triangle TRIANGLE = new Triangle(LEFT_VERTEX, RIGHT_VERTEX, TOP_VERTEX);

    private static ViewPlane VIEW_PLANE = new ViewPlane(1280, 720);

    private static Scene SCENE = prepareScene();

    private static Tracer TRACER = prepareTracer();

    private static int RADIUS = 200;
    private static int DURATION = 5; //seconds
    private static double FINISH_ANGLE = 2*Math.PI; //radians
    private static double START_ANGLE = 0; //radians
    private static int FPS = 60;

    private static double INCREMENT = (FINISH_ANGLE - START_ANGLE)/(DURATION*FPS);

    private static Scene prepareScene() {
        final Point3D eye = new Point3D(0, 0, 200);
        final Point3D lookAt = new Point3D(0, 10, 0);
        final Vector3D up = new Vector3D(0, 1, 0);

        final Light light = new Light(0, 10, 10);

        final Camera pinHoleCamera = new PinHoleCamera(eye, lookAt, up);
        pinHoleCamera.setZoomFactor(20);

        final Scene scene = new Scene().add(TRIANGLE).add(light).setCamera(pinHoleCamera);

        return scene;
    }

    private static Tracer prepareTracer() {
		Tracer tracer = new Tracer();
		
		tracer.addShaders(new AmbientShader(), new DiffuseShader());
		
		return tracer;
	}

	private static void render(final int frame) {
        TRACER.addListeners(new ImageListener("movingframe-" + String.format("%03d", frame) + ".png", "png"), new TimeListener());

        TRACER.render(SCENE, VIEW_PLANE);

        TRACER.clearListeners();
    }

    private static void updateCameraPosition(final int frame) {
        double x;
        double y = 0;
        double z;

        double angle = frame * INCREMENT;

        z = RADIUS * Math.sin(angle);
        x = RADIUS * Math.cos(angle);

        Camera camera = SCENE.getCamera();
        Point3D eye = camera.getEye();
        eye.setX(x);
        eye.setY(y);
        eye.setZ(z);

        camera.update();
    }

    public static void main(final String[] args) {
        for (int frame = 0; frame < DURATION * FPS + 1; frame++) {
            updateCameraPosition(frame);
            render(frame);
        }

    }
}
