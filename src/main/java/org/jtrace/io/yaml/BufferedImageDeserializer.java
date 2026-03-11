package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Custom Jackson deserializer for BufferedImage textures.
 * Deserializes from a file path string.
 */
public class BufferedImageDeserializer extends JsonDeserializer<BufferedImage> {
    
    private final Path basePath;
    
    public BufferedImageDeserializer() {
        this.basePath = null;
    }
    
    public BufferedImageDeserializer(Path basePath) {
        this.basePath = basePath;
    }
    
    @Override
    public BufferedImage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String texturePath = p.getValueAsString();
        
        if (texturePath == null || texturePath.isEmpty() || texturePath.startsWith("[")) {
            return null;
        }
        
        Path resolvedPath = basePath != null ? basePath.resolve(texturePath) : Paths.get(texturePath);
        File textureFile = resolvedPath.toFile();
        
        if (!textureFile.exists()) {
            // Try as resource from classpath
            try {
                return ImageIO.read(getClass().getResourceAsStream("/" + texturePath));
            } catch (Exception e) {
                System.err.println("Warning: Could not load texture: " + texturePath);
                return null;
            }
        }
        
        return ImageIO.read(textureFile);
    }
}
