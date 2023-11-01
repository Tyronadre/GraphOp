package de.henrik.data;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Could make this more efficient if we keep track of the used size, and if this size is big enough calculate all remaining spaces and used them for the fitting methods
 */
public class BoxData extends AbstractData implements DataStructure<RectangleData> {
    private final List<RectangleData> rectangles;
    private final int length;
    private final JPanel panel;
    private int filledArea = 0;

    @Override
    public String toString() {
        return "BoxData{" + "rectangles=" + rectangles + ", length=" + length + '}';
    }

    public BoxData(int length) {
        rectangles = new ArrayList<>();
        this.length = length;
        panel = new JPanel() {
            @Override
            public void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                var g2d = (Graphics2D) g;
                g2d.setColor(Color.BLACK);
                g2d.drawRect(0, 0, length, length);
                g2d.setColor(new Color(200, 200, 100, 20));
                var recCopy = new ArrayList<>(rectangles);
                for (RectangleData rectangle : recCopy) {
                    rectangle.draw(g2d);
                }
            }

        };
        panel.setMinimumSize(new Dimension(length + 1, length + 1));
        panel.setPreferredSize(new Dimension(length + 1, length + 1));
    }

    public void insertRectangle(RectangleData newRectangle) {
        if (!canFit(newRectangle)) {
            System.err.println("Rectangle intersects with other rectangles or is out of bounds");
        }
        rectangles.add(newRectangle);
        filledArea += newRectangle.getSize();
        newRectangle.setBoxData(this);
    }

    private boolean canFit(RectangleData newRectangle) {
        if (newRectangle.x() + newRectangle.getWidth() > length || newRectangle.y() + newRectangle.getHeight() > length)
            return false;
        for (RectangleData rectangle : rectangles) {
            if (rectangle.intersects(newRectangle)) return false;
        }
        return true;
    }

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void add(RectangleData data) {
        insertRectangle(data);
        panel.repaint();
    }

    public List<RectangleData> getFreePositions(int width, int height) {
        RectangleData rectangleData = new RectangleData(0, 0, width, height);
        RectangleData rectangleData1 = new RectangleData(0, 0, height, width);
        var list = new ArrayList<RectangleData>();
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                getFreePostionsHelper(height, width, rectangleData, list, x, y);
                getFreePostionsHelper(width, height, rectangleData1, list, x, y);
            }
        }
        return list;
    }

    private void getFreePostionsHelper(int width, int height, RectangleData rectangleData1, ArrayList<RectangleData> list, int x, int y) {
        if (x + height <= length && y + width <= length) {
            rectangleData1.setPosition(x, y);
            boolean intersects = false;
            for (RectangleData rec : rectangles) {
                if (rectangleData1.intersects(rec)) {
                    intersects = true;
                    break;
                }
            }
            if (!intersects) list.add(new RectangleData(x, y, height, width));
        }
    }

    public RectangleData getFirstFreePosition(int width, int height) {
        RectangleData rectangleData = new RectangleData(0, 0, width, height);
        RectangleData rectangleData1 = new RectangleData(0, 0, height, width);

        //iterate over all possible starting positions of the new rectangle
        for (int x = 0; x <= length - width; x++) {
            for (int y = 0; y <= length - height; y++) {

                rectangleData.setPosition(x, y);
                boolean intersects = false;

                //if it is free we check if it fits
                for (var rec : rectangles) {
                    if (rectangleData.intersects(rec)) {
                        intersects = true;
                        y += rec.y() + rec.getHeight() - y - 1;
                        break;
                    }
                }
                if (!intersects) return rectangleData;
            }
        }

        for (int x = 0; x <= length - height; x++) {
            for (int y = 0; y <= length - width; y++) {
                rectangleData1.setPosition(x, y);
                boolean intersects1 = false;
                for (var rec : rectangles) {
                    if (rectangleData1.intersects(rec)) {
                        intersects1 = true;
                        y += rec.y() + rec.getHeight() - y - 1;
                        break;
                    }
                }
                if (!intersects1) return rectangleData1;
            }
        }
        return null;
    }

    public int getLength() {
        return length;
    }

}

