package org.jtrace.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DrawablePanel extends JPanel {

  private static final long serialVersionUID = -6198405437842952827L;

  private BufferedImage image;
  
  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    
    if (image != null) {
      synchronized (image) {
        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, image.getWidth(), image.getHeight(), null);
      }
    } else {
      super.paintComponent(g2d);
    }
  }

  public BufferedImage getImage() {
    return image;
  }

  public void setImage(BufferedImage image) {
    this.image = image;
  }
  
}