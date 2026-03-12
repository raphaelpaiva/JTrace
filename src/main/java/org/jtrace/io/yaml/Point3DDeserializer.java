package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jtrace.primitives.Point3D;

import java.io.IOException;

/**
 * Custom Jackson deserializer for Point3D with !pt YAML tag.
 * Expects: !pt {x: 1.0, y: 2.0, z: 3.0}
 */
public class Point3DDeserializer extends JsonDeserializer<Point3D> {
    
    @Override
    public Point3D deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        double x = 0.0, y = 0.0, z = 0.0;
        
        while (p.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = p.getCurrentName();
            p.nextToken();
            
            switch (fieldName) {
                case "x":
                    x = p.getDoubleValue();
                    break;
                case "y":
                    y = p.getDoubleValue();
                    break;
                case "z":
                    z = p.getDoubleValue();
                    break;
            }
        }
        
        return new Point3D(x, y, z);
    }
}
