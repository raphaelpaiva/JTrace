package org.jtrace;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jtrace.cameras.Camera;
import org.jtrace.primitives.ColorRGB;

public class MultiThreadTracer extends Tracer {

    private ThreadPoolExecutor executor;

    private Semaphore finishSemaphore = new Semaphore(1);
    private AtomicInteger pixelsPainted = new AtomicInteger(0);
    private int totalPixels = 0;

    public MultiThreadTracer()
    {
        this(Runtime.getRuntime().availableProcessors());
    }

    public MultiThreadTracer(int threads)
    {
        executor = new ThreadPoolExecutor(threads, threads,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(100),
                new ThreadPoolExecutor.CallerRunsPolicy());

        executor.prestartAllCoreThreads();
    }

    @Override
    public void render(Scene scene, ViewPlane viewPlane) {
        final int hres = viewPlane.getHres();
        final int vres = viewPlane.getVres();
        final Camera camera = scene.getCamera();

        totalPixels = vres * hres;

        fireStart(viewPlane);
        initInterceptors(scene);

        try {
            finishSemaphore.acquire();

            for (int r = 0; r < vres; r++) {
                for (int c = 0; c < hres; c++) {
                    final Jay jay = camera.createJay(r, c, vres, hres);

                    executor.submit(new TraceRunnable(c, r, scene, jay));
                }
            }

            finishSemaphore.acquire();
        } catch (InterruptedException e) {

        }

        fireFinish();

        executor.shutdown();
    }

    @Override
    protected void fireAfterTrace(ColorRGB color, int c, int r) {
        super.fireAfterTrace(color, c, r);

        if (pixelsPainted.incrementAndGet() == totalPixels) {
            finishSemaphore.release();
        }
    }

    private class TraceRunnable implements Runnable	{
        private final Scene scene;
        private final Jay jay;
        private final int r;
        private final int c;

        public TraceRunnable(int c, int r, Scene scene, Jay jay) {
            this.scene = scene;
            this.jay = jay;
            this.c = c;
            this.r = r;
        }

        @Override
        public void run() {
            final ColorRGB color = trace(scene, jay);

            fireAfterTrace(color, c, r);
        }
    }
}
