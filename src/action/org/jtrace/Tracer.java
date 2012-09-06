package org.jtrace;

import static java.util.Arrays.asList;

import java.util.LinkedList;
import java.util.List;

import org.jtrace.cameras.Camera;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.Shader;

/**
 * Abstract class containing the common operation and fields of a Tracer.
 */
public class Tracer {

    private List<TracerListener> listeners = new LinkedList<TracerListener>();
    private List<Shader> shaders = new LinkedList<Shader>();

    /**
     * Traces the given {@link Jay}.
     * 
     * @param scene
     *            the {@link Scene} containing the objects to rendered.
     * @param jay
     *            the {@link Jay} to be casted.
     * @return the color of the object hit by the {@link Jay} or the
     *         {@link Scene}'s background color if there was no hit.
     */
    public ColorRGB trace(Scene scene, Jay jay) {
        ColorRGB finalColor = scene.getBackgroundColor();

        Hit hit = cast(scene, jay);

        // if there was a collision, calculate illumination
        if (hit.isHit()) {
            ColorRGB hitColor = shade(scene, jay, hit);
            finalColor = finalColor.add(hitColor);
        }

        return finalColor;
    }

    public Hit cast(Scene scene, Jay jay) {
        double tMin = Double.MAX_VALUE;
        GeometricObject hitObject = null;
        Hit hitMin = new Hit();

        for (GeometricObject object : scene.getObjects()) {
            Hit hit = object.hit(jay);

            if (hit.isHit() && hit.getT() < tMin) {
                tMin = hit.getT();
                hitObject = object;
                hitMin = hit;
            }
        }


        hitMin.setObject(hitObject);
        return hitMin;
    }

    public ColorRGB shade(Scene scene, Jay jay, Hit hit) {
        ColorRGB color = scene.getBackgroundColor();

        for (Light light : scene.getLigths()) {
            Point3D hitPoint = hit.getPoint(jay);
            Vector3D lightDirection = new Vector3D(hitPoint, light.getPosition());

            double hitPointToLightDistance = lightDirection.module();

            Jay lightJay = new Jay(hitPoint, lightDirection.normal());

            Hit hitLight = cast(scene, lightJay);

            if (hitLight.isHit()) {
                if (hitLight.getT() <= hitPointToLightDistance) {
                    return ColorRGB.BLACK;
                }
            }

            for (Shader shader : shaders) {
                ColorRGB shaderColor = shader.shade(light, hit, jay, hit.getObject());
                color = color.add(shaderColor);
            }
        }

        return color;
    }

    /**
     * Renders the scene.
     * The {@link Jay} casting strategy is defined by the {@link Camera} used in the scene.
     * @param scene the {@link Scene} to be rendered.
     * @param viewPlane this should change =p.
     */
    public void render(Scene scene, ViewPlane viewPlane) {
        final int hres = viewPlane.getHres();
        final int vres = viewPlane.getVres();
        final Camera camera = scene.getCamera();

        fireStart(viewPlane);

        for (int r = 0; r < vres; r++) {
            for (int c = 0; c < hres; c++) {
                final Jay jay = camera.createJay(r, c, vres, hres);

                final ColorRGB color = trace(scene, jay);

                fireAfterTrace(color, c, r);
            }
        }

        fireFinish();
    }

    protected void fireFinish() {
        for (TracerListener listener : listeners) {
            listener.finish();
        }
    }

    protected void fireAfterTrace(ColorRGB color, int c, int r) {
        for (TracerListener listener : listeners) {
            listener.afterTrace(color, c, r);
        }
    }

    protected void fireStart(ViewPlane viewPlane) {
        for (TracerListener listener : listeners) {
            listener.start(viewPlane);
        }
    }

    /**
     * Adds a {@link TracerListener} to the {@link Tracer}.
     * 
     * @param paramListeners one or more listeners.
     */
    public void addListeners(TracerListener... paramListeners) {
        listeners.addAll(asList(paramListeners));
    }


    /**
     * Adds a {@link Shader} to the {@link Tracer}.
     * 
     * @param paramShaders one or more shaders.
     */
    public void addShaders(Shader... paramShaders) {
        shaders.addAll(asList(paramShaders));
    }

    public void clearListeners() {
        listeners.clear();
    }
}
