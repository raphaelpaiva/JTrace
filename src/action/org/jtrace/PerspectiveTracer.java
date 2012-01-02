package org.jtrace;

import org.jtrace.cameras.Camera;
import org.jtrace.primitives.ColorRGB;

public class PerspectiveTracer extends Tracer {

    public PerspectiveTracer() {
        super();
    }

    @Override
    public void render(final Scene scene, final ViewPlane viewPlane) {
        final int hres = viewPlane.getHres();
        final int vres = viewPlane.getVres();
        final Camera camera = scene.getCamera();

        fireStart(viewPlane);

        for (int r = 0; r < vres; r++) {
            for (int c = 0; c < hres; c++) {
                final Jay jay = camera.createJay(r, c, vres, hres);

                final ColorRGB color = cast(scene, jay);

                fireAfterTrace(color, c, r);
            }
        }

        fireFinish();
    }

}
