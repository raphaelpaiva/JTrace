package org.jtrace.listeners;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jtrace.TracerListener;
import org.jtrace.ViewPlane;
import org.jtrace.primitives.ColorRGB;

public class ImageListener implements TracerListener {

	private BufferedImage image;
	private String fileName;
	private String format;
	
	public ImageListener(String fileName, String format) {
		this.fileName = fileName;
		this.format = format;
	}

	@Override
	public void start(ViewPlane viewPlane) {
		image = new BufferedImage(viewPlane.getHres(), viewPlane.getVres(), BufferedImage.TYPE_INT_RGB);
	}

	@Override
	public void afterTrace(ColorRGB color, int x, int y) {
		image.setRGB(x, y, color.toInt());
	}

	@Override
	public void finish() {
		try {
			ImageIO.write(image, format, new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
