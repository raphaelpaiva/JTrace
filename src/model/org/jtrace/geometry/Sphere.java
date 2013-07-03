package org.jtrace.geometry;

import org.jtrace.Constants;
import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.Material;
import org.jtrace.NotHit;
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
    private double radius;

    /**
     * Creates a {@link Sphere} from its center and radius.
     * 
     * @param center a {@link Point3D} representing the coordinates of the center of the {@link Sphere}
     * @param d the radius of the {@link Sphere}
     * @param color the color of the {@link Sphere}
     */
    public Sphere(final Point3D center, final double d, final Material material) {
        super(material);
        this.center = center;
        this.radius = d;
    }

    /**
     * Calculates if a given {@link Jay} hits the Object.
     * 
     * @param jay the casted {@link Jay}.
     * @return {@link Hit} if the {@link Jay} hits the object.
     */
    @Override
    public Hit hit(final Jay jay) {
        final Vector3D temp = new Vector3D(jay.getOrigin().subtract(center));

        final double a = jay.getDirection().dot();
        final double b = temp.multiply(2).dot(jay.getDirection());
        final double c = temp.dot() - radius * radius;

        final double delta = b * b - 4 * a * c;
        final double deltaRoot = Math.sqrt(delta);

        double t;

        if (delta < 0.0) {
            return new NotHit();
        } else {
            //smaller root
            t = (-b - deltaRoot) / 2*a;
            if (t > Constants.epsilon) {
                final Vector3D normal = temp.add(jay.getDirection().multiply(t)).divide(t);
                return new Hit(t, normal.normal());
            }

            //larger root
            t = (-b + deltaRoot) / 2*a;
            if (t > Constants.epsilon) {
                final Vector3D normal = temp.add(jay.getDirection().multiply(t)).divide(t);
                return new Hit(t, normal.normal());
            }

            return new NotHit();
        }
    }
    
    @Override
    public ColorRGB getColor(Point3D point) {
        Vector3D pole = Vector3D.UNIT_Y;
        Vector3D equator = Vector3D.UNIT_X;
 
        Vector3D normal = new Vector3D(center, point).normal();
 
        double normalDotPole = normal.dot(pole);
        double phi = Math.acos(normalDotPole);
 
        double v = phi / Math.PI;
 
        Vector3D poleCrossEquator = pole.cross(equator);
 
        double equatorDotNormal = equator.dot(normal);
 
        double theta = Math.acos(equatorDotNormal / Math.sin(phi)) / (2 * Math.PI);
 
        double u = 0;
 
        if (poleCrossEquator.dot(normal) > 0) {
            u = theta;
        } else {
            u = 1 - theta;
        }
            
        return getMaterial().getColor(u, v);
    }

    public Point3D getCenter() {
        return center;
    }

    public void setCenter(final Point3D center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(final float radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "c" + center.toString() + ", r = " + radius;
    }

}
