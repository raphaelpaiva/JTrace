package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jtrace.primitives.ReflectanceCoefficient;

import java.io.IOException;

/**
 * Custom Jackson serializer for ReflectanceCoefficient with !reflect YAML tag.
 * Serializes as: !reflect {r: 0.7, g: 0.7, b: 0.7}
 */
public class ReflectanceCoefficientSerializer extends JsonSerializer<ReflectanceCoefficient> {
    
    @Override
    public void serialize(ReflectanceCoefficient value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("r", value.getRed());
        gen.writeNumberField("g", value.getGreen());
        gen.writeNumberField("b", value.getBlue());
        gen.writeEndObject();
    }
}
