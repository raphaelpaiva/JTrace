package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jtrace.primitives.Vector3D;

import java.io.IOException;

/**
 * Custom Jackson serializer for Vector3D with !vector YAML tag.
 * Serializes as: !vector {x: 1.0, y: 2.0, z: 3.0}
 */
public class Vector3DSerializer extends JsonSerializer<Vector3D> {
    
    @Override
    public void serialize(Vector3D value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("x", value.getX());
        gen.writeNumberField("y", value.getY());
        gen.writeNumberField("z", value.getZ());
        gen.writeEndObject();
    }
}
