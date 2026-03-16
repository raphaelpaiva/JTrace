package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jtrace.primitives.ReflectanceCoefficient;

import java.io.IOException;

/**
 * Custom Jackson deserializer for ReflectanceCoefficient with !reflect YAML tag.
 * Expects: !reflect {r: 0.7, g: 0.7, b: 0.7}
 */
public class ReflectanceCoefficientDeserializer extends JsonDeserializer<ReflectanceCoefficient> {
    
    @Override
    public ReflectanceCoefficient deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Double r = null, g = null, b = null, k = null;
        
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
                case "k":
                    k = p.getDoubleValue();
                    break;
            }
        }

        if (k != null) {
            return new ReflectanceCoefficient(k);
        }
        
        return new ReflectanceCoefficient(r, g, b);
    }
}
