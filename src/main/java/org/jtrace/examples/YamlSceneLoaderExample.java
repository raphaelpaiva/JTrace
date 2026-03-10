package org.jtrace.examples;

import org.jtrace.Scene;
import org.jtrace.io.yaml.SceneYamlIO;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Example demonstrating how to load a scene from YAML.
 * Shows the new Jackson-based YAML serialization with:
 * - Custom primitive tags (!pt, !vector, !color, !reflect)
 * - Material libraries with $references
 * - Polymorphic objects (Sphere, Plane, PinHoleCamera, PointLight)
 */
public class YamlSceneLoaderExample {
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: YamlSceneLoaderExample <scene.yaml>");
            System.exit(1);
        }
        
        Path scenePath = Paths.get(args[0]);
        SceneYamlIO yamlIO = new SceneYamlIO();
        
        try {
            Scene scene = yamlIO.load(scenePath);
            System.out.println("Scene loaded successfully!");
            System.out.println("Objects: " + scene.getObjects().size());
            System.out.println("Lights: " + scene.getLigths().size());
            System.out.println("Camera: " + scene.getCamera().getClass().getSimpleName());
        } catch (IOException e) {
            System.err.println("Failed to load scene: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
