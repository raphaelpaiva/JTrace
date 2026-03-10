package org.jtrace.examples.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.jtrace.Scene;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.OrthogonalCamera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.Vector3D;

public class CameraEditorPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public interface CameraChangeListener {
        void onCameraChanged(Camera camera);
    }

    private CameraChangeListener changeListener;
    private Scene currentScene;

    private JComboBox<String> cameraTypeCombo;
    private JTextField eyeXField, eyeYField, eyeZField;
    private JTextField lookAtXField, lookAtYField, lookAtZField;
    private JTextField upXField, upYField, upZField;
    private JSpinner zoomFactorSpinner;

    public CameraEditorPanel(Scene scene) {
        this.currentScene = scene;
        setLayout(new BorderLayout());
        initComponents();
        loadCamera(scene.getCamera());
    }

    public void setChangeListener(CameraChangeListener listener) {
        this.changeListener = listener;
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Camera Type:"), gbc);

        cameraTypeCombo = new JComboBox<>(new String[]{"PinHole", "Orthogonal"});
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(cameraTypeCombo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Eye Position:"), gbc);

        JPanel eyePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        eyeXField = new JTextField("0", 6);
        eyeYField = new JTextField("0", 6);
        eyeZField = new JTextField("100", 6);
        eyePanel.add(new JLabel("X:"));
        eyePanel.add(eyeXField);
        eyePanel.add(new JLabel("Y:"));
        eyePanel.add(eyeYField);
        eyePanel.add(new JLabel("Z:"));
        eyePanel.add(eyeZField);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(eyePanel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Look At:"), gbc);

        JPanel lookAtPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lookAtXField = new JTextField("0", 6);
        lookAtYField = new JTextField("0", 6);
        lookAtZField = new JTextField("0", 6);
        lookAtPanel.add(new JLabel("X:"));
        lookAtPanel.add(lookAtXField);
        lookAtPanel.add(new JLabel("Y:"));
        lookAtPanel.add(lookAtYField);
        lookAtPanel.add(new JLabel("Z:"));
        lookAtPanel.add(lookAtZField);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(lookAtPanel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Up Vector:"), gbc);

        JPanel upPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        upXField = new JTextField("0", 6);
        upYField = new JTextField("1", 6);
        upZField = new JTextField("0", 6);
        upPanel.add(new JLabel("X:"));
        upPanel.add(upXField);
        upPanel.add(new JLabel("Y:"));
        upPanel.add(upYField);
        upPanel.add(new JLabel("Z:"));
        upPanel.add(upZField);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(upPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(new JLabel("Zoom Factor:"), gbc);

        zoomFactorSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 100.0, 0.1));
        gbc.gridx = 1;
        gbc.gridy = 8;
        panel.add(zoomFactorSpinner, gbc);

        JButton applyCameraBtn = new JButton("Apply Camera");
        applyCameraBtn.addActionListener(e -> notifyCameraChanged());
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        panel.add(applyCameraBtn, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void loadCamera(Camera cam) {
        if (cam != null) {
            if (cam instanceof OrthogonalCamera) {
                cameraTypeCombo.setSelectedItem("Orthogonal");
            } else {
                cameraTypeCombo.setSelectedItem("PinHole");
            }

            eyeXField.setText(String.valueOf(cam.getEye().getX()));
            eyeYField.setText(String.valueOf(cam.getEye().getY()));
            eyeZField.setText(String.valueOf(cam.getEye().getZ()));

            lookAtXField.setText(String.valueOf(cam.getLookAt().getX()));
            lookAtYField.setText(String.valueOf(cam.getLookAt().getY()));
            lookAtZField.setText(String.valueOf(cam.getLookAt().getZ()));

            upXField.setText(String.valueOf(cam.getUp().getX()));
            upYField.setText(String.valueOf(cam.getUp().getY()));
            upZField.setText(String.valueOf(cam.getUp().getZ()));

            zoomFactorSpinner.setValue(cam.getZoomFactor());
        }
    }

    public Camera buildCamera() {
        try {
            double eyeX = parseDouble(eyeXField.getText(), 0);
            double eyeY = parseDouble(eyeYField.getText(), 0);
            double eyeZ = parseDouble(eyeZField.getText(), 100);

            double lookAtX = parseDouble(lookAtXField.getText(), 0);
            double lookAtY = parseDouble(lookAtYField.getText(), 0);
            double lookAtZ = parseDouble(lookAtZField.getText(), 0);

            double upX = parseDouble(upXField.getText(), 0);
            double upY = parseDouble(upYField.getText(), 1);
            double upZ = parseDouble(upZField.getText(), 0);

            double zoom = parseDouble(zoomFactorSpinner.getValue().toString(), 1);

            Point3D eye = new Point3D(eyeX, eyeY, eyeZ);
            Point3D lookAt = new Point3D(lookAtX, lookAtY, lookAtZ);
            Vector3D up = new Vector3D(upX, upY, upZ);

            Camera camera;
            if ("Orthogonal".equals(cameraTypeCombo.getSelectedItem())) {
                camera = new OrthogonalCamera(eye, lookAt, up);
            } else {
                camera = new PinHoleCamera(eye, lookAt, up);
            }
            camera.setZoomFactor(zoom);

            return camera;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private double parseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void notifyCameraChanged() {
        if (changeListener != null) {
            changeListener.onCameraChanged(buildCamera());
        }
    }
}
