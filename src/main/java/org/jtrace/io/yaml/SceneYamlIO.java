package org.jtrace.io.yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jtrace.Material;
import org.jtrace.Scene;
import org.jtrace.ViewPlane;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Main API for loading and saving JTrace scenes from/to YAML files.
 * Supports:
 * - Custom primitive tags (!pt, !vector, !color, !reflect)
 * - Material libraries with $reference support
 * - Include directives for external material libraries
 * - Polymorphic deserialization of GeometricObjects, Cameras, and Lights
 */
public class SceneYamlIO {
    
    private ObjectMapper mapper;
    private final MaterialLibrary materialLibrary;
    private Path currentBasePath;
    
    public SceneYamlIO() {
        this.materialLibrary = new MaterialLibrary();
    }
    
    /**
     * Loads a scene from a YAML file.
     * 
     * @param path the path to the YAML file
     * @return the loaded Scene
     * @throws IOException if the file cannot be read or parsed
     */
    public Scene load(Path path) throws IOException {
        this.currentBasePath = path.getParent();
        this.mapper = new ObjectMapper(new YAMLFactory());
        this.mapper.registerModule(new JTraceYamlModule(currentBasePath));
        
        String content = new String(Files.readAllBytes(path));
        
        // Process includes first
        content = processIncludes(content, currentBasePath);
        
        // Parse the YAML
        JsonNode root = mapper.readTree(content);
        
        // Build material library from file
        buildMaterialLibrary(root);
        
        // Parse scene configuration
        SceneConfig config = mapper.treeToValue(root, SceneConfig.class);
        
        // Resolve material references in objects
        resolveMaterialReferences(config);
        
        return config.getScene();
    }
    
    /**
     * Saves a scene to a YAML file.
     * 
     * @param scene the scene to save
     * @param path the path to write to
     * @throws IOException if the file cannot be written
     */
    public void save(Scene scene, Path path) throws IOException {
        SceneConfig config = new SceneConfig(scene);
        mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), config);
    }
    
    /**
     * Processes !include directives in the YAML content.
     */
    private String processIncludes(String content, Path basePath) throws IOException {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("!include ")) {
                String includePath = trimmed.substring(9).trim();
                Path resolvedPath = basePath != null ? basePath.resolve(includePath) : Path.of(includePath);
                String includedContent = new String(Files.readAllBytes(resolvedPath));
                result.append(includedContent).append("\n");
            } else {
                result.append(line).append("\n");
            }
        }
        
        return result.toString();
    }
    
    /**
     * Builds the material library from the YAML structure.
     */
    private void buildMaterialLibrary(JsonNode root) {
        materialLibrary.clear();
        
        if (root.has("materials")) {
            JsonNode materialsNode = root.get("materials");
            Iterator<Map.Entry<String, JsonNode>> fields = materialsNode.fields();
            
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String materialName = entry.getKey();
                JsonNode materialNode = entry.getValue();
                
                try {
                    Material material = mapper.treeToValue(materialNode, Material.class);
                    materialLibrary.register(materialName, material);
                } catch (Exception e) {
                    System.err.println("Failed to parse material: " + materialName);
                }
            }
        }
    }
    
    /**
     * Resolves material references ($name) in scene objects.
     */
    private void resolveMaterialReferences(SceneConfig config) {
        // This is handled during deserialization via custom deserializer
    }
    
    /**
     * Configuration class representing a scene in YAML format.
     */
    public static class SceneConfig {
        private Map<String, Material> materials = new HashMap<>();
        private Scene scene = new Scene();
        
        public SceneConfig() {}
        
        public SceneConfig(Scene scene) {
            this.scene = scene;
        }
        
        public Map<String, Material> getMaterials() {
            return materials;
        }
        
        public void setMaterials(Map<String, Material> materials) {
            this.materials = materials;
        }
        
        public Scene getScene() {
            return scene;
        }
        
        public void setScene(Scene scene) {
            this.scene = scene;
        }
    }
}
