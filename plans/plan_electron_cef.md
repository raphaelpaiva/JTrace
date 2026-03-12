Plan: Electron/CEF-based Web UI for Ray Tracing project

Overview
- Build a modern, Blender-inspired UI using a web stack (Electron) for cross‑platform desktop experience.
- The UI communicates with the existing Rust/Java engine (or Java-based back end) via a lightweight IPC layer (WebSocket or gRPC).
- Main panes: left Editors area (Scene Graph Editor, Tracer settings, Camera settings, Material library), right Render panel, bottom Control bar.

Key decisions
- Tech: Electron + React (or plain Web Components) for UI; back-end engine exposed via IPC.
- UI binding: The UI drives a JSON-like data model that is passed to the engine for rendering, and engine emits render results or state changes back.
- Persistence: JSON-based save/load for scene, materials, camera, tracer; render results as image files.
- Cross‑platform: macOS, Windows, Linux supported by Electron.

High-level architecture
- UI layer: Electron app with a single window; left editor panels with collapsible sections; render area on right; bottom toolbar.
- Engine bridge: IPC channel (WebSocket or gRPC) to send/receive scene data, materials, camera, tracer configurations, and render results.
- Persistence: JSON files on disk to store scene.json, materials.json, tracer.json, camera.json.
- Discovery: At runtime, UI queries the engine for available concrete classes via a dynamic discovery protocol exposed by the engine; UI then renders type selectors.

Plan highlights by component
- Scene Graph Editor
  - TreeView in the UI; Add/Remove with dialogs; double-click to edit; Delete key support.
  - Add dialog: select object type, then a dynamic form updates to show required fields; material chosen from Library;
- Tracer Settings panel
  - TreeView of components; dynamic add/edit dialogs; ViewPlane is required.
- Camera Settings panel
  - Form with Camera Type, Eye, LookAt, Up, Zoom.
- Material library editor
  - CRUD for materials with Color, Ambient, Diffuse, Specular, Texture, Name; re-use Material library in engine.
- Render panel
  - Render results displayed as image; may be streamed from engine as tiles or full image; support Save Image.
- Control Bar
  - Render button; Auto-render toggle; Save Image button.

Dynamic type discovery and property binding
- UI queries engine for available classes using a plugin-like discovery API.
- UI uses a PropertyDescriptor-like pattern to render editors for primitive types and nested editors for complex types.
- Material selection is wired to the engine's Material library; new materials created via a dialog are saved back to disk and exposed to the scene.

Persistence schema (JSON)
- scene.json, materials.json, tracer.json, camera.json, and lastRender.png (optional).
- Versioning for migrations.

First MVP scope (phases)
- Phase 1 (2 weeks): Basic Electron app scaffold, left editors, render panel placeholder, JSON persistence, and Sphere/Plane object add/edit flows via a simple dynamic form.
- Phase 2 (3 weeks): Tracer and Material editors, dynamic property dialogs, extra object types, and keyboard shortcuts.
- Phase 3 (3 weeks): Render integration, auto-render feature, Save Image, polish, and cross-platform packaging.

File layout (initial)
- plans/plan_javafx.md  (JavaFX plan for cross-reference)
- plans/plan_electron_cef.md  (Electron/CEF plan)
- app/ (Electron source; UI frontend and bridge)
- backend/ (engine bridge and persistence layer in JS/TS or native)

Notes
- This plan targets a web-based UI that can run on macOS with Electron; it can be ported to a native bridge if preferred.
- Cross-platform is supported by Electron; performance and memory footprint considerations need to be watched during MVP.
