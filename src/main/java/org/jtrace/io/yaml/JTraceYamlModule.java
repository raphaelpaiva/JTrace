package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;

import java.awt.image.BufferedImage;

/**
 * Jackson module for JTrace YAML serialization.
 * Registers custom serializers and deserializers for primitives with custom YAML tags.
 */
public class JTraceYamlModule extends SimpleModule {
    
    public JTraceYamlModule() {
        this(null);
    }
    
    public JTraceYamlModule(java.nio.file.Path basePath) {
        super("JTraceYamlModule", new Version(1, 0, 0, null, "org.jtrace", "jtrace"));
        
        // Register serializers for primitives
        addSerializer(Point3D.class, new Point3DSerializer());
        addSerializer(Vector3D.class, new Vector3DSerializer());
        addSerializer(ColorRGB.class, new ColorRGBSerializer());
        addSerializer(ReflectanceCoefficient.class, new ReflectanceCoefficientSerializer());
        addSerializer(BufferedImage.class, new BufferedImageSerializer());
        
        // Register deserializers for primitives
        addDeserializer(Point3D.class, new Point3DDeserializer());
        addDeserializer(Vector3D.class, new Vector3DDeserializer());
        addDeserializer(ColorRGB.class, new ColorRGBDeserializer());
        addDeserializer(ReflectanceCoefficient.class, new ReflectanceCoefficientDeserializer());
        addDeserializer(BufferedImage.class, new BufferedImageDeserializer(basePath));
    }
}
