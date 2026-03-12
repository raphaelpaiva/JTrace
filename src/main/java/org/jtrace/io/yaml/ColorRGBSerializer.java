package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jtrace.primitives.ColorRGB;

import java.io.IOException;

/**
 * Custom Jackson serializer for ColorRGB with !color YAML tag.
 * Serializes as: !color {r: 1.0, g: 0.0, b: 0.0}
 */
public class ColorRGBSerializer extends JsonSerializer<ColorRGB> {
    
    @Override
    public void serialize(ColorRGB value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("r", value.getRed());
        gen.writeNumberField("g", value.getGreen());
        gen.writeNumberField("b", value.getBlue());
        gen.writeEndObject();
    }
}
