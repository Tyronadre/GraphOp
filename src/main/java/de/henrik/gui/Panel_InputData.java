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
            System.out.println(newRectanglePanel.getSize());
            this.add(newRectanglePanel);
            System.out.println(newRectanglePanel.getSize());
        }
    }

    public void addRectangles(List<RectangleData> rectangleData) {
        for (RectangleData data : rectangleData) {
            addRectangle(data);
        }
        this.revalidate();
        System.out.println(this.getSize());

    }

    public void reset() {
        rectangles.clear();
        panels.clear();
        this.removeAll();
    }



    @Override
    public void repaint() {
        super.repaint();

    }
}

class RectanglePanel extends JPanel {
    private final Shape rectangle;
    private int count;
    private final JLabel label;
    public RectanglePanel(Shape rectangle) {
        this.rectangle = rectangle;
        this.label = new JLabel();
        this.add(label);
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        this.setMinimumSize(new Dimension(rectangle.getBounds().width, rectangle.getBounds().height));
        this.setSize(getMinimumSize());
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        setCount(1);
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

