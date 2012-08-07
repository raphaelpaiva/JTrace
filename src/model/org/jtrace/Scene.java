package org.jtrace;

import static java.util.Arrays.asList;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jtrace.cameras.Camera;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;

public class Scene {
    private final Set<GeometricObject> objects = new LinkedHashSet<GeometricObject>();
    private boolean hasAmbientLight = true;
    private final Set<Light> lights = new LinkedHashSet<Light>();
    private ColorRGB backgroundColor = ColorRGB.BLACK;
    private Camera camera;

    public Scene withBackground(final ColorRGB color) {
        backgroundColor = color;
        return this;
    }

    public Scene turnOffAmbientLight() {
        hasAmbientLight = false;
        return this;
    }

    public Scene turnOnAmbientLight() {
        hasAmbientLight = true;
        return this;
    }

    public Scene add(final Light light) {
        lights.add(light);
        return this;
    }

    public Scene add(final Light... paramLights) {
        lights.addAll(asList(paramLights));
        return this;
    }

    public Scene add(final GeometricObject object) {
        objects.add(object);
        return this;
    }

    public Scene add(final GeometricObject... paramObjects) {
        objects.addAll(asList(paramObjects));
        return this;
    }

    public Set<GeometricObject> getObjects() {
        return objects;
    }

    public Set<Light> getLigths() {
        return lights;
    }

    public ColorRGB getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isAmbientLightOn() {
        return hasAmbientLight;
    }

    public Camera getCamera() {
        return camera;
    }

    public Scene setCamera(final Camera camera) {
        this.camera = camera;
        return this;
    }

}
