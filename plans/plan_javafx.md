Plan: JavaFX-based Blender-like UI for Ray Tracing project

Overview
- Build a modern, single-window, Blender-inspired UI using JavaFX (cross‑platform, macOS friendly).
- Plan to implement dynamic type discovery, a dark UI, and persistence in JSON.
- Main panes: left Editors area (Scene Graph Editor, Tracer settings, Camera settings, Material library), right Render panel, bottom Control bar.

Key decisions
- Tech: JavaFX; dynamic discovery via classpath scanning at startup.
- UI binding: MVVM-ish with observable properties; UI driven by reflection of domain objects.
- Persistence: JSON-based save/load for scene, materials, camera, and tracer configuration.
- Cross‑platform: macOS first; works on Windows/Linux thanks to JavaFX.

High-level architecture
- Domain model (existing): Scene, GeometricObject, Light, Camera, Material, ViewPlane, Tracer, etc.
- Discovery layer: ClasspathScanner scans for subclasses of GeometricObject/Light, Camera, Tracer components, and Material-related wrappers.
- UI layer: JavaFX views bound to domain models; a single main window with dockable-like panels (collapsible sections).
- Render integration: a Renderer service calling into the engine; render results shown in the Render panel and can be saved.

Plan highlights by component
- Scene Graph Editor
  - TreeView of objects; add/remove with dialogs; double-click to edit; Delete key to remove.
  - Add dialog: first pick object type, then a dynamic editor updates to display required properties (including Vector3, Points, and Material reference from Library).
- Tracer Settings panel
  - TreeView of components (TracerListener, TracerInterceptor, Shader, ViewPlane).
  - Dynamic add/edit dialogs; ViewPlane required.
- Camera Settings panel
  - Simple form: Type (Camera subclass), Eye, LookAt, Up, Zoom.
- Material editor / library
  - TreeView of materials; add/edit dialogs for Color, kAmbient, kDiffuse, kSpecular, Texture, and material name.
- Render panel
  - Displays rendered image; supports Save Image.
- Control Bar
  - Render button, Auto-render on change checkbox, Save Image button.

Dynamic type discovery and property binding
- Use a lightweight reflection-based approach:
  - Discover concrete classes for base types via ClasspathScanner.
  - Expose properties for UI via a PropertyDescriptor-like mechanism (name, type, default, constraints).
- Primitive properties map to controls (int/float -> Spinner/TextField with validation; string -> TextField).
- Complex properties (points, vectors, colors) render as nested editors or dialogs.
- Complex objects like Material are selected from a Material Library dropdown.

Persistence schema (JSON)
- scene.json: { camera, backgroundColor, objects: [{ type, name, materialName, properties }], lights }
- materials.json: [{ name, color, kAmbient, kDiffuse, kSpecular, texturePath }]
- tracer.json: { components: [{ type, properties }] }
- camera.json: { cameraType, eye, lookAt, up, zoom }

First MVP scope (phases)
- Phase 1 (2 weeks): core window, left editors with Scene Graph and Camera, render panel placeholder, persistence scaffolding, basic add/edit for Sphere and Plane.
- Phase 2 (3 weeks): Tracer settings, Materials library, dynamic dialogs for more properties, edit flows, keyboard shortcuts.
- Phase 3 (3 weeks): Finish all object types, ViewPlane enforcement, render integration, Save Image, polish, tests.

File layout (initial)
- plans/plan_javafx.md  (this file)
- src/main/java/org/jtrace/editor/MonolithicEditor.java  (implementation skeleton in a single file)
- plans/plan_electron_cef.md  (see second plan for Electron/CEF approach)

Notes
- This plan targets a single, maintainable monolith in JavaFX for the initial MVP. The AI agent can progressively refine the code and add more object types.
- Cross-platform readiness is ensured by JavaFX. macOS UI conventions will be respected.
