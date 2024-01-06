package de.henrik.gui;

import de.henrik.data.RectangleData;
import de.henrik.generator.Generator;
import de.henrik.generator.RectangleDataGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Panel_InputData extends JPanel {

    private final HashMap<Dimension, RectanglePanel> panels;

    private RectangleDataGenerator generator;

    private int boxWidth;

    public Panel_InputData() {
        panels = new HashMap<>();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void addRectangle(RectangleData rectangleData) {
        var dimension = new Dimension(rectangleData.getWidth(), rectangleData.getHeight());
        if (panels.containsKey(dimension)) {
            panels.get(dimension).increaseCount();
            panels.get(dimension).repaint();
        } else {
            var newRectanglePanel = new RectanglePanel(dimension);
            panels.put(dimension, newRectanglePanel);
            this.add(newRectanglePanel);
            validate();
        }
    }

    public void removeRectangle(RectangleData rectangle) {
        var dimension = new Dimension(rectangle.getWidth(), rectangle.getHeight());
        if (panels.containsKey(dimension)) {
            var panel = panels.get(dimension);
            panel.decreaseCount();
            if (panel.getCount() == 0) {
                panels.remove(dimension);
                this.remove(panel);
                validate();
            } else
                panel.repaint();
        }
    }

    public void addRectangles(List<RectangleData> rectangleData) {
        for (RectangleData data : rectangleData) {
            addRectangle(data);
        }
    }

    public void reset() {
        panels.clear();
        this.removeAll();
        this.generator = null;
    }

    public void setGenerator(RectangleDataGenerator generator) {
        this.generator = generator;
    }

    public List<RectangleData> getData() {
        if (generator == null) {
            JOptionPane.showMessageDialog(this, "Generator must not be null", "Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException("Generator must not be null");
        } else {
            return generator.getData();
        }
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public void setBoxWidth(int boxWidth) {
        this.boxWidth = boxWidth;
    }


    public int getRecMinSize() {
        return generator.REC_MIN_SIZE;
    }

    public void clear() {
        panels.clear();
        this.removeAll();
        validate();
    }
}

class RectanglePanel extends JPanel {
    private final Shape rectangle;
    private final JLabel label;
    private int count;

    public RectanglePanel(Dimension dimension) {
        if (dimension == null) {
            throw new IllegalArgumentException("Dimension must not be null");
        }
        this.rectangle = new Rectangle(3, 3, dimension.width, dimension.height);
        this.label = new JLabel();
        this.add(label);
        setCount(1);
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
    }

    @Override
    public void validate() {
        if (rectangle != null && this.getGraphics() != null) {
            this.setMinimumSize(new Dimension(rectangle.getBounds().width + 25, Math.max(rectangle.getBounds().height + 6, 25)));
            this.setPreferredSize(getMinimumSize());
        }
        super.validate();
    }

    public void increaseCount() {
        count++;
        label.setText(count + " x : (" + rectangle.getBounds().width + " x " + rectangle.getBounds().height + ") ");
    }

    public void decreaseCount() {
        count--;
        label.setText(count + " x : (" + rectangle.getBounds().width + " x " + rectangle.getBounds().height + ") ");
    }

    public int getCount() {
        return count;
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

