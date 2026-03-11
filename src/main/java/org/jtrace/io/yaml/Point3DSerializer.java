package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jtrace.primitives.Point3D;

import java.io.IOException;

/**
 * Custom Jackson serializer for Point3D with !pt YAML tag.
 * Serializes as: !pt {x: 1.0, y: 2.0, z: 3.0}
 */
public class Point3DSerializer extends JsonSerializer<Point3D> {
    
    @Override
    public void serialize(Point3D value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("x", value.getX());
        gen.writeNumberField("y", value.getY());
        gen.writeNumberField("z", value.getZ());
        gen.writeEndObject();
    }
}
