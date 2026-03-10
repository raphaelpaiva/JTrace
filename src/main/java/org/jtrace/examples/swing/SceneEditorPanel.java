package org.jtrace.examples.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;

import org.jtrace.Material;
import org.jtrace.Scene;
import org.jtrace.cameras.Camera;
import org.jtrace.cameras.OrthogonalCamera;
import org.jtrace.cameras.PinHoleCamera;
import org.jtrace.geometry.GeometricObject;
import org.jtrace.geometry.Plane;
import org.jtrace.geometry.Quadrilateral;
import org.jtrace.geometry.Sphere;
import org.jtrace.geometry.Triangle;
import org.jtrace.lights.DecayingPointLight;
import org.jtrace.lights.Light;
import org.jtrace.lights.PointLight;
import org.jtrace.primitives.ColorRGB;
import org.jtrace.primitives.Point3D;
import org.jtrace.primitives.ReflectanceCoefficient;
import org.jtrace.primitives.Vector3D;

public class SceneEditorPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public interface SceneChangeListener {
        void onSceneChanged(Scene scene);
    }

    public interface ItemEditor<T> {
        String getName();
        T createNew();
        T edit(T existing);
    }

    private SceneChangeListener changeListener;
    private Scene currentScene;

    private SceneItemTableModel tableModel;
    private JTable sceneTable;

    private final Map<String, ColorRGB> predefinedColors;
    private final List<ItemEditor<?>> objectEditors = new ArrayList<>();
    private final List<ItemEditor<?>> lightEditors = new ArrayList<>();

    public SceneEditorPanel(Scene scene) {
        this.currentScene = scene;
        this.predefinedColors = createPredefinedColors();
        
        registerEditors();
        
        setLayout(new BorderLayout());
        initComponents();
        loadScene(scene);
    }

    private void registerEditors() {
        objectEditors.add(new SphereEditor());
        objectEditors.add(new PlaneEditor());
        objectEditors.add(new TriangleEditor());
        objectEditors.add(new QuadrilateralEditor());
        
        lightEditors.add(new PointLightEditor());
        lightEditors.add(new DecayingLightEditor());
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

    public void setChangeListener(SceneChangeListener listener) {
        this.changeListener = listener;
    }

    private void initComponents() {
        add(createScenePanel(), BorderLayout.CENTER);
    }

    private JPanel createScenePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        tableModel = new SceneItemTableModel();
        sceneTable = new JTable(tableModel);
        sceneTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sceneTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        sceneTable.getColumnModel().getColumn(2).setPreferredWidth(150);

        sceneTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedItem();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(sceneTable);
        scrollPane.setPreferredSize(new Dimension(400, 250));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addBtn = new JButton("Add...");
        JButton editBtn = new JButton("Edit");
        JButton removeBtn = new JButton("Remove");

        addBtn.addActionListener(e -> showAddDialog());
        editBtn.addActionListener(e -> editSelectedItem());
        removeBtn.addActionListener(e -> removeSelectedItem());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(removeBtn);

        panel.add(new JLabel("Scene Objects (double-click to edit):"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showAddDialog() {
        List<String> allTypes = new ArrayList<>();
        for (ItemEditor<?> editor : objectEditors) {
            allTypes.add(editor.getName());
        }
        allTypes.add("--- Lights ---");
        for (ItemEditor<?> editor : lightEditors) {
            allTypes.add(editor.getName());
        }
        
        String[] types = allTypes.toArray(new String[0]);
        String selected = (String) JOptionPane.showInputDialog(
            this, "Select object type:", "Add Object",
            JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
        
        if (selected == null || selected.startsWith("---")) return;
        
        Object newItem = null;
        
        for (ItemEditor<?> editor : objectEditors) {
            if (editor.getName().equals(selected)) {
                newItem = editor.createNew();
                break;
            }
        }
        
        if (newItem == null) {
            for (ItemEditor<?> editor : lightEditors) {
                if (editor.getName().equals(selected)) {
                    newItem = editor.createNew();
                    break;
                }
            }
        }
        
        if (newItem != null) {
            String category = isLight(newItem) ? "Light" : "Object";
            tableModel.addItem(new SceneItem(category, newItem));
            notifySceneChanged();
        }
    }

    private boolean isLight(Object item) {
        return item instanceof Light;
    }

    private void editSelectedItem() {
        int selectedRow = sceneTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.");
            return;
        }
        
        SceneItem item = tableModel.getItemAt(selectedRow);
        Object edited = null;
        
        for (ItemEditor<?> editor : objectEditors) {
            if (editor.getName().equals(item.getType())) {
                @SuppressWarnings("unchecked")
                ItemEditor<Object> objectEditor = (ItemEditor<Object>) editor;
                edited = objectEditor.edit(item.getItem());
                break;
            }
        }
        
        if (edited == null) {
            for (ItemEditor<?> editor : lightEditors) {
                if (editor.getName().equals(item.getType())) {
                    @SuppressWarnings("unchecked")
                    ItemEditor<Object> lightEditor = (ItemEditor<Object>) editor;
                    edited = lightEditor.edit(item.getItem());
                    break;
                }
            }
        }
        
        if (edited != null) {
            tableModel.setItemAt(selectedRow, new SceneItem(item.getCategory(), edited));
            notifySceneChanged();
        }
    }

    private void loadScene(Scene scene) {
        tableModel.clear();
        for (GeometricObject obj : scene.getObjects()) {
            tableModel.addItem(new SceneItem("Object", obj));
        }
        for (Light light : scene.getLigths()) {
            tableModel.addItem(new SceneItem("Light", light));
        }
    }

    private void removeSelectedItem() {
        int selectedRow = sceneTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeItem(selectedRow);
            notifySceneChanged();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.");
        }
    }

    public Scene buildScene() {
        Scene scene = new Scene();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            SceneItem item = tableModel.getItemAt(i);
            if (item != null) {
                if (item.getItem() instanceof GeometricObject) {
                    scene.add((GeometricObject) item.getItem());
                } else if (item.getItem() instanceof Light) {
                    scene.add((Light) item.getItem());
                }
            }
        }

        return scene;
    }

    private void notifySceneChanged() {
        if (changeListener != null) {
            changeListener.onSceneChanged(buildScene());
        }
    }

    private Material createDefaultMaterial(ColorRGB color) {
        return new Material(color,
            new ReflectanceCoefficient(0.07, 0.07, 0.07),
            new ReflectanceCoefficient(0.3, 0.3, 0.3));
    }

    private JDialog createDialog(String title) {
        JDialog dialog = new JDialog((java.awt.Frame) null, title, true);
        dialog.setLayout(new GridBagLayout());
        return dialog;
    }

    private void addLabelAndField(JDialog dialog, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        dialog.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        dialog.add(field, gbc);
    }

    private void addSectionLabel(JDialog dialog, GridBagConstraints gbc, int row, String label) {
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row;
        dialog.add(new JLabel(label), gbc);
    }

    private void addButtons(JDialog dialog, GridBagConstraints gbc, int row, String ok, String cancel, 
            java.awt.event.ActionListener okListener, java.awt.event.ActionListener cancelListener) {
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row;
        JPanel panel = new JPanel();
        JButton okBtn = new JButton(ok);
        JButton cancelBtn = new JButton(cancel);
        okBtn.addActionListener(okListener);
        cancelBtn.addActionListener(cancelListener);
        panel.add(okBtn);
        panel.add(cancelBtn);
        dialog.add(panel, gbc);
    }

    private double parseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // ========== Editors ==========

    class SphereEditor implements ItemEditor<Sphere> {
        @Override
        public String getName() { return "Sphere"; }

        @Override
        public Sphere createNew() {
            return edit(new Sphere(new Point3D(0, 0, -10), 10, createDefaultMaterial(ColorRGB.RED)));
        }

        @Override
        public Sphere edit(Sphere existing) {
            JDialog dialog = createDialog(existing != null ? "Edit Sphere" : "Add Sphere");
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField centerX = new JTextField(existing != null ? String.valueOf(existing.getCenter().getX()) : "0", 8);
            JTextField centerY = new JTextField(existing != null ? String.valueOf(existing.getCenter().getY()) : "0", 8);
            JTextField centerZ = new JTextField(existing != null ? String.valueOf(existing.getCenter().getZ()) : "-10", 8);
            JSpinner radius = new JSpinner(new SpinnerNumberModel(
                existing != null ? existing.getRadius() : 10.0, 0.1, 1000.0, 0.5));
            
            JComboBox<String> colorCombo = new JComboBox<>(predefinedColors.keySet().toArray(new String[0]));
            if (existing != null) {
                ColorRGB color = existing.getMaterial().getColor();
                for (Map.Entry<String, ColorRGB> e : predefinedColors.entrySet()) {
                    if (e.getValue().equals(color)) { colorCombo.setSelectedItem(e.getKey()); break; }
                }
            } else { colorCombo.setSelectedItem("RED"); }

            int row = 0;
            addLabelAndField(dialog, gbc, row, "Center X:", centerX);
            addLabelAndField(dialog, gbc, ++row, "Center Y:", centerY);
            addLabelAndField(dialog, gbc, ++row, "Center Z:", centerZ);
            addLabelAndField(dialog, gbc, ++row, "Radius:", radius);
            addLabelAndField(dialog, gbc, ++row, "Color:", colorCombo);

            final Sphere[] result = new Sphere[1];
            addButtons(dialog, gbc, ++row, "OK", "Cancel", 
                e -> { 
                    try {
                        ColorRGB c = predefinedColors.get(colorCombo.getSelectedItem());
                        result[0] = new Sphere(
                            new Point3D(parseDouble(centerX.getText(), 0), parseDouble(centerY.getText(), 0), parseDouble(centerZ.getText(), -10)),
                            ((Number)radius.getValue()).doubleValue(),
                            createDefaultMaterial(c != null ? c : ColorRGB.RED));
                        dialog.dispose();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, "Invalid value"); }
                }, e -> dialog.dispose());

            dialog.setSize(350, 250);
            dialog.setLocationRelativeTo(SceneEditorPanel.this);
            dialog.setVisible(true);
            return result[0];
        }
    }

    class PlaneEditor implements ItemEditor<Plane> {
        @Override
        public String getName() { return "Plane"; }

        @Override
        public Plane createNew() {
            return edit(new Plane(new Point3D(0, 20, 0), new Vector3D(0, -1, 0), createDefaultMaterial(ColorRGB.YELLOW)));
        }

        @Override
        public Plane edit(Plane existing) {
            JDialog dialog = createDialog(existing != null ? "Edit Plane" : "Add Plane");
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField pointX = new JTextField(existing != null ? String.valueOf(existing.getPoint().getX()) : "0", 8);
            JTextField pointY = new JTextField(existing != null ? String.valueOf(existing.getPoint().getY()) : "20", 8);
            JTextField pointZ = new JTextField(existing != null ? String.valueOf(existing.getPoint().getZ()) : "0", 8);
            JTextField normalX = new JTextField(existing != null ? String.valueOf(existing.getNormal().getX()) : "0", 8);
            JTextField normalY = new JTextField(existing != null ? String.valueOf(existing.getNormal().getY()) : "-1", 8);
            JTextField normalZ = new JTextField(existing != null ? String.valueOf(existing.getNormal().getZ()) : "0", 8);
            JComboBox<String> colorCombo = new JComboBox<>(predefinedColors.keySet().toArray(new String[0]));
            if (existing != null) {
                ColorRGB color = existing.getMaterial().getColor();
                for (Map.Entry<String, ColorRGB> e : predefinedColors.entrySet()) {
                    if (e.getValue().equals(color)) { colorCombo.setSelectedItem(e.getKey()); break; }
                }
            } else { colorCombo.setSelectedItem("YELLOW"); }

            int row = 0;
            addSectionLabel(dialog, gbc, row, "Point:");
            addLabelAndField(dialog, gbc, ++row, "X:", pointX);
            addLabelAndField(dialog, gbc, ++row, "Y:", pointY);
            addLabelAndField(dialog, gbc, ++row, "Z:", pointZ);
            addSectionLabel(dialog, gbc, ++row, "Normal:");
            addLabelAndField(dialog, gbc, ++row, "X:", normalX);
            addLabelAndField(dialog, gbc, ++row, "Y:", normalY);
            addLabelAndField(dialog, gbc, ++row, "Z:", normalZ);
            addLabelAndField(dialog, gbc, ++row, "Color:", colorCombo);

            final Plane[] result = new Plane[1];
            addButtons(dialog, gbc, ++row, "OK", "Cancel", 
                e -> { 
                    try {
                        ColorRGB c = predefinedColors.get(colorCombo.getSelectedItem());
                        result[0] = new Plane(
                            new Point3D(parseDouble(pointX.getText(), 0), parseDouble(pointY.getText(), 20), parseDouble(pointZ.getText(), 0)),
                            new Vector3D(parseDouble(normalX.getText(), 0), parseDouble(normalY.getText(), -1), parseDouble(normalZ.getText(), 0)),
                            createDefaultMaterial(c != null ? c : ColorRGB.YELLOW));
                        dialog.dispose();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, "Invalid value"); }
                }, e -> dialog.dispose());

            dialog.setSize(350, 350);
            dialog.setLocationRelativeTo(SceneEditorPanel.this);
            dialog.setVisible(true);
            return result[0];
        }
    }

    class TriangleEditor implements ItemEditor<Triangle> {
        @Override
        public String getName() { return "Triangle"; }

        @Override
        public Triangle createNew() {
            return edit(new Triangle(
                new Point3D(-10, 0, -20),
                new Point3D(10, 0, -20),
                new Point3D(0, 10, -20),
                createDefaultMaterial(ColorRGB.GREEN)));
        }

        @Override
        public Triangle edit(Triangle existing) {
            JDialog dialog = createDialog(existing != null ? "Edit Triangle" : "Add Triangle");
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField v1x = new JTextField(existing != null ? String.valueOf(existing.getXMin()) : "-10", 8);
            JTextField v1y = new JTextField(existing != null ? String.valueOf(existing.getYMin()) : "0", 8);
            JTextField v1z = new JTextField(existing != null ? String.valueOf(existing.getZMin()) : "-20", 8);
            
            JTextField v2x = new JTextField("10");
            JTextField v2y = new JTextField("0");
            JTextField v2z = new JTextField("-20");
            
            JTextField v3x = new JTextField("0");
            JTextField v3y = new JTextField("10");
            JTextField v3z = new JTextField("-20");
            
            JComboBox<String> colorCombo = new JComboBox<>(predefinedColors.keySet().toArray(new String[0]));
            colorCombo.setSelectedItem("GREEN");

            int row = 0;
            addSectionLabel(dialog, gbc, row, "Vertex 1:");
            addLabelAndField(dialog, gbc, ++row, "X:", v1x);
            addLabelAndField(dialog, gbc, ++row, "Y:", v1y);
            addLabelAndField(dialog, gbc, ++row, "Z:", v1z);
            addSectionLabel(dialog, gbc, ++row, "Vertex 2:");
            addLabelAndField(dialog, gbc, ++row, "X:", v2x);
            addLabelAndField(dialog, gbc, ++row, "Y:", v2y);
            addLabelAndField(dialog, gbc, ++row, "Z:", v2z);
            addSectionLabel(dialog, gbc, ++row, "Vertex 3:");
            addLabelAndField(dialog, gbc, ++row, "X:", v3x);
            addLabelAndField(dialog, gbc, ++row, "Y:", v3y);
            addLabelAndField(dialog, gbc, ++row, "Z:", v3z);
            addLabelAndField(dialog, gbc, ++row, "Color:", colorCombo);

            final Triangle[] result = new Triangle[1];
            addButtons(dialog, gbc, ++row, "OK", "Cancel", 
                e -> { 
                    try {
                        ColorRGB c = predefinedColors.get(colorCombo.getSelectedItem());
                        result[0] = new Triangle(
                            new Point3D(parseDouble(v1x.getText(), -10), parseDouble(v1y.getText(), 0), parseDouble(v1z.getText(), -20)),
                            new Point3D(parseDouble(v2x.getText(), 10), parseDouble(v2y.getText(), 0), parseDouble(v2z.getText(), -20)),
                            new Point3D(parseDouble(v3x.getText(), 0), parseDouble(v3y.getText(), 10), parseDouble(v3z.getText(), -20)),
                            createDefaultMaterial(c != null ? c : ColorRGB.GREEN));
                        dialog.dispose();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, "Invalid value"); }
                }, e -> dialog.dispose());

            dialog.setSize(350, 450);
            dialog.setLocationRelativeTo(SceneEditorPanel.this);
            dialog.setVisible(true);
            return result[0];
        }
    }

    class QuadrilateralEditor implements ItemEditor<Quadrilateral> {
        @Override
        public String getName() { return "Quadrilateral"; }

        @Override
        public Quadrilateral createNew() {
            return edit(new Quadrilateral(
                new Point3D(-10, 0, -20),
                new Point3D(10, 0, -20),
                new Point3D(10, 10, -20),
                new Point3D(-10, 10, -20),
                createDefaultMaterial(ColorRGB.BLUE)));
        }

        @Override
        public Quadrilateral edit(Quadrilateral existing) {
            JDialog dialog = createDialog(existing != null ? "Edit Quadrilateral" : "Add Quadrilateral");
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField p0x = new JTextField("-10");
            JTextField p0y = new JTextField("0");
            JTextField p0z = new JTextField("-20");
            
            JTextField p1x = new JTextField("10");
            JTextField p1y = new JTextField("0");
            JTextField p1z = new JTextField("-20");
            
            JTextField p2x = new JTextField("10");
            JTextField p2y = new JTextField("10");
            JTextField p2z = new JTextField("-20");
            
            JTextField p3x = new JTextField("-10");
            JTextField p3y = new JTextField("10");
            JTextField p3z = new JTextField("-20");
            
            JComboBox<String> colorCombo = new JComboBox<>(predefinedColors.keySet().toArray(new String[0]));
            colorCombo.setSelectedItem("BLUE");

            int row = 0;
            addSectionLabel(dialog, gbc, row, "Point 0:");
            addLabelAndField(dialog, gbc, ++row, "X:", p0x);
            addLabelAndField(dialog, gbc, ++row, "Y:", p0y);
            addLabelAndField(dialog, gbc, ++row, "Z:", p0z);
            addSectionLabel(dialog, gbc, ++row, "Point 1:");
            addLabelAndField(dialog, gbc, ++row, "X:", p1x);
            addLabelAndField(dialog, gbc, ++row, "Y:", p1y);
            addLabelAndField(dialog, gbc, ++row, "Z:", p1z);
            addSectionLabel(dialog, gbc, ++row, "Point 2:");
            addLabelAndField(dialog, gbc, ++row, "X:", p2x);
            addLabelAndField(dialog, gbc, ++row, "Y:", p2y);
            addLabelAndField(dialog, gbc, ++row, "Z:", p2z);
            addSectionLabel(dialog, gbc, ++row, "Point 3:");
            addLabelAndField(dialog, gbc, ++row, "X:", p3x);
            addLabelAndField(dialog, gbc, ++row, "Y:", p3y);
            addLabelAndField(dialog, gbc, ++row, "Z:", p3z);
            addLabelAndField(dialog, gbc, ++row, "Color:", colorCombo);

            final Quadrilateral[] result = new Quadrilateral[1];
            addButtons(dialog, gbc, ++row, "OK", "Cancel", 
                e -> { 
                    try {
                        ColorRGB c = predefinedColors.get(colorCombo.getSelectedItem());
                        result[0] = new Quadrilateral(
                            new Point3D(parseDouble(p0x.getText(), -10), parseDouble(p0y.getText(), 0), parseDouble(p0z.getText(), -20)),
                            new Point3D(parseDouble(p1x.getText(), 10), parseDouble(p1y.getText(), 0), parseDouble(p1z.getText(), -20)),
                            new Point3D(parseDouble(p2x.getText(), 10), parseDouble(p2y.getText(), 10), parseDouble(p2z.getText(), -20)),
                            new Point3D(parseDouble(p3x.getText(), -10), parseDouble(p3y.getText(), 10), parseDouble(p3z.getText(), -20)),
                            createDefaultMaterial(c != null ? c : ColorRGB.BLUE));
                        dialog.dispose();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, "Invalid value"); }
                }, e -> dialog.dispose());

            dialog.setSize(350, 550);
            dialog.setLocationRelativeTo(SceneEditorPanel.this);
            dialog.setVisible(true);
            return result[0];
        }
    }

    class PointLightEditor implements ItemEditor<PointLight> {
        @Override
        public String getName() { return "Point Light"; }

        @Override
        public PointLight createNew() {
            return edit(new PointLight(0, -20, 10, ColorRGB.WHITE));
        }

        @Override
        public PointLight edit(PointLight existing) {
            JDialog dialog = createDialog(existing != null ? "Edit Point Light" : "Add Point Light");
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField x = new JTextField(existing != null ? String.valueOf(existing.getPosition().getX()) : "0", 8);
            JTextField y = new JTextField(existing != null ? String.valueOf(existing.getPosition().getY()) : "-20", 8);
            JTextField z = new JTextField(existing != null ? String.valueOf(existing.getPosition().getZ()) : "10", 8);
            JComboBox<String> colorCombo = new JComboBox<>(predefinedColors.keySet().toArray(new String[0]));
            if (existing != null) {
                ColorRGB color = existing.getColor();
                for (Map.Entry<String, ColorRGB> e : predefinedColors.entrySet()) {
                    if (e.getValue().equals(color)) { colorCombo.setSelectedItem(e.getKey()); break; }
                }
            } else { colorCombo.setSelectedItem("WHITE"); }

            int row = 0;
            addLabelAndField(dialog, gbc, row, "Position X:", x);
            addLabelAndField(dialog, gbc, ++row, "Position Y:", y);
            addLabelAndField(dialog, gbc, ++row, "Position Z:", z);
            addLabelAndField(dialog, gbc, ++row, "Color:", colorCombo);

            final PointLight[] result = new PointLight[1];
            addButtons(dialog, gbc, ++row, "OK", "Cancel", 
                e -> { 
                    try {
                        ColorRGB c = predefinedColors.get(colorCombo.getSelectedItem());
                        result[0] = new PointLight(new Point3D(parseDouble(x.getText(), 0), parseDouble(y.getText(), -20), parseDouble(z.getText(), 10)), 
                            c != null ? c : ColorRGB.WHITE);
                        dialog.dispose();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, "Invalid value"); }
                }, e -> dialog.dispose());

            dialog.setSize(350, 250);
            dialog.setLocationRelativeTo(SceneEditorPanel.this);
            dialog.setVisible(true);
            return result[0];
        }
    }

    class DecayingLightEditor implements ItemEditor<DecayingPointLight> {
        @Override
        public String getName() { return "Decaying Point Light"; }

        @Override
        public DecayingPointLight createNew() {
            return edit(new DecayingPointLight(0, -20, 10, 100));
        }

        @Override
        public DecayingPointLight edit(DecayingPointLight existing) {
            JDialog dialog = createDialog(existing != null ? "Edit Decaying Light" : "Add Decaying Light");
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField x = new JTextField(existing != null ? String.valueOf(existing.getPosition().getX()) : "0", 8);
            JTextField y = new JTextField(existing != null ? String.valueOf(existing.getPosition().getY()) : "-20", 8);
            JTextField z = new JTextField(existing != null ? String.valueOf(existing.getPosition().getZ()) : "10", 8);
            JSpinner intensity = new JSpinner(new SpinnerNumberModel(existing != null ? 100.0 : 100.0, 1.0, 1000.0, 1.0));
            JComboBox<String> colorCombo = new JComboBox<>(predefinedColors.keySet().toArray(new String[0]));
            if (existing != null) {
                ColorRGB color = existing.getColor();
                for (Map.Entry<String, ColorRGB> e : predefinedColors.entrySet()) {
                    if (e.getValue().equals(color)) { colorCombo.setSelectedItem(e.getKey()); break; }
                }
            } else { colorCombo.setSelectedItem("WHITE"); }

            int row = 0;
            addLabelAndField(dialog, gbc, row, "Position X:", x);
            addLabelAndField(dialog, gbc, ++row, "Position Y:", y);
            addLabelAndField(dialog, gbc, ++row, "Position Z:", z);
            addLabelAndField(dialog, gbc, ++row, "Intensity:", intensity);
            addLabelAndField(dialog, gbc, ++row, "Color:", colorCombo);

            final DecayingPointLight[] result = new DecayingPointLight[1];
            addButtons(dialog, gbc, ++row, "OK", "Cancel", 
                e -> { 
                    try {
                        ColorRGB c = predefinedColors.get(colorCombo.getSelectedItem());
                        result[0] = new DecayingPointLight(new Point3D(parseDouble(x.getText(), 0), parseDouble(y.getText(), -20), parseDouble(z.getText(), 10)), 
                            c != null ? c : ColorRGB.WHITE, ((Number)intensity.getValue()).doubleValue());
                        dialog.dispose();
                    } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, "Invalid value"); }
                }, e -> dialog.dispose());

            dialog.setSize(350, 280);
            dialog.setLocationRelativeTo(SceneEditorPanel.this);
            dialog.setVisible(true);
            return result[0];
        }
    }

    // ========== Table Model ==========

    static class SceneItem {
        private final String category;
        private final Object item;

        public SceneItem(String category, Object item) {
            this.category = category;
            this.item = item;
        }

        public String getCategory() { return category; }
        public Object getItem() { return item; }
        public String getType() { return item.getClass().getSimpleName(); }
        public String getDescription() { return item.toString(); }
    }

    class SceneItemTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private List<SceneItem> items = new ArrayList<>();
        private String[] columnNames = {"Category", "Type", "Description"};

        public void clear() { items.clear(); fireTableDataChanged(); }
        public void addItem(SceneItem item) { items.add(item); fireTableDataChanged(); }
        public void removeItem(int index) { if (index >= 0 && index < items.size()) { items.remove(index); fireTableDataChanged(); } }
        public void setItemAt(int index, SceneItem item) { if (index >= 0 && index < items.size()) { items.set(index, item); fireTableDataChanged(); } }
        public SceneItem getItemAt(int index) { return (index >= 0 && index < items.size()) ? items.get(index) : null; }

        @Override
        public int getRowCount() { return items.size(); }
        @Override
        public int getColumnCount() { return columnNames.length; }
        @Override
        public String getColumnName(int column) { return columnNames[column]; }
        @Override
        public Object getValueAt(int row, int column) {
            SceneItem item = items.get(row);
            return column == 0 ? item.getCategory() : column == 1 ? item.getType() : item.getDescription();
        }
    }
}
