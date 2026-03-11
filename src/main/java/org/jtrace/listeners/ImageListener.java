package org.jtrace.listeners;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jtrace.Scene;
import org.jtrace.TracerListener;
import org.jtrace.ViewPlane;
import org.jtrace.primitives.ColorRGB;

/**
 * A {@link TracerListener} that creates an image from the {@link Scene}
 * rendered and writes it to disk.
 * 
 */
public class ImageListener implements TracerListener {

	private transient BufferedImage image;
	private String fileName;
	private String format;

	public ImageListener() {
		this.fileName = "output.png";
		this.format = "png";
	}

	/**
	 * Creates a {@link ImageListener}.
	 * 
	 * @see BufferedImage
	 * 
	 * @param fileName the path to write the image to.
	 * @param format the image format.
	 */
	public ImageListener(String fileName, String format) {
		this.fileName = fileName;
		this.format = format;
	}

	@Override
	public void start(ViewPlane viewPlane) {
		image = new BufferedImage(viewPlane.getHres(), viewPlane.getVres(),
				BufferedImage.TYPE_INT_RGB);
	}

	@Override
	public void afterTrace(ColorRGB color, int x, int y) {
		image.setRGB(x, image.getHeight() - 1 - y, color.toInt());
	}

	@Override
	public void finish() {
		try {
			ImageIO.write(image, format, new File(fileName));
      System.out.println("Wrote " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
