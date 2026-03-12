package org.jtrace;

import static java.util.Arrays.asList;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jtrace.cameras.Camera;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Represents the scene to be rendered.
 * 
 * A scene is a collection of {@link GeometricObject}, {@link Light} and a single {@link Camera}.<br>
 * 
 * The scene is passed to the {@link Tracer#render(Scene, ViewPlane)} method in order to be rendered by that {@link Tracer}<br>
 * 
 * All {@link GeometricObject} and {@link Light} added are stored in a {@link LinkedHashSet}.
 * 
 * @author raphaelpaiva
 *
 */
public class Scene {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = org.jtrace.geometry.Sphere.class, name = "Sphere"),
        @JsonSubTypes.Type(value = org.jtrace.geometry.Plane.class, name = "Plane"),
        @JsonSubTypes.Type(value = org.jtrace.geometry.Triangle.class, name = "Triangle"),
        @JsonSubTypes.Type(value = org.jtrace.geometry.TriangleMesh.class, name = "TriangleMesh"),
        @JsonSubTypes.Type(value = org.jtrace.geometry.Quadrilateral.class, name = "Quadrilateral")
    })
    private Set<GeometricObject> objects = new LinkedHashSet<GeometricObject>();
    
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = org.jtrace.lights.PointLight.class, name = "PointLight"),
        @JsonSubTypes.Type(value = org.jtrace.lights.DecayingPointLight.class, name = "DecayingPointLight")
    })
    private Set<Light> lights = new LinkedHashSet<Light>();
    private ColorRGB backgroundColor = ColorRGB.BLACK;
    
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = org.jtrace.cameras.PinHoleCamera.class, name = "PinHoleCamera"),
        @JsonSubTypes.Type(value = org.jtrace.cameras.OrthogonalCamera.class, name = "OrthogonalCamera")
    })
    private Camera camera;

    public Scene() {
    }

    public Scene withBackground(final ColorRGB color) {
        backgroundColor = color;
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

    public void setObjects(Set<GeometricObject> objects) {
        this.objects = objects;
    }

    public Set<Light> getLigths() {
        return lights;
    }

    public void setLights(Set<Light> lights) {
        this.lights = lights;
    }

    public ColorRGB getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(ColorRGB backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Camera getCamera() {
        return camera;
    }

    public Scene setCamera(final Camera camera) {
        this.camera = camera;
        return this;
    }

    public void setCameraVoid(final Camera camera) {
        this.camera = camera;
    }

}
