package org.jtrace.examples.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.swing.DrawablePanel;

public class SimpleTracerPanel extends JPanel {

    private static final long serialVersionUID = 6707782783340362498L;

    private Tracer tracer;
    private Scene scene;
    private ViewPlane viewPlane;
    
    private JProgressBar progressBar;
    
    private int height;
    private int width;
    private DrawablePanel drawablePanel;
    
    public SimpleTracerPanel(Tracer tracer, Scene scene, ViewPlane viewPlane, int width, int height) {
        super();
        
        this.tracer = tracer;
        this.scene = scene;
        this.viewPlane = viewPlane;
        this.width = width;
        this.height = height;
        
        this.progressBar = new JProgressBar(0, width * height);
        
        setLayout(new BorderLayout());
        prepareAspectRatio();
        init();
    }

    private void prepareAspectRatio() {
        float aspect = (float) viewPlane.getHres() / (float) viewPlane.getVres(); 
        int newheight = Math.round(height / aspect);
        
        setPreferredSize(new Dimension(width, newheight));
    }

    private void init() {
        drawablePanel = new DrawablePanel();
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        
        statusPanel.add(progressBar);
        
        JScrollPane scrollPane = new JScrollPane(drawablePanel);
        
        add(statusPanel, BorderLayout.PAGE_END);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setViewPlane(ViewPlane viewPlane) {
        this.viewPlane = viewPlane;
        this.progressBar.setMaximum(viewPlane.getHres() * viewPlane.getVres());
        prepareAspectRatio();
    }

    public void render() {
        progressBar.setValue(0);
        progressBar.setString(null);
        
        new Thread() {
            @Override
            public void run() {
                renderScene();
            }
        }.start();
    }
    
    private void renderScene() {
        final int hres = viewPlane.getHres();
        final int vres = viewPlane.getVres();
        
        BufferedImage image = new BufferedImage(hres, vres, BufferedImage.TYPE_INT_RGB);
        
        for (int r = 0; r < vres; r++) {
            for (int c = 0; c < hres; c++) {
                final org.jtrace.Jay jay = scene.getCamera().createJay(r, c, vres, hres);
                final ColorRGB color = tracer.trace(scene, jay);
                image.setRGB(c, vres - 1 - r, color.toInt());
            }
            
            final int progress = (r + 1) * hres;
            final int finalR = r;
                SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    progressBar.setValue(progress);
                    drawablePanel.setImage(image);
                    drawablePanel.repaint();
                }
            });
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setString("Complete");
                drawablePanel.setImage(image);
                drawablePanel.revalidate();
            }
        });
    }
   
    public DrawablePanel getDrawablePanel() {
        return drawablePanel;
    }
    
    public JProgressBar getProgressBar() {
        return progressBar;
    }
    
    public void pixelsPainted(int pixels) {
        progressBar.setValue(pixels);
    }
    
    public void resetProgressBar() {
        progressBar.setString("Inactive");
    }
}
