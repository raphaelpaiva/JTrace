package org.jtrace.swing;

import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import org.jtrace.TracerListener;
import org.jtrace.ViewPlane;
import org.jtrace.primitives.ColorRGB;

public class SwingListener implements TracerListener {

  private TracerPanel panel;
  
  private int paintedPixels = 0;
  
  public SwingListener(TracerPanel panel) {
    this.panel = panel;
  }
  
  @Override
  public void start(ViewPlane viewPlane) {
    panel.getDrawablePanel().setImage(new BufferedImage(viewPlane.getHres(), viewPlane.getVres(), BufferedImage.TYPE_INT_RGB));
  }

  @Override
  public void afterTrace(ColorRGB color, int x, int y) {
    getImage().setRGB(x, y, color.toInt());
    
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        panel.repaint();
      }
    });
    
    paintedPixels++;
    panel.pixelsPainted(paintedPixels);
  }

  @Override
  public void finish() {
    paintedPixels = 0;
    panel.resetProgressBar();
  }
  
  private BufferedImage getImage() {
    return panel.getDrawablePanel().getImage();
  }

}
