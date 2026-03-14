package org.jtrace;

import org.jtrace.interceptor.ShadowInterceptor;
import org.jtrace.io.yaml.SceneYamlIO;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.shader.Shaders;
import org.jtrace.tracer.TaskTracer;
import org.jtrace.tracer.Tracer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main class for JTrace - renders scenes from YAML configuration files.
 * 
 * Usage:
 *   java -jar jtrace.jar <input.yaml> [output.png]
 * 
 * Arguments:
 *   input  - Path to YAML file containing scene, tracer, and viewPlane configuration (required)
 *   output - Path to output image file (optional, defaults to output.png)
 * 
 * If output is provided, any ImageListener in the YAML will be replaced with a new one
 * using the provided output path.
 * 
 * If output is not provided:
 *   - If YAML contains an ImageListener, it will be used as-is
 *   - If YAML has no ImageListener, a default one (output.png) will be added
 */
public class Main {
    
    private static final String DEFAULT_OUTPUT = "output.png";
    
    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }
        
        Path inputPath = Paths.get(args[0]);
        
        if (!Files.exists(inputPath)) {
            System.err.println("Error: Input file does not exist: " + inputPath);
            System.exit(1);
        }
        
        String outputPath = args.length > 1 ? args[1] : DEFAULT_OUTPUT;
        
        try {
            // Load configuration from YAML
            SceneYamlIO yamlIO = new SceneYamlIO();
            SceneYamlIO.SceneConfiguration config = yamlIO.loadConfiguration(inputPath);
            
            Scene scene = config.getScene();
            Tracer tracer = config.getTracer();
            ViewPlane viewPlane = config.getViewPlane();
            
            // Handle ImageListener configuration
            configureTracerOutput(tracer, outputPath, args.length > 1);
            
            // Render the scene
            tracer.render(scene, viewPlane);
            
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Configures the tracer's output based on command line arguments.
     * 
     * @param tracer the tracer to configure
     * @param outputPath the output file path
     * @param userProvidedOutput whether the user explicitly provided the output argument
     */
    private static void configureTracerOutput(Tracer tracer, String outputPath, boolean userProvidedOutput) {
        boolean hasImageListener = tracer.getListeners().stream()
                .anyMatch(listener -> listener instanceof ImageListener);
        
        if (userProvidedOutput) {
            // User provided output argument - replace any existing ImageListener
            tracer.getListeners().removeIf(listener -> listener instanceof ImageListener);
            tracer.addListeners(new ImageListener(outputPath, getFormatFromPath(outputPath)));
            System.out.println("Output: " + outputPath);
        } else if (!hasImageListener) {
            // No output argument and no ImageListener in YAML - add default
            tracer.addListeners(new ImageListener(DEFAULT_OUTPUT, "png"));
            System.out.println("Output: " + DEFAULT_OUTPUT);
        } else {
            // Respect existing ImageListener from YAML
            for (Object listener : tracer.getListeners()) {
                if (listener instanceof ImageListener imageListener) {
                  System.out.println("Output: " + imageListener.getFileName());
                }
            }
        }
        
        // Add TimeListener if not present for performance info
        boolean hasTimeListener = tracer.getListeners().stream()
                .anyMatch(listener -> listener instanceof TimeListener);
        if (!hasTimeListener) {
            tracer.addListeners(new TimeListener());
        }
    }
    
    /**
     * Extracts image format from file path.
     */
    private static String getFormatFromPath(String path) {
        String lower = path.toLowerCase();
        if (lower.endsWith(".png")) return "png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "jpg";
        if (lower.endsWith(".bmp")) return "bmp";
        if (lower.endsWith(".gif")) return "gif";
        return "png"; // default
    }
    
    private static void printUsage() {
        System.out.println("JTrace - Ray Tracing Engine");
        System.out.println();
        System.out.println("Usage: java -jar jtrace.jar <input.yaml> [output.png]");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  input.yaml   - Path to YAML configuration file (required)");
        System.out.println("  output.png   - Path to output image file (optional, defaults to output.png)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -jar jtrace.jar scene.yaml");
        System.out.println("  java -jar jtrace.jar scene.yaml myrender.png");
    }
}
