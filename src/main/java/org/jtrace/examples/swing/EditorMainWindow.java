package org.jtrace.examples.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.jtrace.Scene;
import org.jtrace.Tracer;
import org.jtrace.ViewPlane;
import org.jtrace.cameras.Camera;
import org.jtrace.examples.swing.CameraEditorPanel.CameraChangeListener;
import org.jtrace.examples.swing.SceneEditorPanel.SceneChangeListener;
import org.jtrace.examples.swing.TracerEditorPanel.TracerChangeListener;
import org.jtrace.examples.swing.TracerEditorPanel.TracerSettings;
import org.jtrace.shader.AmbientShader;
import org.jtrace.shader.DiffuseShader;
import org.jtrace.shader.SpecularShader;
import org.jtrace.swing.DrawablePanel;

public class EditorMainWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private Scene scene;
    private Tracer tracer;
    private ViewPlane viewPlane;
    private SimpleTracerPanel tracerPanel;
    private DrawablePanel drawablePanel;

    private SceneEditorPanel sceneEditorPanel;
    private TracerEditorPanel tracerEditorPanel;
    private CameraEditorPanel cameraEditorPanel;

    private JButton renderButton;
    private JCheckBox autoRenderCheckBox;
    private JButton savePngButton;

    private boolean isAutoRenderEnabled = false;

    public EditorMainWindow(Scene initialScene) {
        setSize(1200, 900);
        setTitle("JTrace Modern Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.scene = initialScene;
        this.tracer = new Tracer();
        this.viewPlane = new ViewPlane(500, 500);

        initComponents();
        setupListeners();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initialRender();
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        sceneEditorPanel = new SceneEditorPanel(scene);
        tabbedPane.addTab("Scene", sceneEditorPanel);

        cameraEditorPanel = new CameraEditorPanel(scene);
        tabbedPane.addTab("Camera", cameraEditorPanel);

        tracerEditorPanel = new TracerEditorPanel();
        tabbedPane.addTab("Tracer", tracerEditorPanel);

        add(tabbedPane, BorderLayout.NORTH);

        tracerPanel = new SimpleTracerPanel(tracer, scene, viewPlane, 500, 500);
        drawablePanel = tracerPanel.getDrawablePanel();

        JScrollPane scrollPane = new JScrollPane(drawablePanel);
        scrollPane.setPreferredSize(new Dimension(700, 700));

        add(scrollPane, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        renderButton = new JButton("Render");
        renderButton.addActionListener(e -> performRender());

        autoRenderCheckBox = new JCheckBox("Auto-render on change");
        autoRenderCheckBox.addActionListener(e -> {
            isAutoRenderEnabled = autoRenderCheckBox.isSelected();
        });

        savePngButton = new JButton("Save to PNG");
        savePngButton.addActionListener(e -> saveToPng());

        controlsPanel.add(renderButton);
        controlsPanel.add(autoRenderCheckBox);
        controlsPanel.add(savePngButton);
        controlsPanel.add(tracerPanel.getProgressBar());

        add(controlsPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        sceneEditorPanel.setChangeListener(new SceneChangeListener() {
            @Override
            public void onSceneChanged(Scene newScene) {
                scene = newScene;
                if (isAutoRenderEnabled) {
                    performRender();
                }
            }
        });

        cameraEditorPanel.setChangeListener(new CameraChangeListener() {
            @Override
            public void onCameraChanged(Camera camera) {
                if (camera != null && scene != null) {
                    scene.setCamera(camera);
                    if (isAutoRenderEnabled) {
                        performRender();
                    }
                }
            }
        });

        tracerEditorPanel.setChangeListener(new TracerChangeListener() {
            @Override
            public void onTracerChanged(TracerSettings settings) {
                updateTracerSettings(settings);
                if (isAutoRenderEnabled) {
                    performRender();
                }
            }
        });
    }

    private void updateTracerSettings(TracerSettings settings) {
        tracer = new Tracer();

        if (settings.isAmbientShader()) {
            tracer.addShaders(new AmbientShader());
        }
        if (settings.isDiffuseShader()) {
            tracer.addShaders(new DiffuseShader());
        }
        if (settings.isSpecularShader()) {
            tracer.addShaders(new SpecularShader(settings.getSpecularFactor()));
        }

        viewPlane = new ViewPlane(settings.getViewPlaneWidth(), settings.getViewPlaneHeight());

        scene = scene.withBackground(settings.getBackgroundColor());
    }

    private void performRender() {
        tracerPanel.setTracer(tracer);
        tracerPanel.setScene(scene);
        tracerPanel.setViewPlane(viewPlane);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                tracerPanel.render();
            }
        });
    }

    private void initialRender() {
        updateTracerSettings(tracerEditorPanel.getSettings());
        performRender();
    }

    private void saveToPng() {
        BufferedImage image = drawablePanel.getImage();
        if (image == null) {
            JOptionPane.showMessageDialog(this, "No image to save. Please render first.", 
                "No Image", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Rendered Image");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String defaultFileName = "jtrace_render_" + sdf.format(new Date()) + ".png";
        fileChooser.setSelectedFile(new File(defaultFileName));

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "PNG Files", "png"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".png")) {
                fileToSave = new File(fileToSave.getParentFile(), 
                    fileToSave.getName() + ".png");
            }

            try {
                ImageIO.write(image, "PNG", fileToSave);
                JOptionPane.showMessageDialog(this, "Image saved successfully to:\n" + 
                    fileToSave.getAbsolutePath(), "Save Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), 
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
