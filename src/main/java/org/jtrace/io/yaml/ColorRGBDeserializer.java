package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jtrace.primitives.ColorRGB;

import java.io.IOException;

/**
 * Custom Jackson deserializer for ColorRGB with !color YAML tag.
 * Expects: !color {r: 1.0, g: 0.0, b: 0.0}
 */
public class ColorRGBDeserializer extends JsonDeserializer<ColorRGB> {
    
    @Override
    public ColorRGB deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        double r = 0.0, g = 0.0, b = 0.0;
        
        while (p.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = p.getCurrentName();
            p.nextToken();
            
            switch (fieldName) {
                case "r":
                    r = p.getDoubleValue();
                    break;
                case "g":
                    g = p.getDoubleValue();
                    break;
                case "b":
                    b = p.getDoubleValue();
                    break;
            }
        }
        
        return new ColorRGB(r, g, b);
    }
}
