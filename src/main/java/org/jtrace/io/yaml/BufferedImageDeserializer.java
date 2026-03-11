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
    
    private Path basePath;
    
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
        
        // Try to resolve relative to basePath first
        if (basePath != null) {
            Path resolvedPath = basePath.resolve(texturePath);
            File textureFile = resolvedPath.toFile();
            
            if (textureFile.exists()) {
                System.err.println("DEBUG: Loading texture from file: " + textureFile.getAbsolutePath());
                return ImageIO.read(textureFile);
            }
        }
        
        // Try as absolute path
        File absoluteFile = new File(texturePath);
        if (absoluteFile.exists()) {
            System.err.println("DEBUG: Loading texture from absolute: " + absoluteFile.getAbsolutePath());
            return ImageIO.read(absoluteFile);
        }
        
        // Try as resource from classpath
        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/" + texturePath));
            if (img != null) {
                System.err.println("DEBUG: Loading texture from classpath: /" + texturePath);
                return img;
            }
        } catch (Exception e) {
            // ignore
        }
        
        // Try without leading slash
        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream(texturePath));
            if (img != null) {
                System.err.println("DEBUG: Loading texture from classpath: " + texturePath);
                return img;
            }
        } catch (Exception e) {
            // ignore
        }
        
        System.err.println("Warning: Could not load texture: " + texturePath);
        return null;
    }
}
