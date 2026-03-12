package org.jtrace.tracer;

import org.jtrace.Jay;
import org.jtrace.Scene;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Vector3D;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskTracer extends Tracer {

  private int maxDepth = 3;

  @Override
  public void render(Scene scene, ViewPlane viewPlane) {
    final int hres = viewPlane.getHres();
    final int vres = viewPlane.getVres();
    final Camera camera = scene.getCamera();

    fireStart(viewPlane);
    initInterceptors(scene);

    List<TracerTask> tasks = new ArrayList<>();

    for (int row = 0; row < vres; row++) {
      for (int column = 0; column < hres; column++) {
        final Jay jay = camera.createJay(row, column, vres, hres);
        tasks.add(new TracerTask(row, column, 1, jay, scene, null));
      }
    }

    System.out.println("[" + new Date() + "] Casting " + tasks.size() + " primary rays...");
    List<TracerTaskResult> firstGenResults = tasks.parallelStream()
                                                  .map(this::cast)
                                                  .toList();

    List<TracerTaskResult> accumulator = new ArrayList<>(firstGenResults.size() * maxDepth); // hmmmm
    accumulator.addAll(firstGenResults);

    List<TracerTaskResult> currentGenResults = firstGenResults;
    for (int gen = 2; gen <= maxDepth; gen++) {
      System.out.println("[" + new Date() + "] Casting generation " + gen + " rays...");
      List<TracerTaskResult> nextGenResults = currentGenResults.parallelStream()
                                                               .filter(r -> r.hit().isHit() && r.hit().getObject().getMaterial().isReflective())
                                                               .map(TracerTaskResult::createNextGenTask)
                                                               .map(this::cast)
                                                               .toList();

      accumulator.addAll(nextGenResults);
      currentGenResults = nextGenResults;
    }

    System.out.println("[" + new Date() + "] Shading " + accumulator.size() + " hits...");
    List<ShadeTaskResult> shadeTaskResults = accumulator.parallelStream()
                                                        .filter(r -> r.hit().isHit())
                                                        .map(ShadeTask::new)
                                                        .map(this::shade)
                                                        .toList();


    System.out.println("[" + new Date() + "] Combining " + shadeTaskResults.size() + " rays...");
    ColorRGB[][] colors = new ColorRGB[vres][hres];
    for (ShadeTaskResult r : shadeTaskResults) {
      var row = r.task().row;
      var column = r.task().column;

      if (colors[row][column] == null) {
        colors[row][column] = r.color;
      } else {
        colors[row][column] = colors[row][column].add(r.color);
      }
    }

    System.out.println("[" + new Date() + "] painting " + vres * hres + " pixels...");
    for (int row = 0; row < vres; row++) {
      for (int column = 0; column < hres; column++) {
        if (colors[row][column] == null) {
          colors[row][column] = scene.getBackgroundColor();
        }
        fireAfterTrace(colors[row][column], column, row);
      }
    }

    fireFinish();
    System.out.println("[" + new Date() + "] Done!");
  }

  private TracerTaskResult cast(TracerTask task) {
    Hit hit = super.cast(task.scene(), task.jay());
    return new TracerTaskResult(task, hit);
  }

  private ShadeTaskResult shade(ShadeTask task) {
    ColorRGB color = super.shade(task.scene, task.jay, task.hit);

    if (task.depth == 1) {
      return new ShadeTaskResult(task, color);
    }

    var colorContribution = 1d / (task.depth * task.depth);

    var objectMaterial = task.reflectedParent.getMaterial();
    color = new ColorRGB(
      color.getRed()   * objectMaterial.getkReflect().getRed()   * colorContribution,
      color.getGreen() * objectMaterial.getkReflect().getGreen() * colorContribution,
      color.getBlue()  * objectMaterial.getkReflect().getBlue()  * colorContribution
    );

    return new ShadeTaskResult(task, color);
  }

  public int getMaxDepth() {
    return maxDepth;
  }

  public void setMaxDepth(int maxDepth) {
    this.maxDepth = maxDepth;
  }

  record TracerTask(int row, int column, int depth, Jay jay, Scene scene, TracerTaskResult parent) {}

  record TracerTaskResult(TracerTask task, Hit hit) {
    TracerTask createNextGenTask() {
      return new TracerTask(task.row, task.column, task.depth + 1, reflectJay(), task.scene, this);
    }

    Jay reflectJay() {
      Vector3D incident = task.jay().getDirection().normal();
      Vector3D normal = hit.getNormal();

      Vector3D reflectDirection = incident.subtract(normal.multiply(2 * incident.dot(normal)));

      return new Jay(hit.getPoint().add(reflectDirection.normal()), reflectDirection);
    }
  }

  class ShadeTask {
    private final Hit hit;
    private final int row;
    private final int column;
    private final Jay jay;
    private final Scene scene;
    private final int depth;
    private final TracerTaskResult generator;
    private final GeometricObject reflectedParent;

    ShadeTask(TracerTaskResult traceResult) {
      this.generator = traceResult;
      this.row = traceResult.task().row();
      this.column = traceResult.task().column();
      this.jay = traceResult.task().jay();
      this.scene = traceResult.task().scene();
      this.hit = traceResult.hit();
      this.depth = traceResult.task().depth();
      this.reflectedParent = traceResult.task().depth() > 1 ? traceResult.task().parent().hit.getObject() : null;
    }
  }

  record ShadeTaskResult(ShadeTask task, ColorRGB color) {}

}