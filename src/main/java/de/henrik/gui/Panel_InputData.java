package de.henrik.gui;

import de.henrik.data.RectangleData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Panel_InputData extends JPanel {

    private final HashMap<Shape, List<RectangleData>> rectangles;
    private final HashMap<Shape, RectanglePanel> panels;

    public Panel_InputData() {
        rectangles = new HashMap<>();
        panels = new HashMap<>();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void addRectangle(RectangleData rectangleData) {
        var rectangle = new java.awt.Rectangle(rectangleData.width(), rectangleData.height());
        rectangle.x = 3;
        rectangle.y = 3;
        if (rectangles.containsKey(rectangle)) {
            rectangles.get(rectangle).add(rectangleData);
            panels.get(rectangle).updateCount();
            panels.get(rectangle).repaint();
        } else {
            var list = new ArrayList<RectangleData>();
            list.add(rectangleData);
            rectangles.put(rectangle, list);

            var newRectanglePanel = new RectanglePanel(rectangle);
            panels.put(rectangle, newRectanglePanel);
            this.add(newRectanglePanel);
            validate();
            revalidate();
        }
    }

    public void addRectangles(List<RectangleData> rectangleData) {
        for (RectangleData data : rectangleData) {
            addRectangle(data);
        }
    }

    public void reset() {
        rectangles.clear();
        panels.clear();
        this.removeAll();
    }
}

class RectanglePanel extends JPanel {
    private final Shape rectangle;
    private final JLabel label;
    private int count;

    public RectanglePanel(Shape rectangle) {
        if (rectangle == null) {
            throw new IllegalArgumentException("Rectangle must not be null");
        }
        this.rectangle = rectangle;
        this.label = new JLabel();
        this.add(label);
        setCount(1);
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
    }

    @Override
    public void validate() {
        System.out.println("validating " + rectangle);
        if (rectangle != null && this.getGraphics() != null) {
            this.setMinimumSize(new Dimension(rectangle.getBounds().width + 6 + 3 + this.getGraphics().getFontMetrics().stringWidth(label.getText()), Math.max(rectangle.getBounds().height , 20)));
            this.setPreferredSize(getMinimumSize());
            System.out.println("Revalidating " + rectangle + " new MinimumSize: " + this.getMinimumSize());
        } super.validate();
    }

    public void updateCount() {
        count++;
        label.setText(count + " x : (" + rectangle.getBounds().width + " x " + rectangle.getBounds().height + ") ");
    }

    public void setCount(int i) {
        count = i;
        label.setText(count + " x : (" + rectangle.getBounds().width + " x " + rectangle.getBounds().height + ") ");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.DARK_GRAY);
        ((Graphics2D) g).fill(rectangle);
    }
}

