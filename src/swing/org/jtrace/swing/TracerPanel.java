package org.jtrace.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;

public class TracerPanel extends JPanel {

  private Tracer tracer;
  private Scene scene;
  private ViewPlane viewPlane;
  
  //private JLabel statusLabel = new JLabel("Inativo");
  private JProgressBar progressBar;
  
  private int height;
  private int width;
  private DrawablePanel drawablePanel;
  
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
    
    SwingListener listener = new SwingListener(this);
    tracer.addListeners(listener);
    
    JButton renderBtn = new JButton("Render");
    renderBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        progressBar.setString(null);
        
        new Thread() {
          @Override
          public void run() {
            tracer.render(scene, viewPlane);
          }
        }.start();
      }
    });
    
    JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
    
    progressBar.setValue(0);
    progressBar.setStringPainted(true);
    
    statusPanel.add(progressBar);
    
    add(statusPanel, BorderLayout.PAGE_END);
    add(drawablePanel, BorderLayout.CENTER);
    add(renderBtn, BorderLayout.PAGE_START);
  }
  
  public DrawablePanel getDrawablePanel() {
    return drawablePanel;
  }
  
  /*public void setStatusLabel(String status) {
    statusLabel.setText(status);
  }*/
  
  public void pixelsPainted(int pixels) {
    progressBar.setValue(pixels);
  }
  
  public void resetProgressBar() {
    progressBar.setString("Inactive");
  }
}