package org.jtrace.geometry;

import java.util.LinkedList;
import java.util.List;

import org.jtrace.Hit;
import org.jtrace.Jay;
import org.jtrace.material.Material;
import org.jtrace.material.Materials;
import org.jtrace.NotHit;
import org.jtrace.Section;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class TriangleMesh extends GeometricObject {
 
    private List<Triangle> triangles = new LinkedList<Triangle>();
 
    private double xmin = Double.POSITIVE_INFINITY;
    private double xmax = Double.NEGATIVE_INFINITY;
 
    private double ymin = Double.POSITIVE_INFINITY;
    private double ymax = Double.NEGATIVE_INFINITY;
 
    private double zmin = Double.POSITIVE_INFINITY;
    private double zmax = Double.NEGATIVE_INFINITY;
 
    public TriangleMesh(Material material) {
        super(material);
    }
 
    /**
     * 
     * @param triangles
     * @throws IndexOutOfBoundsException if <code>triangles</code> is empty
     */
    public TriangleMesh(List<Triangle> triangles) throws IndexOutOfBoundsException {
        super(triangles.get(0).getMaterial());
 
        for (Triangle t : triangles) {
            add(t);
        }
    }
    
    public TriangleMesh() {
    	super(Materials.metallic(ColorRGB.WHITE));
    }
    
    @Override
    public Hit hit(Jay jay) {
 
        double tMin = Double.MAX_VALUE;
        Hit hitMin = new NotHit();
 
        for (Triangle triangle : triangles) {
            Hit hit = triangle.hit(jay);
 
            if (hit.isHit() && hit.getT() < tMin) {
                tMin = hit.getT();
                hitMin = hit;
            }
        }
 
        hitMin.setObject(this);
        return hitMin;
    }

    @Override
    public List<Section> sections(Jay jay) {
      // TODO Auto-generated method stub
      return null;
    }
    
    public void add(Triangle... triangles) {
        for (Triangle t : triangles) {
            xmax = Math.max(xmax, t.getXMax());
            xmin = Math.min(xmin, t.getXMin());

            ymax = Math.max(ymax, t.getYMax());
            ymin = Math.min(ymin, t.getYMin());

            zmax = Math.max(zmax, t.getZMax());
            zmin = Math.min(zmin, t.getZMin());

            this.triangles.add(t);
        }
    }

    public Point3D getCenter() {
        double x = (xmax + xmin)/2;
        double y = (ymax + ymin)/2;
        double z = (zmax + zmin)/2;

        return new Point3D(x, y, z);
    }

    public Vector3D getBoundsFromCenter() {
        return new Vector3D(xmax -xmin, ymax - ymin, zmax - zmin).divide(2);
    }

}