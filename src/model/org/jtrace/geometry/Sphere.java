package org.jtrace.geometry;

import org.jtrace.Jay;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

/**
 * Basic class Representing a Sphere in three-dimensional space.
 * 
 * @author raphaelpaiva
 * @author brunocosta
 *
 */
public class Sphere extends GeometricObject {
    private Point3D center;
    private float radius;

    /**
     * Creates a {@link Sphere} from its center and radius.
     * 
     * @param center a {@link Point3D} representing the coordinates of the center of the {@link Sphere}
     * @param radius the radius of the {@link Sphere}
     * @param color the color of the {@link Sphere}
     */
    public Sphere(final Point3D center, final float radius, ColorRGB color) {
    	super(color);
        this.center = center;
        this.radius = radius;
    }

    /**
     * Calculates if a given {@link Jay} hits the Object.
     * 
     * @param jay the casted {@link Jay}.
     * @return <code>true</code> if the {@link Jay} hits the object.
     */
    public boolean hit(final Jay jay) {
        Vector3D temp = new Vector3D(jay.getPoint().subtract(center));
        
        double a = jay.getDirection().dot();
        double b = temp.multiply(2).dot(jay.getDirection());
        double c = temp.dot() - radius * radius;

        double delta = b * b - 4 * a * c;

        if (delta < 0.0) {
            return false;
        } else {
            return true;
        }
    }

    public Point3D getCenter() {
        return center;
    }

    public void setCenter(final Point3D center) {
        this.center = center;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(final float radius) {
        this.radius = radius;
    }

}
