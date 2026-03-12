package org.jtrace.material;

import org.jtrace.tracer.Hit;

public interface TextureMapper {
  UVMapping map(Hit hit);
}
