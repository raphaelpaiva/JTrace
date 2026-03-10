package org.jtrace.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;

public class TracerPanel extends JPanel {

  private static final long serialVersionUID = 6707782783340362498L;

  private Tracer tracer;
  private Scene scene;
  private ViewPlane viewPlane;
  
  private JProgressBar progressBar;
  
  private int height;
  private int width;
  private DrawablePanel drawablePanel;
  private SwingListener swingListener;
  
  public TracerPanel(Tracer tracer, Scene scene, ViewPlane viewPlane) {
    this(tracer, scene, viewPlane, 300, 300);
  }
  
  public TracerPanel(Tracer tracer, Scene scene, ViewPlane viewPlane, int width, int height) {
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
    
    swingListener = new SwingListener(this);
    tracer.addListeners(swingListener);
    
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
    tracer.clearListeners();
    tracer.addListeners(swingListener);
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
        tracer.render(scene, viewPlane);
      }
    }.start();
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