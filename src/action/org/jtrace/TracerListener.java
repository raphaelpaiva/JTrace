package org.jtrace;

import org.jtrace.primitives.ColorRGB;

public interface TracerListener {
	public void start(ViewPlane viewPlane);
	public void afterTrace(ColorRGB color, int x, int y);
	public void finish();
}
