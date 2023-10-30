package de.henrik.data;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoxData extends AbstractData implements DataStructure<RectangleData> {
    private List<RectangleData> rectangles;
    private final int length;

    public BoxData(int length) {
        rectangles = new ArrayList<>();
        this.length = length;
    }

    public void insertRectangle(RectangleData newRectangle) {
        if (isFreePosition(newRectangle)) {
            rectangles.add(newRectangle);
        } else {
            throw new IllegalArgumentException("Rectangle intersects with existing rectangles");
        }
    }

    public boolean isFreePosition(RectangleData newRectangle) {
        for (RectangleData existingRectangle : rectangles) {
            if (newRectangle.intersects(existingRectangle)) {
                return false;
            }
        }
        return true;
    }

    public List<RectangleData> getFreePositions() {
        List<RectangleData> freePositions = new ArrayList<>();

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                RectangleData potentialPosition = new RectangleData(x, y, 1, 1);
                if (isFreePosition(potentialPosition)) {
                    freePositions.add(potentialPosition);
                }
            }
        }
        return freePositions;
    }

    public static void main(String[] args) {
        BoxData rtree = new BoxData(10);

        // Insert rectangles of different sizes
        rtree.insertRectangle(new RectangleData(1,1,0,0));
        rtree.insertRectangle(new RectangleData(3,3,1,0));
        rtree.insertRectangle(new RectangleData(3,4,2,0));

        // Get free positions in the square
        List<RectangleData> freePositions = rtree.getFreePositions();

        // Display free positions
        for (RectangleData position : freePositions) {
            System.out.println("Free Position: x=" + position.x() + ", y=" + position.y());
        }
    }

    public JPanel getPanel() {
        return new JPanel() {
            @Override
            public void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                var g2d = (Graphics2D) g;
                g2d.setColor(Color.BLACK);
                g2d.drawRect(3,3,length,length);
                g2d.setColor(new Color(200,200,100,0.2f));
                for (RectangleData rectangle : rectangles) {
                    rectangle.draw(g2d);
                }
            }
        };
    }

    @Override
    public void add(RectangleData data) {
        insertRectangle(data);
    }
}

