package de.henrik.data;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

public class Box extends AbstractData {
    private List<RectangleData> rectangles;
    private final int length;

    public Box(int length) {
        rectangles = new ArrayList<>();
        this.length = length;
    }

    public void insertRectangle(RectangleData newRectangle) {
        if (isFreePosition(newRectangle)) {
            rectangles.add(newRectangle);
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
        Box rtree = new Box(10);

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
                for (RectangleData rectangle : rectangles) {
                    rectangle.draw(g);
                }
            }
        };
    }

}

