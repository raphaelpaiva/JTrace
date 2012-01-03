package org.jtrace.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


/**
 * @author flavio
 * @see http://stackoverflow.com/questions/7298817/making-image-scrollable-in-jframe-contentpane
 * @see http://tech.javayogi.com/blogs/blog4.php/2010/05/23/java-game-development-paint-drawimage-performance-benchmark
 * @see http://www.java-tips.org/java-se-tips/java.awt.geom/how-to-create-a-buffered-image-2.html
 */
public class DrawablePanel extends JPanel {

  private static final long serialVersionUID = -6198405437842952827L;

  private BufferedImage image;
  
  @Override
  public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    
    if (image != null) {
      synchronized (image) {
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        
        // the next line scales the image
        //g2d.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, image.getWidth(), image.getHeight(), null);
        
        // the next line leaves the work to the scroll bars
        g2d.drawImage(image, 0, 0, null);
        
        // no need to call this method while rendering
        // let's wait until the image is completelly ready
        //revalidate(); 
      }
    } else {
      super.paint(g2d);
    }
  }

  public BufferedImage getImage() {
    return image;
  }

  public void setImage(BufferedImage image) {
    this.image = image;
    
    setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
  }
  
}