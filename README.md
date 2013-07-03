JTrace - A RayTrace Engine for Java
===================================

This project aims to create a extensible RayTrace Engine for the Java Programming Language.

Features
--------

### Mathematic Primitives
  * Point;
  * Vector;
  * Color;
  * Along with its operations.

### Geometry Primitives
  * Sphere;
  * Plane;
  * Triangle.

### Camera
  * Extensible Camera model: easy to create new camera types;
  * JTrace provides some out of the box cameras:
    - Pin hole Camera;
    - Orthogonal Camera;

### Light
  * Colored Point light.

### Shadows
  * Hard shadows.

### Illumination
  * Direct Illumination.

### Shading
  * Pluggable and extensible Shader model: just add pre-made or custom shaders to the Tracer;
  * JTrace provides some out of the box shaders:
    - Ambient Shader
    - Diffuse Shader
    - Specular Shader

### Materials
  * Colored materials with Diffuse, Ambient and specular coefficients;
  * Texturized materials.

### Tracer Listener Model
  * All external interaction is done by listeners, such as:
    - Image file writing;
    - On screen Plotting;
    - Time tracking.
  * JTrace provides the following out of the box listeners:
    - Time listener;
    - Image listener;
    - Swing listener.

### Optimization
  * Multi-threading;
    - Via MultiThreadTracer;

### IO
  * Triangle meshes:
    - Support to ply file loading.
      - Using [jPLY][3].

Work in Progress
----------------

### Scene IO
  * Scene description file in a custom [YAML][2] format
  * Needs to read/dump all scene components;
    - Currently only mathematic primitives are readable/writable;

Future work (TODO)
------------------

### Geometry Primitives
  * Rectangle;
  * Disk;
  * Generic open Cylinder;
  * Generic Torus.

### Object
  * Composite objects;

### Light
  * Spot Light;

### Material
  * Reflexive and Refractive materials;

### Tracer
  * Recursion (Reflection);

### Optimization
  * Intersection acceleration:
    - Bounding boxes;
    - KD-Tree;

General Assumptions
-------------------

### World Axes Orientation
  * X Axis grows from left to right of the screen;
  * Y Axis grows from the bottom to the top of the screen;
  * Z Axis grows from the inside to the outside of the screen.


References
----------

  * Kevin Suffern's [RayTracing from the Ground Up][1]

[1]: http://www.raytracegroundup.com/
[2]: http://www.yaml.org/
[3]: https://github.com/smurn/jPLY
