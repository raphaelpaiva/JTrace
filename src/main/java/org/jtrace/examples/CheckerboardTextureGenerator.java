package org.jtrace.examples;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CheckerboardTextureGenerator {
    public static void main(String[] args) throws IOException {
        int size = 512;
        int squares = 8;
        int squareSize = size / squares;
        
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        for (int y = 0; y < squares; y++) {
            for (int x = 0; x < squares; x++) {
                boolean isWhite = (x + y) % 2 == 0;
                g.setColor(isWhite ? Color.WHITE : Color.BLACK);
                g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
            }
        }
        
        g.dispose();
        
        File output = new File("src/main/resources/org/jtrace/examples/checkerboard.png");
        ImageIO.write(image, "png", output);
        System.out.println("Created: " + output.getAbsolutePath());
    }
}
