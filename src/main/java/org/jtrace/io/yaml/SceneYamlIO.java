package org.jtrace.io.yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jtrace.Material;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.lights.Light;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.cameras.Camera;

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
 * - Polymorphic deserialization of GeometricObjects, Cameras, Lights, Shaders, Listeners
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
        
        // Build material library from file first (before deserializing objects)
        buildMaterialLibrary(root);
        
        // Replace material references in objects with actual material objects
        replaceMaterialReferences(root);
        
        // Parse scene configuration
        SceneConfig config = mapper.treeToValue(root, SceneConfig.class);
        
        // Fix materials in scene (set default color if texture present but color is null)
        fixSceneMaterials(config.getScene());
        
        // Initialize camera coordinate system
        initializeCamera(config.getScene());
        
        return config.getScene();
    }
    
    /**
     * Fixes all materials in the scene that have a texture but no color.
     * Also loads textures from texturePath if specified.
     */
    private void fixSceneMaterials(Scene scene) {
        for (org.jtrace.geometry.GeometricObject obj : scene.getObjects()) {
            Material material = obj.getMaterial();
            if (material != null) {
                fixMaterial(material);
            }
            
            // Fix Triangle - rebuild plane after deserialization
            if (obj instanceof org.jtrace.geometry.Triangle) {
                org.jtrace.geometry.Triangle triangle = (org.jtrace.geometry.Triangle) obj;
                fixTriangle(triangle);
            }
        }
    }
    
    /**
     * Fixes a Triangle after Jackson deserialization - rebuilds the internal plane.
     */
    private void fixTriangle(org.jtrace.geometry.Triangle triangle) {
        org.jtrace.primitives.Point3D v1 = triangle.getV1();
        org.jtrace.primitives.Point3D v2 = triangle.getV2();
        org.jtrace.primitives.Point3D v3 = triangle.getV3();
        
        if (v1 != null && v2 != null && v3 != null) {
            org.jtrace.primitives.Vector3D a12 = new org.jtrace.primitives.Vector3D(v1, v2);
            org.jtrace.primitives.Vector3D a31 = new org.jtrace.primitives.Vector3D(v3, v1);
            org.jtrace.primitives.Vector3D a23 = new org.jtrace.primitives.Vector3D(v2, v3);
            
            org.jtrace.primitives.Vector3D normal = a31.cross(a12).normal();
            
            org.jtrace.geometry.Plane plane = new org.jtrace.geometry.Plane(v1, normal, triangle.getMaterial());
            
            // Use reflection to set the private plane field
            try {
                java.lang.reflect.Field planeField = org.jtrace.geometry.Triangle.class.getDeclaredField("plane");
                planeField.setAccessible(true);
                planeField.set(triangle, plane);
                
                java.lang.reflect.Field a12Field = org.jtrace.geometry.Triangle.class.getDeclaredField("a12");
                a12Field.setAccessible(true);
                a12Field.set(triangle, a12);
                
                java.lang.reflect.Field a23Field = org.jtrace.geometry.Triangle.class.getDeclaredField("a23");
                a23Field.setAccessible(true);
                a23Field.set(triangle, a23);
                
                java.lang.reflect.Field a31Field = org.jtrace.geometry.Triangle.class.getDeclaredField("a31");
                a31Field.setAccessible(true);
                a31Field.set(triangle, a31);
            } catch (Exception e) {
                System.err.println("Warning: Could not fix Triangle: " + e.getMessage());
            }
        }
    }
    
    /**
     * Loads a tracer configuration from a YAML file.
     * 
     * @param path the path to the YAML file
     * @return the loaded Tracer
     * @throws IOException if the file cannot be read or parsed
     */
    public Tracer loadTracer(Path path) throws IOException {
        this.currentBasePath = path.getParent();
        this.mapper = new ObjectMapper(new YAMLFactory());
        this.mapper.registerModule(new JTraceYamlModule(currentBasePath));
        
        String content = new String(Files.readAllBytes(path));
        
        // Process includes first
        content = processIncludes(content, currentBasePath);
        
        // Parse the YAML
        JsonNode root = mapper.readTree(content);
        
        // Build material library from file first
        buildMaterialLibrary(root);
        
        // Replace material references in objects with actual material objects
        replaceMaterialReferences(root);
        
        // Parse scene configuration
        SceneConfig config = mapper.treeToValue(root, SceneConfig.class);
        
        // Initialize camera coordinate system
        initializeCamera(config.getScene());
        
        return config.getTracer();
    }
    
    /**
     * Loads a ViewPlane configuration from a YAML file.
     * 
     * @param path the path to the YAML file
     * @return the loaded ViewPlane
     * @throws IOException if the file cannot be read or parsed
     */
    public ViewPlane loadViewPlane(Path path) throws IOException {
        this.currentBasePath = path.getParent();
        this.mapper = new ObjectMapper(new YAMLFactory());
        this.mapper.registerModule(new JTraceYamlModule(currentBasePath));
        
        String content = new String(Files.readAllBytes(path));
        
        // Process includes first
        content = processIncludes(content, currentBasePath);
        
        // Parse the YAML
        JsonNode root = mapper.readTree(content);
        
        // Build material library from file first
        buildMaterialLibrary(root);
        
        // Replace material references in objects with actual material objects
        replaceMaterialReferences(root);
        
        // Parse scene configuration
        SceneConfig config = mapper.treeToValue(root, SceneConfig.class);
        
        return config.getViewPlane();
    }
    
    /**
     * Loads all configuration from a YAML file.
     * 
     * @param path the path to the YAML file
     * @return the loaded SceneConfiguration containing scene, tracer, and viewPlane
     * @throws IOException if the file cannot be read or parsed
     */
    public SceneConfiguration loadConfiguration(Path path) throws IOException {
        this.currentBasePath = path.getParent();
        this.mapper = new ObjectMapper(new YAMLFactory());
        this.mapper.registerModule(new JTraceYamlModule(currentBasePath));
        
        String content = new String(Files.readAllBytes(path));
        
        // Process includes first
        content = processIncludes(content, currentBasePath);
        
        // Parse the YAML
        JsonNode root = mapper.readTree(content);
        
        // Build material library from file first
        buildMaterialLibrary(root);
        
        // Replace material references in objects with actual material objects
        replaceMaterialReferences(root);
        
        // Parse scene configuration
        SceneConfig config = mapper.treeToValue(root, SceneConfig.class);
        
        // Fix materials in scene (set default color if texture present but color is null)
        fixSceneMaterials(config.getScene());
        
        // Initialize camera coordinate system
        initializeCamera(config.getScene());
        
        return new SceneConfiguration(config.getScene(), config.getTracer(), config.getViewPlane());
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
     * Saves a scene, tracer, and viewPlane to a YAML file.
     * 
     * @param scene the scene to save
     * @param tracer the tracer to save
     * @param viewPlane the viewPlane to save
     * @param path the path to write to
     * @throws IOException if the file cannot be written
     */
    public void save(Scene scene, Tracer tracer, ViewPlane viewPlane, Path path) throws IOException {
        SceneConfig config = new SceneConfig(scene, tracer, viewPlane);
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
                    fixMaterial(material);
                    materialLibrary.register(materialName, material);
                } catch (Exception e) {
                    System.err.println("Failed to parse material: " + materialName + " - " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Fixes materials that have a texture but no color.
     * Sets a default white color when texture is present but color is null.
     * Also loads textures from texturePath if specified.
     */
    private void fixMaterial(Material material) {
        // Load texture from texturePath if specified
        String texturePath = material.getTexturePath();
        if (texturePath != null && !texturePath.isEmpty() && material.getTexture() == null) {
            material.setTexture(loadTexture(texturePath));
        }
        
        if (material.getTexture() != null && material.getColor() == null) {
            material.setColor(new org.jtrace.primitives.ColorRGB(1.0, 1.0, 1.0));
        }
    }
    
    /**
     * Loads a texture image from the given path.
     */
    private java.awt.image.BufferedImage loadTexture(String texturePath) {
        // Try relative to scenes folder
        if (currentBasePath != null) {
            java.io.File file = currentBasePath.resolve(texturePath).toFile();
            if (file.exists()) {
                try {
                    return javax.imageio.ImageIO.read(file);
                } catch (Exception e) {
                    // continue to try other methods
                }
            }
        }
        
        // Try as file
        java.io.File absoluteFile = new java.io.File(texturePath);
        if (absoluteFile.exists()) {
            try {
                return javax.imageio.ImageIO.read(absoluteFile);
            } catch (Exception e) {
                // continue
            }
        }
        
        // Try as classpath resource
        try {
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/" + texturePath));
            if (img != null) return img;
        } catch (Exception e) {
            // continue
        }
        
        try {
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(getClass().getResourceAsStream(texturePath));
            if (img != null) return img;
        } catch (Exception e) {
            // continue
        }
        
        return null;
    }
    
    /**
     * Replaces material references ($name) in objects with actual material objects.
     */
    private void replaceMaterialReferences(JsonNode root) throws IOException {
        if (!root.has("scene")) {
            return;
        }
        
        JsonNode sceneNode = root.get("scene");
        if (!sceneNode.has("objects")) {
            return;
        }
        
        JsonNode objectsNode = sceneNode.get("objects");
        if (!objectsNode.isArray()) {
            return;
        }
        
        for (JsonNode objectNode : objectsNode) {
            if (objectNode.has("material")) {
                JsonNode materialNode = objectNode.get("material");
                
                if (materialNode.isTextual()) {
                    String materialValue = materialNode.asText();
                    if (materialValue.startsWith("$")) {
                        String materialName = materialValue.substring(1);
                        Material material = materialLibrary.get(materialName);
                        
                        if (material == null) {
                            throw new IOException("Unknown material reference: " + materialName);
                        }
                        
                        // Replace the string with the actual material object as JSON
                        ((com.fasterxml.jackson.databind.node.ObjectNode) objectNode).replace("material", 
                            mapper.valueToTree(material));
                    }
                }
            }
        }
    }
    
    /**
     * Initializes the camera's coordinate system after deserialization.
     * This is necessary because computeUVW() is called in constructors
     * but Jackson uses setters which bypass the constructor.
     */
    private void initializeCamera(Scene scene) {
        Camera camera = scene.getCamera();
        if (camera != null) {
            camera.update();
        }
    }
    
    /**
     * Configuration class representing a scene in YAML format.
     */
    public static class SceneConfig {
        private Map<String, Material> materials = new HashMap<>();
        private Scene scene = new Scene();
        private Tracer tracer = new Tracer();
        private ViewPlane viewPlane = new ViewPlane(640, 480);
        
        public SceneConfig() {}
        
        public SceneConfig(Scene scene) {
            this.scene = scene;
        }
        
        public SceneConfig(Scene scene, Tracer tracer, ViewPlane viewPlane) {
            this.scene = scene;
            this.tracer = tracer;
            this.viewPlane = viewPlane;
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
        
        public Tracer getTracer() {
            return tracer;
        }
        
        public void setTracer(Tracer tracer) {
            this.tracer = tracer;
        }
        
        public ViewPlane getViewPlane() {
            return viewPlane;
        }
        
        public void setViewPlane(ViewPlane viewPlane) {
            this.viewPlane = viewPlane;
        }
    }
    
    /**
     * Container for scene, tracer, and viewPlane configuration.
     */
    public static class SceneConfiguration {
        private final Scene scene;
        private final Tracer tracer;
        private final ViewPlane viewPlane;
        
        public SceneConfiguration(Scene scene, Tracer tracer, ViewPlane viewPlane) {
            this.scene = scene;
            this.tracer = tracer;
            this.viewPlane = viewPlane;
        }
        
        public Scene getScene() {
            return scene;
        }
        
        public Tracer getTracer() {
            return tracer;
        }
        
        public ViewPlane getViewPlane() {
            return viewPlane;
        }
    }
}
