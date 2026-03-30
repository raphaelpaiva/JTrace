package org.jtrace.geometry;

import org.jtrace.Jay;
import org.jtrace.Section;
import org.jtrace.tracer.Hit;

import java.util.List;

public class CompositeGeometricObject extends GeometricObject {

  private GeometricObject[] children;

  public CompositeGeometricObject(GeometricObject... objects) {
    super(null);
    this.children = objects;
  }

  @Override
  public Hit hit(Jay jay) {
    throw new IllegalStateException("CompositeGeometricObject is not meant to be hit directly. Use its components instead.");
  }

  @Override
  public List<Section> sections(Jay jay) {
    throw new IllegalStateException("CompositeGeometricObject is not meant to be hit directly. Use its components instead.");
  }

  public GeometricObject[] getChildren() {
    return children;
  }
}
