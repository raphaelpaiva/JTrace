package org.jtrace.geometry;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jtrace.Constants;
import org.jtrace.Jay;
import org.jtrace.Section;
import org.jtrace.material.Material;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.tracer.Hit;
import org.jtrace.tracer.NotHit;

import java.util.List;

public class Cylinder extends GeometricObject {

  private Point3D base;
  private Vector3D axis;
  private double radius;
  private double height;
  private Disk upperCap;
  private Disk lowerCap;

  public Cylinder(
      @JsonProperty("base") Point3D base,
      @JsonProperty("axis") Vector3D axis,
      @JsonProperty("radius") double radius,
      @JsonProperty(value = "height", defaultValue = "0.0") double height,
      @JsonProperty("material") Material material) {
    super(material);
    this.base = base;
    this.axis = axis.normal();
    this.radius = radius;
    this.height = height;

    if (height > 0) {
      this.lowerCap = new Disk(base, this.axis.multiply(-1), radius, material);
      this.upperCap = new Disk(base.add(this.axis.multiply(height)), this.axis, radius, material);
    }
  }

  @Override
  public Hit hit(Jay jay) {
    Hit upperCapHit = new NotHit();
    Hit lowerCapHit = new NotHit();

    if (height > 0) {
      upperCapHit = upperCap.hit(jay);
      lowerCapHit = lowerCap.hit(jay);
    }

    Hit bodyHit = bodyHit(jay);

    double bodyT     = bodyHit.isHit()     ? bodyHit.getT()     : Double.POSITIVE_INFINITY;
    double upperCapT = upperCapHit.isHit() ? upperCapHit.getT() : Double.POSITIVE_INFINITY;
    double lowerCapT = lowerCapHit.isHit() ? lowerCapHit.getT() : Double.POSITIVE_INFINITY;

    double minT = Math.min(bodyT, Math.min(upperCapT, lowerCapT));

    if (minT == Double.POSITIVE_INFINITY) {
      return new NotHit();
    }

    Hit closestHit = null;

    if (minT == bodyT) {
      closestHit = bodyHit;
    } else if (minT == upperCapT) {
      closestHit = upperCapHit;
    } else {
      closestHit = lowerCapHit;
    }

    return closestHit;
  }

  private Hit bodyHit(Jay jay) {
    // https://en.wikipedia.org/wiki/Line-cylinder_intersection
    // || axis x (P - base) ||^2 = radius^2
    // || axis x (origin + t * direction - base) ||^2 = radius^2
    // || axis x (t * direction + origin - base) ||^2 = radius^2
    // let w = origin - base
    // || axis x (t * direction + w) ||^2 = radius^2
    // || axis x (t * direction) + axis x w ||^2 = radius^2
    // || t * (axis x direction) + (axis x w) ||^2 = radius^2
    // let v1 = axis x direction
    // let v2 = axis x w
    // || t * v1 + v2 ||^2 = radius^2
    // ||v||^2 = v . v
    // (t*v1 + v2) . (t*v1 + v2) = radius^2
    // Distribute the dot product:
    // (t*v1) . (t*v1) + (t*v1) . v2 + v2 . (t*v1) + v2 . v2 = radius^2
    // t^2 * (v1 . v1) + t * (v1 . v2) + t * (v2 . v1) + (v2 . v2) = radius^2
    // t^2 * (v1 . v1) + 2t * (v1 . v2) + (v2 . v2) - radius^2 = 0
    // Quadratic formula: at^2 + bt + c = 0
    // a = v1 . v1
    // b = 2 * (v1 . v2)
    // c = (v2 . v2) - radius^2

    Vector3D w = jay.getOrigin().subtract(base);
    Vector3D v1 = axis.cross(jay.getDirection());
    Vector3D v2 = axis.cross(w);

    double a = v1.dot(v1);
    double b = 2 * v1.dot(v2);
    double c = v2.dot(v2) - radius * radius;

    double delta = b * b - 4 * a * c;

    if (delta < 0) {
      return new NotHit();
    }

    if (delta == 0) {
      double t = -b / (2 * a);
      if (t < Constants.epsilon) {
        return new NotHit();
      }

      Point3D hitPoint = jay.getOrigin().add(jay.getDirection().multiply(t));
      Point3D closestOnAxis = base.add(axis.multiply(new Vector3D(base, hitPoint).dot(axis)));
      Vector3D normal = hitPoint.subtract(closestOnAxis).normal();

      if (height > 0) {
        double hitHeight = new Vector3D(base, hitPoint).dot(axis);
        if (hitHeight < 0 || hitHeight >= height) {
          return new NotHit();
        }
      }

      return new Hit(t, normal, jay);
    }

    if (delta > 0) {
      double sqrtDelta = Math.sqrt(delta);
      double t1 = (-b - sqrtDelta) / (2 * a);
      double t2 = (-b + sqrtDelta) / (2 * a);

      boolean t1Valid = t1 >= Constants.epsilon && isWithinBounds(t1, jay);
      boolean t2Valid = t2 >= Constants.epsilon && isWithinBounds(t2, jay);

      if (!t1Valid && !t2Valid) {
        return new NotHit();
      }

      double t;
      if (t1Valid && t2Valid) {
        t = Math.min(t1, t2);
      } else if (t1Valid) {
        t = t1;
      } else {
        t = t2;
      }

      Point3D hitPoint = jay.getOrigin().add(jay.getDirection().multiply(t));

      Point3D closestOnAxis = base.add(axis.multiply(new Vector3D(base, hitPoint).dot(axis)));
      Vector3D normal = hitPoint.subtract(closestOnAxis).normal();
      return new Hit(t, normal, jay);
    }

    return new NotHit();
  }

  private boolean isWithinBounds(double t, Jay jay) {
    Point3D hitPoint = jay.getOrigin().add(jay.getDirection().multiply(t));

    if (height > 0) {
      double hitHeight = new Vector3D(base, hitPoint).dot(axis);
      return !(hitHeight < 0) && !(hitHeight >= height);
    }

    return true;
  }

  @Override
  public List<Section> sections(Jay jay) {
    return List.of();
  }
}
