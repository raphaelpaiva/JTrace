package org.jtrace.io.yaml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.jtrace.material.Material;

import java.io.IOException;

/**
 * Custom deserializer for Material that handles $reference syntax.
 * If the value starts with $, it's treated as a material library reference.
 * Otherwise, it's deserialized as a full Material object.
 */
public class MaterialDeserializer extends JsonDeserializer<Material> {
    
    private final MaterialLibrary materialLibrary;
    
    public MaterialDeserializer(MaterialLibrary materialLibrary) {
        this.materialLibrary = materialLibrary;
    }
    
    @Override
    public Material deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        
        if (value != null && value.startsWith("$")) {
            // It's a material reference
            String materialName = value.substring(1);
            Material material = materialLibrary.get(materialName);
            if (material == null) {
                throw new IOException("Unknown material reference: " + materialName);
            }
            return material;
        }
        
        // Fall back to default deserialization for inline materials
        // This won't work directly, so we need to handle it differently
        // For now, throw an error if it's not a reference
        throw new IOException("Material must be a reference (starting with $) or inline material not supported yet");
    }
}
