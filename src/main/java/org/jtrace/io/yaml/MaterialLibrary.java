package org.jtrace.io.yaml;

import org.jtrace.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for named materials that can be referenced in YAML files.
 * Supports $reference syntax for material reuse.
 */
public class MaterialLibrary {
    
    private final Map<String, Material> materials = new HashMap<>();
    
    /**
     * Registers a material with the given name.
     * 
     * @param name the material name/reference
     * @param material the material to register
     */
    public void register(String name, Material material) {
        materials.put(name, material);
    }
    
    /**
     * Retrieves a material by name.
     * 
     * @param name the material name/reference
     * @return the material, or null if not found
     */
    public Material get(String name) {
        return materials.get(name);
    }
    
    /**
     * Checks if a material with the given name exists.
     * 
     * @param name the material name/reference
     * @return true if the material exists
     */
    public boolean contains(String name) {
        return materials.containsKey(name);
    }
    
    /**
     * Merges another library into this one.
     * 
     * @param other the library to merge
     */
    public void merge(MaterialLibrary other) {
        materials.putAll(other.materials);
    }
    
    /**
     * Clears all registered materials.
     */
    public void clear() {
        materials.clear();
    }
    
    public Map<String, Material> getAll() {
        return new HashMap<>(materials);
    }
}
