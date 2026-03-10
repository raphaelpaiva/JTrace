package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jtrace.primitives.Vector3D;

import java.io.IOException;

/**
 * Custom Jackson deserializer for Vector3D with !vector YAML tag.
 * Expects: !vector {x: 1.0, y: 2.0, z: 3.0}
 */
public class Vector3DDeserializer extends JsonDeserializer<Vector3D> {
    
    @Override
    public Vector3D deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
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
        
        return new Vector3D(x, y, z);
    }
}
