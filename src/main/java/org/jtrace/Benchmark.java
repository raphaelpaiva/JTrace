package org.jtrace;

import java.util.Random;

import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Quadrilateral;
import org.jtrace.geometry.Sphere;
import org.jtrace.geometry.Triangle;
import org.jtrace.interceptor.ShadowInterceptor;
import org.jtrace.lights.DecayingPointLight;
import org.jtrace.lights.PointLight;
import org.jtrace.material.Material;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;
import org.jtrace.shader.AmbientShader;
import org.jtrace.shader.DiffuseShader;
import org.jtrace.shader.SpecularShader;
import org.jtrace.tracer.Tracer;

public class Benchmark {

    private static final int ITERATIONS = 10;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 150;
    private static final int OBJECT_COUNT = 100;
    private static final int LIGHT_COUNT = 10;
    private static final int SPHERES = 25;
    private static final int PLANES = 25;
    private static final int TRIANGLES = 25;
    private static final int QUADS = 25;
    private static final int POINT_LIGHTS = 5;
    private static final int DECAYING_LIGHTS = 5;

    public static void main(String[] args) {
        System.out.println("=== JTrace Benchmark ===");
        System.out.println("Resolution: " + WIDTH + "x" + HEIGHT);
        System.out.println("Pixels: " + (WIDTH * HEIGHT));
        System.out.println("Objects: " + OBJECT_COUNT);
        System.out.println("Lights: " + LIGHT_COUNT);
        System.out.println("Iterations: " + ITERATIONS);
        System.out.println();

        Scene scene = createBenchmarkScene();
        Tracer tracer = createTracer();
        ViewPlane viewPlane = new ViewPlane(WIDTH, HEIGHT);

        System.out.println("Warming up...");
        tracer.render(scene, viewPlane);
        System.out.println("Warmup complete.\n");

        System.out.println("Running benchmark...");
        long[] times = new long[ITERATIONS];
        for (int i = 0; i < ITERATIONS; i++) {
            long start = System.nanoTime();
            tracer.render(scene, viewPlane);
            times[i] = System.nanoTime() - start;
        }

        printStatistics(times, WIDTH * HEIGHT);
    }

    private static Scene createBenchmarkScene() {
        Scene scene = new Scene();
        Random random = new Random(42);

        Material redMat = createMaterial(ColorRGB.RED);
        Material greenMat = createMaterial(ColorRGB.GREEN);
        Material blueMat = createMaterial(ColorRGB.BLUE);
        Material yellowMat = createMaterial(new ColorRGB(1, 1, 0));
        Material cyanMat = createMaterial(new ColorRGB(0, 1, 1));
        Material magentaMat = createMaterial(new ColorRGB(1, 0, 1));
        Material whiteMat = createMaterial(ColorRGB.WHITE);
        Material orangeMat = createMaterial(new ColorRGB(1, 0.5, 0));

        Material[] materials = { redMat, greenMat, blueMat, yellowMat, cyanMat, magentaMat, whiteMat, orangeMat };

        for (int i = 0; i < SPHERES; i++) {
            Point3D center = new Point3D(
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100
            );
            double radius = 5 + random.nextDouble() * 15;
            Sphere sphere = new Sphere(center, radius, materials[i % materials.length]);
            scene.add(sphere);
        }

        for (int i = 0; i < PLANES; i++) {
            Point3D point = new Point3D(
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100
            );
            Vector3D normal = new Vector3D(
                random.nextDouble() * 2 - 1,
                random.nextDouble() * 2 - 1,
                random.nextDouble() * 2 - 1
            ).normal();
            Plane plane = new Plane(point, normal, materials[i % materials.length]);
            scene.add(plane);
        }

        for (int i = 0; i < TRIANGLES; i++) {
            Point3D v1 = new Point3D(
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100
            );
            Point3D v2 = new Point3D(
                v1.getX() + random.nextDouble() * 20,
                v1.getY() + random.nextDouble() * 20,
                v1.getZ() + random.nextDouble() * 20
            );
            Point3D v3 = new Point3D(
                v1.getX() - random.nextDouble() * 20,
                v1.getY() + random.nextDouble() * 20,
                v1.getZ() - random.nextDouble() * 20
            );
            Triangle triangle = new Triangle(v1, v2, v3, materials[i % materials.length]);
            scene.add(triangle);
        }

        for (int i = 0; i < QUADS; i++) {
            Point3D p0 = new Point3D(
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100
            );
            Point3D p1 = new Point3D(
                p0.getX() + random.nextDouble() * 20,
                p0.getY(),
                p0.getZ()
            );
            Point3D p2 = new Point3D(
                p0.getX() + random.nextDouble() * 20,
                p0.getY() + random.nextDouble() * 20,
                p0.getZ() + random.nextDouble() * 20
            );
            Point3D p3 = new Point3D(
                p0.getX(),
                p0.getY() + random.nextDouble() * 20,
                p0.getZ() + random.nextDouble() * 20
            );
            Quadrilateral quad = new Quadrilateral(p0, p1, p2, p3, materials[i % materials.length]);
            scene.add(quad);
        }

        for (int i = 0; i < POINT_LIGHTS; i++) {
            PointLight light = new PointLight(
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100,
                ColorRGB.WHITE
            );
            scene.add(light);
        }

        for (int i = 0; i < DECAYING_LIGHTS; i++) {
            DecayingPointLight light = new DecayingPointLight(
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100,
                random.nextDouble() * 200 - 100,
                100.0
            );
            scene.add(light);
        }

        PinHoleCamera camera = new PinHoleCamera(
            new Point3D(0, 0, 200),
            new Point3D(0, 0, 0),
            new Vector3D(0, 1, 0)
        );
        scene.setCamera(camera);

        scene.withBackground(ColorRGB.BLACK);

        return scene;
    }

    private static Tracer createTracer() {
        Tracer tracer = new Tracer();
        tracer.addShaders(new AmbientShader(), new DiffuseShader(), new SpecularShader(32.0));
        tracer.addInterceptors(new ShadowInterceptor());
        return tracer;
    }

    private static Material createMaterial(ColorRGB color) {
        ReflectanceCoefficient kAmbient = new ReflectanceCoefficient(0.1, 0.1, 0.1);
        ReflectanceCoefficient kDiffuse = new ReflectanceCoefficient(0.6, 0.6, 0.6);
        ReflectanceCoefficient kSpecular = new ReflectanceCoefficient(0.5, 0.5, 0.5);
        return new Material(color, kAmbient, kDiffuse, kSpecular);
    }

    private static void printStatistics(long[] times, int pixelCount) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;

        for (long time : times) {
            if (time < min) min = time;
            if (time > max) max = time;
            sum += time;
        }

        double mean = (double) sum / times.length;

        double variance = 0;
        for (long time : times) {
            variance += (time - mean) * (time - mean);
        }
        variance /= times.length;
        double stdDev = Math.sqrt(variance);

        long[] sorted = times.clone();
        java.util.Arrays.sort(sorted);
        long median = sorted[sorted.length / 2];

        double minMs = min / 1_000_000.0;
        double maxMs = max / 1_000_000.0;
        double meanMs = mean / 1_000_000.0;
        double medianMs = median / 1_000_000.0;
        double stdDevMs = stdDev / 1_000_000.0;

        double totalRays = (double) pixelCount * ITERATIONS;
        double raysPerSecond = totalRays / (mean / 1_000_000_000.0);

        System.out.println("=== Benchmark Results ===");
        System.out.println();
        System.out.println("Timing (milliseconds):");
        System.out.printf("  Min:    %.2f ms%n", minMs);
        System.out.printf("  Max:    %.2f ms%n", maxMs);
        System.out.printf("  Mean:   %.2f ms%n", meanMs);
        System.out.printf("  Median: %.2f ms%n", medianMs);
        System.out.printf("  StdDev: %.2f ms%n", stdDevMs);
        System.out.println();
        System.out.printf("Throughput: %.0f rays/second%n", raysPerSecond);
    }
}
