package org.jtrace.examples;

import org.jtrace.Materials;
import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Sphere;
import org.jtrace.lights.Light;
import org.jtrace.lights.PointLight;
import org.jtrace.listeners.ImageListener;
import org.jtrace.listeners.TimeListener;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.Shaders;

public class IlluminationExample {
  public static void main(String[] args) {
    Scene scene = createScene();
    
    Tracer tracer = new Tracer();
    tracer.addListeners(new ImageListener("illumination.png", "png"), new TimeListener());
    
    tracer.addShaders(Shaders.ambientShader(), Shaders.diffuseShader(), Shaders.specularShader(4));
    
    tracer.render(scene, new ViewPlane(1024, 768));
  }
  
  private static Scene createScene() {
    Sphere red = new Sphere(new Point3D(0, 0, -10), 10, Materials.metallic(ColorRGB.RED));
    Plane green = new Plane(new Point3D(0, -10, 0), new Vector3D(0, 1, 0), Materials.metallic(ColorRGB.BLUE));
    
    Light light = new PointLight(-30, 30, 30);
    
    Camera camera = new PinHoleCamera(new Point3D(0, 0, 30), new Point3D(0, 0, 0), Vector3D.UNIT_Y);
    
    camera.setZoomFactor(10);
    
    Scene scene = new Scene().add(red, green).add(light).setCamera(camera);
    
    return scene;
  }
  
}
