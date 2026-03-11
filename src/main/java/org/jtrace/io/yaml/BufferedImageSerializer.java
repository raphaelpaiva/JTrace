package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Custom Jackson serializer for BufferedImage textures.
 * Serializes as a file path string.
 */
public class BufferedImageSerializer extends JsonSerializer<BufferedImage> {
    
    @Override
    public void serialize(BufferedImage value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // For now, serialize as null or a placeholder
        // In a full implementation, you'd save the image and return the path
        if (value != null) {
            gen.writeString("[texture:" + value.getWidth() + "x" + value.getHeight() + "]");
        } else {
            gen.writeNull();
        }
    }
}
