package org.jtrace.listeners;

import org.jtrace.TracerListener;
import org.jtrace.ViewPlane;
import org.jtrace.primitives.ColorRGB;

public class TimeListener implements TracerListener {
	
	long startTime;

	@Override
	public void start(ViewPlane viewPlane) {
		startTime = System.currentTimeMillis();
	}

	@Override
	public void afterTrace(ColorRGB color, int x, int y) {
		
	}

	@Override
	public void finish() {
		long finishTime = System.currentTimeMillis();
		long duration = finishTime - startTime;
		
		System.out.println("Finished in " + duration + "ms");
	}

}
