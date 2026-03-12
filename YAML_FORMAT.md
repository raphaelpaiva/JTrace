# JTrace YAML Scene Format Specification

## Overview

The JTrace YAML format provides a human-readable way to define ray tracing scenes, including geometric objects, materials, lights, and cameras. It supports:

- **Custom YAML tags** for JTrace primitives (`!pt`, `!vector`, `!color`, `!reflect`)
- **Material libraries** with named references (`$materialName`)
- **External includes** for reusable material definitions
- **Polymorphic objects** (different object types in same list)

---

## File Structure

A JTrace YAML scene file has the following top-level sections:

```yaml
# Optional: Include external material libraries
!include path/to/materials.yaml

# Optional: Local material definitions
materials:
  materialName:
    # Material definition

# Required: Scene configuration
scene:
  backgroundColor: !color {r: 0.0, g: 0.0, b: 0.0}
  camera:
    # Camera configuration
  objects:
    # List of geometric objects
  lights:
    # List of light sources
```

---

## Custom YAML Tags

### `!pt` - Point3D

Represents a 3D point in space.

**Syntax:**
```yaml
!pt {x: 1.0, y: 2.0, z: 3.0}
```

**Fields:**
- `x` (double): X-coordinate
- `y` (double): Y-coordinate
- `z` (double): Z-coordinate

**Example:**
```yaml
center: !pt {x: 0.0, y: 10.0, z: -5.0}
```

---

### `!vector` - Vector3D

Represents a 3D direction vector.

**Syntax:**
```yaml
!vector {x: 0.0, y: 1.0, z: 0.0}
```

**Fields:**
- `x` (double): X-component
- `y` (double): Y-component
- `z` (double): Z-component

**Example:**
```yaml
normal: !vector {x: 0.0, y: 1.0, z: 0.0}
up: !vector {x: 0.0, y: 0.0, z: 1.0}
```

---

### `!color` - ColorRGB

Represents an RGB color with values from 0.0 to 1.0.

**Syntax:**
```yaml
!color {r: 1.0, g: 0.5, b: 0.0}
```

**Fields:**
- `r` (double): Red component (0.0 - 1.0)
- `g` (double): Green component (0.0 - 1.0)
- `b` (double): Blue component (0.0 - 1.0)

**Example:**
```yaml
color: !color {r: 1.0, g: 0.0, b: 0.0}  # Pure red
```

---

### `!reflect` - ReflectanceCoefficient

Represents light reflectance coefficients for ambient, diffuse, and specular lighting.

**Syntax:**
```yaml
!reflect {r: 0.7, g: 0.7, b: 0.7}
```

**Fields:**
- `r` (double): Red component (0.0 - 1.0)
- `g` (double): Green component (0.0 - 1.0)
- `b` (double): Blue component (0.0 - 1.0)

**Example:**
```yaml
kAmbient: !reflect {r: 0.07, g: 0.07, b: 0.07}
kDiffuse: !reflect {r: 0.7, g: 0.7, b: 0.7}
kSpecular: !reflect {r: 0.8, g: 0.8, b: 0.8}
```

---

## Material Libraries

### Defining Materials

Materials can be defined in the `materials` section with unique names:

```yaml
materials:
  redMetallic:
    color: !color {r: 1.0, g: 0.0, b: 0.0}
    kAmbient: !reflect {r: 0.07, g: 0.07, b: 0.07}
    kDiffuse: !reflect {r: 0.7, g: 0.7, b: 0.7}
    kSpecular: !reflect {r: 0.8, g: 0.8, b: 0.8}
  
  blueMatte:
    color: !color {r: 0.0, g: 0.0, b: 1.0}
    kAmbient: !reflect {r: 0.1, g: 0.1, b: 0.1}
    kDiffuse: !reflect {r: 0.9, g: 0.9, b: 0.9}
    # No kSpecular = matte material
  
  texturedFloor:
    texturePath: "textures/marble.jpg"
    kAmbient: !reflect {r: 0.07, g: 0.07, b: 0.07}
    kDiffuse: !reflect {r: 0.7, g: 0.7, b: 0.7}
```

### Material Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `color` | !color | Yes (if no texture) | Base material color |
| `texturePath` | string | No | Path to texture image file |
| `textureMapper` | string | No | Texture mapping mode: `planar` (default) or `spherical` |
| `textureScale` | double | No | Texture scaling factor |
| `kAmbient` | !reflect | Yes | Ambient light reflectance |
| `kDiffuse` | !reflect | Yes | Diffuse light reflectance |
| `kSpecular` | !reflect | No | Specular light reflectance (omit for matte) |

### Texture Mapping

JTrace supports texture mapping for materials with image-based textures. The `textureMapper` field controls how 2D texture coordinates are calculated from 3D surfaces.

#### Available Texture Mappers

| Mapper | Description |
|--------|-------------|
| `planar` (default) | Planar projection - maps texture using a plane perpendicular to the Z-axis |
| `spherical` | Spherical projection - wraps texture around objects like a sphere |

#### Example: Textured Material with Planar Mapping

```yaml
materials:
  floorMaterial:
    texturePath: "textures/checkerboard.png"
    textureMapper: "planar"
    textureScale: 1.0
    kAmbient: !reflect {r: 0.05, g: 0.05, b: 0.05}
    kDiffuse: !reflect {r: 0.8, g: 0.8, b: 0.8}
```

#### Example: Sphere with Spherical Mapping

```yaml
materials:
  earthMaterial:
    texturePath: "textures/earth.jpg"
    textureMapper: "spherical"
    kAmbient: !reflect {r: 0.1, g: 0.1, b: 0.1}
    kDiffuse: !reflect {r: 0.7, g: 0.7, b: 0.7}
```

#### Example: Material without Texture Mapping

```yaml
materials:
  simpleRed:
    color: !color {r: 1.0, g: 0.0, b: 0.0}
    kAmbient: !reflect {r: 0.1, g: 0.1, b: 0.1}
    kDiffuse: !reflect {r: 0.8, g: 0.8, b: 0.8}
```

### Referencing Materials

Use the `$` prefix to reference a named material:

```yaml
objects:
  - type: Sphere
    center: !pt {x: 0.0, y: 0.0, z: 0.0}
    radius: 10.0
    material: $redMetallic  # References the material defined above
```

---

## Include Directives

External material libraries can be included using the `!include` directive:

```yaml
!include materials/common_metals.yaml
!include materials/organic_textures.yaml

materials:
  # Additional local materials
  localMaterial:
    color: !color {r: 0.5, g: 0.5, b: 0.5}
    kAmbient: !reflect {r: 0.1, g: 0.1, b: 0.1}
    kDiffuse: !reflect {r: 0.8, g: 0.8, b: 0.8}

scene:
  # Use materials from both included files and local definitions
```

**Include Resolution:**
- Relative paths are resolved from the directory containing the YAML file
- Multiple includes are processed in order
- Later definitions override earlier ones (last wins)

---

## Scene Configuration

### Background Color

```yaml
scene:
  backgroundColor: !color {r: 0.0, g: 0.0, b: 0.0}  # Black background
```

### Camera

#### PinHoleCamera (Perspective)

```yaml
scene:
  camera:
    type: PinHoleCamera
    eye: !pt {x: 0.0, y: 0.0, z: 100.0}
    lookAt: !pt {x: 0.0, y: 0.0, z: 0.0}
    up: !vector {x: 0.0, y: 1.0, z: 0.0}
    zoomFactor: 10.0
```

#### OrthogonalCamera (Orthographic)

```yaml
scene:
  camera:
    type: OrthogonalCamera
    eye: !pt {x: 0.0, y: 0.0, z: 100.0}
    lookAt: !pt {x: 0.0, y: 0.0, z: 0.0}
    up: !vector {x: 0.0, y: 1.0, z: 0.0}
    zoomFactor: 1.0
```

**Camera Fields:**

| Field | Type | Description |
|-------|------|-------------|
| `type` | string | Camera type: `PinHoleCamera` or `OrthogonalCamera` |
| `eye` | !pt | Camera position |
| `lookAt` | !pt | Point the camera is looking at |
| `up` | !vector | Up direction vector |
| `zoomFactor` | double | Zoom level (default: 1.0) |

---

## Geometric Objects

### Sphere

```yaml
- type: Sphere
  center: !pt {x: 0.0, y: 0.0, z: 0.0}
  radius: 10.0
  material: $materialName
```

**Fields:**
- `center` (!pt): Sphere center position
- `radius` (double): Sphere radius
- `material` (string): Material reference ($name) or inline definition

### Plane

```yaml
- type: Plane
  point: !pt {x: 0.0, y: -10.0, z: 0.0}
  normal: !vector {x: 0.0, y: 1.0, z: 0.0}
  material: $materialName
```

**Fields:**
- `point` (!pt): A point on the plane
- `normal` (!vector): Plane normal vector (should be normalized)
- `material` (string): Material reference ($name) or inline definition

### Triangle

```yaml
- type: Triangle
  v1: !pt {x: 0.0, y: 0.0, z: 0.0}
  v2: !pt {x: 10.0, y: 0.0, z: 0.0}
  v3: !pt {x: 5.0, y: 10.0, z: 0.0}
  normal: !vector {x: 0.0, y: 0.0, z: 1.0}  # Optional
  material: $materialName
```

**Fields:**
- `v1`, `v2`, `v3` (!pt): Triangle vertices
- `normal` (!vector): Optional explicit normal
- `material` (string): Material reference ($name) or inline definition

---

## Lights

### PointLight

Constant intensity light.

```yaml
- type: PointLight
  position: !pt {x: 0.0, y: 20.0, z: -10.0}
  color: !color {r: 1.0, g: 1.0, b: 1.0}
```

### DecayingPointLight

Light with intensity that decays with distance squared.

```yaml
- type: DecayingPointLight
  position: !pt {x: 0.0, y: 20.0, z: -10.0}
  color: !color {r: 1.0, g: 1.0, b: 1.0}
  initialIntensity: 100.0
```

**Light Fields:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `type` | string | Yes | `PointLight` or `DecayingPointLight` |
| `position` | !pt | Yes | Light position |
| `color` | !color | No | Light color (default: white) |
| `initialIntensity` | double | Decaying only | Initial light intensity |

---

## Tracer Configuration

The `tracer` section defines how rays are traced and shaded.

```yaml
tracer:
  type: Tracer  # or MultiThreadTracer
  threads: 4     # Only for MultiThreadTracer
  shaders:
    - type: AmbientShader
    - type: DiffuseShader
    - type: SpecularShader
      specularFactor: 4.0
  listeners:
    - type: ImageListener
      fileName: output.png
      format: png
    - type: TimeListener
  interceptors:
    - type: ShadowInterceptor
```

### Tracer Types

| Type | Description |
|------|-------------|
| `Tracer` | Single-threaded ray tracer |
| `MultiThreadTracer` | Multi-threaded ray tracer |

**MultiThreadTracer Fields:**
- `threads` (int): Number of threads (optional, defaults to available processors)

---

## Shaders

Shaders define how surfaces are shaded. Multiple shaders can be combined.

### Available Shaders

#### AmbientShader

Basic ambient lighting. No additional fields required.

```yaml
- type: AmbientShader
```

#### DiffuseShader

Lambertian diffuse shading. No additional fields required.

```yaml
- type: DiffuseShader
```

#### SpecularShader

Phong specular highlighting.

```yaml
- type: SpecularShader
  specularFactor: 64.0  # Shininess factor (higher = sharper highlights)
```

**SpecularShader Fields:**
- `specularFactor` (double): Shininess exponent (default: 1.0)

---

## Listeners

Listeners respond to tracer events like rendering progress and completion.

### Available Listeners

#### ImageListener

Saves the rendered image to a file.

```yaml
- type: ImageListener
  fileName: output.png
  format: png
```

**ImageListener Fields:**
- `fileName` (string): Output file path
- `format` (string): Image format (png, jpg, bmp, gif)

#### TimeListener

Prints rendering time to console.

```yaml
- type: TimeListener
```

---

## Interceptors

Interceptors modify the shading process.

### Available Interceptors

#### ShadowInterceptor

Enables shadow casting by checking if light is occluded.

```yaml
- type: ShadowInterceptor
```

---

## ViewPlane

Defines the rendering resolution.

```yaml
viewPlane:
  hres: 1920
  vres: 1080
```

**ViewPlane Fields:**
- `hres` (int): Horizontal resolution (width)
- `vres` (int): Vertical resolution (height)

---

## Complete Example

```yaml
# Include common materials
!include materials/metals.yaml

# Define local materials
materials:
  floorMaterial:
    texturePath: "textures/checkerboard.png"
    textureMapper: "planar"
    textureScale: 1.0
    kAmbient: !reflect {r: 0.05, g: 0.05, b: 0.05}
    kDiffuse: !reflect {r: 0.8, g: 0.8, b: 0.8}
    kSpecular: !reflect {r: 0.1, g: 0.1, b: 0.1}

# Scene configuration
scene:
  backgroundColor: !color {r: 0.1, g: 0.1, b: 0.2}
  
  camera:
    type: PinHoleCamera
    eye: !pt {x: 0.0, y: 5.0, z: 50.0}
    lookAt: !pt {x: 0.0, y: 0.0, z: 0.0}
    up: !vector {x: 0.0, y: 1.0, z: 0.0}
    zoomFactor: 8.0
  
  objects:
    # Sphere using material from included file
    - type: Sphere
      center: !pt {x: 0.0, y: 0.0, z: 0.0}
      radius: 10.0
      material: $goldMetallic
    
    # Sphere using local material
    - type: Sphere
      center: !pt {x: 25.0, y: 0.0, z: 0.0}
      radius: 10.0
      material: $silverMetallic
    
    # Floor plane with texture
    - type: Plane
      point: !pt {x: 0.0, y: -10.1, z: 0.0}
      normal: !vector {x: 0.0, y: 1.0, z: 0.0}
      material: $floorMaterial
  
  lights:
    - type: PointLight
      position: !pt {x: 50.0, y: 50.0, z: -50.0}
      color: !color {r: 1.0, g: 1.0, b: 0.9}
    
    - type: DecayingPointLight
      position: !pt {x: -30.0, y: 20.0, z: 20.0}
      color: !color {r: 0.8, g: 0.8, b: 1.0}
      initialIntensity: 80.0

# Tracer configuration
tracer:
  type: Tracer
  shaders:
    - type: AmbientShader
    - type: DiffuseShader
    - type: SpecularShader
      specularFactor: 64.0
  listeners:
    - type: ImageListener
      fileName: output.png
      format: png
    - type: TimeListener
  interceptors:
    - type: ShadowInterceptor

# ViewPlane resolution
viewPlane:
  hres: 1920
  vres: 1080
```

---

## Loading Scenes in Java

```java
import org.jtrace.*;
import org.jtrace.io.yaml.SceneYamlIO;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SceneLoader {
    public static void main(String[] args) throws Exception {
        SceneYamlIO yamlIO = new SceneYamlIO();
        
        // Load all configuration (scene, tracer, viewPlane)
        Path sceneFile = Paths.get("myscene.yaml");
        SceneYamlIO.SceneConfiguration config = yamlIO.loadConfiguration(sceneFile);
        
        Scene scene = config.getScene();
        Tracer tracer = config.getTracer();
        ViewPlane viewPlane = config.getViewPlane();
        
        // Or load individually
        // Scene scene = yamlIO.load(sceneFile);
        // Tracer tracer = yamlIO.loadTracer(sceneFile);
        // ViewPlane viewPlane = yamlIO.loadViewPlane(sceneFile);
        
        // Render
        tracer.render(scene, viewPlane);
    }
}
```

---

## Best Practices

1. **Use material libraries** for complex scenes to avoid repetition
2. **Group related materials** in separate files and use `!include`
3. **Use relative paths** for textures and includes
4. **Normalize vectors** for normals and directions
5. **Keep color values** in range [0.0, 1.0]
6. **Use meaningful names** for materials (e.g., `redMetallic` not `material1`)
7. **Organize scenes** in folders with materials/ and textures/ subdirectories

---

## Validation

The YAML parser will validate:
- Required fields are present
- Tag syntax is correct
- Material references exist
- Numeric values are valid

Runtime errors will occur if:
- Texture files are not found
- Material references are unresolved
- Invalid object configurations are provided
