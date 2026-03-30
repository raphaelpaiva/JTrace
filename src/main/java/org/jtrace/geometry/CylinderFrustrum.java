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

public class CylinderFrustrum extends GeometricObject {

  private Point3D base;
  private Vector3D axis;
  private double height;
  private Disk upperCap;
  private Disk lowerCap;

  public CylinderFrustrum(
      @JsonProperty("base") Point3D base,
      @JsonProperty("axis") Vector3D axis,
      @JsonProperty("baseRadius") double baseRadius,
      @JsonProperty("topRadius") double topRadius,
      @JsonProperty(value = "height", defaultValue = "0.0") double height,
      @JsonProperty("material") Material material) {
    super(material);
    this.base = base;
    this.axis = axis.normal();
    this.height = height;

    this.lowerCap = new Disk(base, this.axis.multiply(-1), baseRadius, material);
    this.upperCap = new Disk(base.add(this.axis.multiply(height)), this.axis, topRadius, material);
  }

  @Override
  public Hit hit(Jay jay) {
    Hit upperCapHit = NotHit.INSTANCE;
    Hit lowerCapHit = NotHit.INSTANCE;

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
      return NotHit.INSTANCE;
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
    double alpha = height == 0 ? Double.MIN_VALUE : (upperCap.getRadius() - lowerCap.getRadius()) / height;
    Vector3D w   = jay.getOrigin().subtract(base);

    double hw    = w.dot(axis);
    double hd    = jay.getDirection().dot(axis);

    Vector3D v   = w.subtract(axis.multiply(hw));
    Vector3D u   = jay.getDirection().subtract(axis.multiply(hd));

    double   rw  = lowerCap.getRadius() + alpha * hw;
    double   rd  = alpha * hd;

    // || v + t * u ||^2 = (rw + t * rd)^2

    // at^2 + bt + c = 0
    double a = u.dot(u) - rd * rd;
    double b = 2 * (u.dot(v) - rw * rd);
    double c = v.dot(v) - rw * rw;

    double delta = b * b - 4 * a * c;

    if (delta < 0) {
      return NotHit.INSTANCE;
    }

    if (delta == 0) {
      double t = -b / (2 * a);
      Point3D hitPoint = jay.getOrigin().add(jay.getDirection().multiply(t));
      double hitHeight = new Vector3D(base, hitPoint).dot(axis);

      if (t > Constants.epsilon && isWithinBounds(hitHeight)) {
        Point3D closestOnAxis = base.add(axis.multiply(new Vector3D(base, hitPoint).dot(axis)));
        Vector3D normal = hitPoint.subtract(closestOnAxis).normal();

        return new Hit(t, normal, jay);
      }

      return NotHit.INSTANCE;
    }

    if (delta > 0) {
      double sqrtDelta = Math.sqrt(delta);
      double t1 = (-b - sqrtDelta) / (2 * a);
      double t2 = (-b + sqrtDelta) / (2 * a);

      Point3D hitPointT1 = jay.getOrigin().add(jay.getDirection().multiply(t1));
      double hitHeightT1 = new Vector3D(base, hitPointT1).dot(axis);

      Point3D hitPointT2 = jay.getOrigin().add(jay.getDirection().multiply(t2));
      double hitHeightT2 = new Vector3D(base, hitPointT2).dot(axis);

      boolean t1Valid = t1 >= Constants.epsilon && isWithinBounds(hitHeightT1);
      boolean t2Valid = t2 >= Constants.epsilon && isWithinBounds(hitHeightT2);

      if (!t1Valid && !t2Valid) {
        return NotHit.INSTANCE;
      }

      double t;
      Point3D hitPoint;
      double hitHeight;
      if (t1Valid && t2Valid) {
        t = Math.min(t1, t2);
        hitPoint  = t == t1 ? hitPointT1 : hitPointT2;
        hitHeight = t == t1 ? hitHeightT1 : hitHeightT2;
      } else if (t1Valid) {
        t = t1;
        hitPoint  = hitPointT1;
        hitHeight = hitHeightT1;
      } else {
        t = t2;
        hitPoint  = hitPointT2;
        hitHeight = hitHeightT2;
      }

      Point3D closestOnAxis = base.add(axis.multiply(hitHeight));
      Vector3D nperp = hitPoint.subtract(closestOnAxis);
      Vector3D normal = nperp.subtract(axis.multiply(alpha)).normal();

      return new Hit(t, normal, jay);
    }

    return NotHit.INSTANCE;
  }

  private boolean isWithinBounds(double hitHeight) {
    if (height > 0) {
      return hitHeight >= 0 && hitHeight < height;
    }

    return true;
  }

  @Override
  public List<Section> sections(Jay jay) {
    return List.of();
  }
}
