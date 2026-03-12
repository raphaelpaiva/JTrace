package org.jtrace.material;

import org.jtrace.Hit;

public interface TextureMapper {
  UVMapping map(Hit hit);
}
