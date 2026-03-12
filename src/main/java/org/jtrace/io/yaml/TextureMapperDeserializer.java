package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jtrace.material.TextureMapper;
import org.jtrace.material.TextureMappers;

import java.io.IOException;

public class TextureMapperDeserializer extends JsonDeserializer<TextureMapper> {

    @Override
    public TextureMapper deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        
        if (value == null) {
            return TextureMappers.PLANAR;
        }
        
        switch (value.toUpperCase()) {
            case "SPHERICAL":
                return TextureMappers.SPHERICAL;
            case "PLANAR":
            default:
                return TextureMappers.PLANAR;
        }
    }
}