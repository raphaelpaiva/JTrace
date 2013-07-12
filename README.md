JTrace - A RayTrace Engine for Java
===================================

This project aims to create a extensible RayTrace Engine for the Java Programming Language.

Getting Started
---------------
1. Download the latest jtrace.zip file in [releases section][4].
2. Add the jtrace.jar and all the jars contained in the dependencies folder inside jtrace.zip to the classpath of your applcation.
3. You can find the source code in jtrace-source.jar file and the javadocs in jtrace-javadoc.jar.
4. Take a look at the examples in the examples folder inside jtrace-source.jar.
5. In order to use the swing components, you'll need to add the jtrace-swing.jar to your application's classpath as well.
6. Happy Rendering! =)

Contributing
------------
JTrace is open source software! Feel free to contribute!

Fork it, code it and submit a pull request!

Found a bug? Have a feature request? Suggestions on improving the code/build? [Open a issue][5] and we'll be happy to take a look at it! =)

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
  * Triangle;
  * Quadrilateral.

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
[4]: https://github.com/raphaelpaiva/JTrace/releases
[5]: https://github.com/raphaelpaiva/JTrace/issues
