package org.jtrace.examples.swing;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jtrace.primitives.ColorRGB;
import org.jtrace.shader.AmbientShader;
import org.jtrace.shader.DiffuseShader;
import org.jtrace.shader.SpecularShader;

public class TracerEditorPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public interface TracerChangeListener {
        void onTracerChanged(TracerSettings settings);
    }

    public static class TracerSettings {
        private boolean ambientShader;
        private boolean diffuseShader;
        private boolean specularShader;
        private double specularFactor;
        private int viewPlaneWidth;
        private int viewPlaneHeight;
        private ColorRGB backgroundColor;

        public boolean isAmbientShader() {
            return ambientShader;
        }

        public void setAmbientShader(boolean ambientShader) {
            this.ambientShader = ambientShader;
        }

        public boolean isDiffuseShader() {
            return diffuseShader;
        }

        public void setDiffuseShader(boolean diffuseShader) {
            this.diffuseShader = diffuseShader;
        }

        public boolean isSpecularShader() {
            return specularShader;
        }

        public void setSpecularShader(boolean specularShader) {
            this.specularShader = specularShader;
        }

        public double getSpecularFactor() {
            return specularFactor;
        }

        public void setSpecularFactor(double specularFactor) {
            this.specularFactor = specularFactor;
        }

        public int getViewPlaneWidth() {
            return viewPlaneWidth;
        }

        public void setViewPlaneWidth(int viewPlaneWidth) {
            this.viewPlaneWidth = viewPlaneWidth;
        }

        public int getViewPlaneHeight() {
            return viewPlaneHeight;
        }

        public void setViewPlaneHeight(int viewPlaneHeight) {
            this.viewPlaneHeight = viewPlaneHeight;
        }

        public ColorRGB getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(ColorRGB backgroundColor) {
            this.backgroundColor = backgroundColor;
        }
    }

    private TracerChangeListener changeListener;
    private TracerSettings settings;

    private JCheckBox ambientCheckBox;
    private JCheckBox diffuseCheckBox;
    private JCheckBox specularCheckBox;
    private JSpinner specularFactorSpinner;
    private JSpinner widthSpinner;
    private JSpinner heightSpinner;
    private JLabel backgroundColorLabel;
    private ColorRGB currentBackgroundColor;

    private final Map<String, ColorRGB> predefinedColors;

    public TracerEditorPanel() {
        this.settings = new TracerSettings();
        this.settings.setAmbientShader(true);
        this.settings.setDiffuseShader(true);
        this.settings.setSpecularShader(false);
        this.settings.setSpecularFactor(10.0);
        this.settings.setViewPlaneWidth(500);
        this.settings.setViewPlaneHeight(500);
        this.settings.setBackgroundColor(ColorRGB.BLACK);

        this.predefinedColors = createPredefinedColors();
        this.currentBackgroundColor = ColorRGB.BLACK;

        setLayout(new BorderLayout());
        initComponents();
    }

    private Map<String, ColorRGB> createPredefinedColors() {
        Map<String, ColorRGB> colors = new HashMap<>();
        colors.put("RED", ColorRGB.RED);
        colors.put("GREEN", ColorRGB.GREEN);
        colors.put("BLUE", ColorRGB.BLUE);
        colors.put("YELLOW", ColorRGB.YELLOW);
        colors.put("WHITE", ColorRGB.WHITE);
        colors.put("BLACK", ColorRGB.BLACK);
        colors.put("PURPLE", ColorRGB.PURPLE);
        colors.put("CYAN", new ColorRGB(0.0, 1.0, 1.0));
        colors.put("MAGENTA", new ColorRGB(1.0, 0.0, 1.0));
        colors.put("GRAY", new ColorRGB(0.5, 0.5, 0.5));
        colors.put("ORANGE", new ColorRGB(1.0, 0.5, 0.0));
        colors.put("PINK", new ColorRGB(1.0, 0.7, 0.7));
        return colors;
    }

    public void setChangeListener(TracerChangeListener listener) {
        this.changeListener = listener;
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Shaders:"), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        ambientCheckBox = new JCheckBox("Ambient");
        ambientCheckBox.setSelected(true);
        ambientCheckBox.addActionListener(e -> notifyChanged());
        panel.add(ambientCheckBox, gbc);

        gbc.gridx = 1;
        diffuseCheckBox = new JCheckBox("Diffuse");
        diffuseCheckBox.setSelected(true);
        diffuseCheckBox.addActionListener(e -> notifyChanged());
        panel.add(diffuseCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        specularCheckBox = new JCheckBox("Specular");
        specularCheckBox.addActionListener(e -> {
            specularFactorSpinner.setEnabled(specularCheckBox.isSelected());
            notifyChanged();
        });
        panel.add(specularCheckBox, gbc);

        gbc.gridx = 1;
        specularFactorSpinner = new JSpinner(new SpinnerNumberModel(10.0, 1.0, 100.0, 1.0));
        specularFactorSpinner.setEnabled(false);
        specularFactorSpinner.addChangeListener(e -> notifyChanged());
        panel.add(specularFactorSpinner, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JSeparator(), gbc);

        gbc.gridy = 4;
        panel.add(new JLabel("View Plane Resolution:"), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 5;
        panel.add(new JLabel("Width:"), gbc);

        gbc.gridx = 1;
        widthSpinner = new JSpinner(new SpinnerNumberModel(500, 100, 2000, 50));
        widthSpinner.addChangeListener(e -> notifyChanged());
        panel.add(widthSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Height:"), gbc);

        gbc.gridx = 1;
        heightSpinner = new JSpinner(new SpinnerNumberModel(500, 100, 2000, 50));
        heightSpinner.addChangeListener(e -> notifyChanged());
        panel.add(heightSpinner, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JSeparator(), gbc);

        gbc.gridy = 8;
        panel.add(new JLabel("Background Color:"), gbc);

        JPanel colorPanel = new JPanel(new GridBagLayout());
        colorPanel.setBorder(BorderFactory.createEtchedBorder());
        GridBagConstraints colorGbc = new GridBagConstraints();
        colorGbc.insets = new Insets(2, 2, 2, 2);

        int colorIndex = 0;
        for (Map.Entry<String, ColorRGB> entry : predefinedColors.entrySet()) {
            int row = colorIndex / 4;
            int col = colorIndex % 4;
            colorGbc.gridx = col;
            colorGbc.gridy = row;
            JButton colorBtn = createColorButton(entry.getKey(), entry.getValue());
            colorPanel.add(colorBtn, colorGbc);
            colorIndex++;
        }

        colorGbc.gridx = 0;
        colorGbc.gridy = (predefinedColors.size() + 3) / 4;
        colorGbc.gridwidth = 4;
        JButton customColorBtn = new JButton("Custom...");
        customColorBtn.addActionListener(e -> {
            ColorRGB selected = selectCustomColor();
            if (selected != null) {
                currentBackgroundColor = selected;
                updateBackgroundColorLabel();
                notifyChanged();
            }
        });
        colorPanel.add(customColorBtn, colorGbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        panel.add(colorPanel, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private JButton createColorButton(String name, ColorRGB color) {
        JButton btn = new JButton(name);
        btn.setForeground(convertToAwtColor(color));
        btn.setBackground(convertToAwtColor(color));
        btn.addActionListener(e -> {
            currentBackgroundColor = color;
            updateBackgroundColorLabel();
            notifyChanged();
        });
        return btn;
    }

    private ColorRGB selectCustomColor() {
        java.awt.Color selected = JColorChooser.showDialog(this, "Choose Background Color", 
            convertToAwtColor(currentBackgroundColor));
        if (selected != null) {
            return new ColorRGB(selected.getRed(), selected.getGreen(), selected.getBlue());
        }
        return null;
    }

    private void updateBackgroundColorLabel() {
        backgroundColorLabel.setBackground(convertToAwtColor(currentBackgroundColor));
    }

    private java.awt.Color convertToAwtColor(ColorRGB colorRGB) {
        return new java.awt.Color(
            (int) (colorRGB.getRed() * 255),
            (int) (colorRGB.getGreen() * 255),
            (int) (colorRGB.getBlue() * 255)
        );
    }

    public TracerSettings getSettings() {
        settings.setAmbientShader(ambientCheckBox.isSelected());
        settings.setDiffuseShader(diffuseCheckBox.isSelected());
        settings.setSpecularShader(specularCheckBox.isSelected());
        settings.setSpecularFactor(((Number) specularFactorSpinner.getValue()).doubleValue());
        settings.setViewPlaneWidth(((Number) widthSpinner.getValue()).intValue());
        settings.setViewPlaneHeight(((Number) heightSpinner.getValue()).intValue());
        settings.setBackgroundColor(currentBackgroundColor);
        return settings;
    }

    public void setSettings(TracerSettings settings) {
        this.settings = settings;
        ambientCheckBox.setSelected(settings.isAmbientShader());
        diffuseCheckBox.setSelected(settings.isDiffuseShader());
        specularCheckBox.setSelected(settings.isSpecularShader());
        specularFactorSpinner.setValue(settings.getSpecularFactor());
        specularFactorSpinner.setEnabled(settings.isSpecularShader());
        widthSpinner.setValue(settings.getViewPlaneWidth());
        heightSpinner.setValue(settings.getViewPlaneHeight());
        currentBackgroundColor = settings.getBackgroundColor();
    }

    private void notifyChanged() {
        if (changeListener != null) {
            changeListener.onTracerChanged(getSettings());
        }
    }

    public Map<String, ColorRGB> getPredefinedColors() {
        return predefinedColors;
    }

    class JSeparator extends JLabel {
        private static final long serialVersionUID = 1L;

        JSeparator() {
            setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, java.awt.Color.GRAY));
            setPreferredSize(new java.awt.Dimension(200, 2));
        }
    }
}
