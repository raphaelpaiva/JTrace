# JTrace - A RayTrace Engine for Java

JTrace is an extensible Ray Tracing engine for Java.

## Features

- **Ray Tracing**: Render 3D scenes with realistic lighting
- **Geometric Objects**: Sphere, Plane, Triangle, TriangleMesh, Quadrilateral
- **Materials**: Configurable ambient, diffuse, and specular reflectance
- **Texture Mapping**: Support for image-based textures
- **YAML Configuration**: Define scenes using human-readable YAML files
- **Multiple Shaders**: Ambient, Diffuse, Specular shading
- **Shadows**: Shadow interceptor support
- **Multi-threading**: Parallel rendering support

## Building

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Build Commands

```bash
# Build the project
mvn compile

# Run tests
mvn test

# Package (creates fat JAR)
mvn package

# The JAR will be at: target/jtrace-1.0-SNAPSHOT.jar
```

## Running

### Basic Usage

```bash
java -jar jtrace.jar <input.yaml> [output.png]
```

**Arguments:**
- `input.yaml` - Path to YAML configuration file (required)
- `output.png` - Path to output image file (optional, defaults to output.png)

### Examples

```bash
# Render with default output (output.png)
java -jar jtrace.jar scene.yaml

# Render to custom output file
java -jar jtrace.jar scene.yaml myrender.png
```

## Configuration Files

JTrace uses YAML files to define scenes. See [YAML_FORMAT.md](YAML_FORMAT.md) for the complete syntax guide.

### Quick Example

```yaml
materials:
  redMetallic:
    color: !color {r: 1.0, g: 0.0, b: 0.0}
    kAmbient: !reflect {r: 0.07, g: 0.07, b: 0.07}
    kDiffuse: !reflect {r: 0.7, g: 0.7, b: 0.7}
    kSpecular: !reflect {r: 0.8, g: 0.8, b: 0.8}

scene:
  backgroundColor: !color {r: 0.0, g: 0.0, b: 0.0}
  
  camera:
    type: PinHoleCamera
    eye: !pt {x: 0.0, y: 0.0, z: 100.0}
    lookAt: !pt {x: 0.0, y: 0.0, z: 0.0}
    up: !vector {x: 0.0, y: 1.0, z: 0.0}
    zoomFactor: 10.0
  
  objects:
    - type: Sphere
      center: !pt {x: 0.0, y: 0.0, z: 0.0}
      radius: 10.0
      material: $redMetallic
  
  lights:
    - type: PointLight
      position: !pt {x: 0.0, y: 20.0, z: -10.0}

tracer:
  type: Tracer
  shaders:
    - type: AmbientShader
    - type: DiffuseShader
    - type: SpecularShader
      specularFactor: 4.0
  listeners:
    - type: ImageListener
      fileName: output.png
      format: png

viewPlane:
  hres: 1920
  vres: 1080
```

## Programmatic Usage

```java
import org.jtrace.*;
import org.jtrace.io.yaml.SceneYamlIO;

Path scenePath = Paths.get("scene.yaml");
SceneYamlIO yamlIO = new SceneYamlIO();
SceneYamlIO.SceneConfiguration config = yamlIO.loadConfiguration(scenePath);

Tracer tracer = config.getTracer();
ViewPlane viewPlane = config.getViewPlane();

tracer.render(config.getScene(), viewPlane);
```

## Examples

Example YAML scenes are located in `src/main/resources/scenes/`:

- `four_spheres_two_planes.yaml` - Basic scene with spheres and planes

## Contributing

JTrace is open source! Feel free to contribute.

1. Fork the repository
2. Create a feature branch
3. Submit a pull request

Found a bug? Have a feature request? [Open an issue](https://github.com/raphaelpaiva/JTrace/issues)!
