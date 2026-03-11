package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jtrace.material.PlanarTextureMapper;
import org.jtrace.material.SphericalTextureMapper;
import org.jtrace.material.TextureMapper;

import java.io.IOException;

public class TextureMapperSerializer extends JsonSerializer<TextureMapper> {

    @Override
    public void serialize(TextureMapper value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value instanceof SphericalTextureMapper) {
            gen.writeString("spherical");
        } else if (value instanceof PlanarTextureMapper) {
            gen.writeString("planar");
        } else {
            gen.writeString("planar");
        }
    }
}