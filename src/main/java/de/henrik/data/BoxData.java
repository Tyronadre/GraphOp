package de.henrik.data;

import javax.swing.JPanel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Could make this more efficient if we keep track of the used size, and if this size is big enough calculate all remaining spaces and used them for the fitting methods
 */
public class BoxData extends AbstractData implements DataStructure<RectangleData> {
    private final HashMap<Integer, RectangleData> rectangles;
    private final int length;
    private final JPanel panel;
    private int filledArea = 0;
    private int[][] box;

    private List<RectangleData> freeRectangles;


    public BoxData(int length, int recMinSize) {
        rectangles = new HashMap<>();
        this.length = length;
        box = new int[length][length];
        panel = new JPanel() {
            @Override
            public void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                var g2d = (Graphics2D) g;
                g2d.setColor(Color.BLACK);
                g2d.drawRect(0, 0, length, length);
                g2d.setColor(new Color(200, 200, 100, 20));
                var recCopy = new ArrayList<>(rectangles.values());
                for (RectangleData rectangle : recCopy) {
                    rectangle.draw(g2d);
                }
            }

        };
        panel.setMinimumSize(new Dimension(length + 1, length + 1));
        panel.setPreferredSize(new Dimension(length + 1, length + 1));
    }

    @Override
    public String toString() {
        return "BoxData{" + "rectangles=" + rectangles + ", length=" + length + '}';
    }

    /**
     * Inserts a rectangle into this box
     *
     * @param newRectangle the new rectangle
     */
    private void insertRectangle(RectangleData newRectangle) {
        if (!canFit(newRectangle)) {
            System.err.println("Rectangle intersects with other rectangles or is out of bounds");
        }
        rectangles.put(newRectangle.getID(), newRectangle);
        filledArea += newRectangle.getSize();
        for (var recPoint : newRectangle.getPoints()) {
            box[recPoint.y][recPoint.x] = newRectangle.getID();
        }
        newRectangle.setBoxData(this);

        //if we only have a certain space left we calculate all rectangles that are left within the box
        //if (filledArea > length * length * 0.9)
        calculateFreeRectangles();
        System.out.println(freeRectangles);
    }

    /**
     * Calculates if a given rectangle can fit into this box without overlapping with any other
     *
     * @param newRectangle the new rectangle to fit
     * @return {@code true} if it can fit, {@code false} otherwise
     */
    public boolean canFit(RectangleData newRectangle) {
        //test if out of bounds
        if (newRectangle.x() + newRectangle.getWidth() >= length || newRectangle.y() + newRectangle.getHeight() >= length)
            return false;
        //fast test if a corner is intersecting
        if (box[newRectangle.y()][newRectangle.x()] != 0 ||
                box[newRectangle.y()][newRectangle.x() + newRectangle.getWidth()] != 0 ||
                box[newRectangle.y() + newRectangle.getHeight()][newRectangle.x()] != 0 ||
                box[newRectangle.y() + newRectangle.getHeight()][newRectangle.x() + newRectangle.getWidth()] != 0) {
            return true;
        }
        //test with each rectangle if it intersects (this is not very fast)
        for (RectangleData rectangle : rectangles.values()) {
            if (rectangle.intersects(newRectangle)) return false;
        }
        return true;
    }

    /**
     * @return the filled area as value
     */
    public int getFilledArea() {
        return filledArea;
    }

    /**
     * @return the filled area as percentage of the whole box
     */
    public double getPercentageFilled() {
        return filledArea / Math.pow(this.length, 2);
    }

    /**
     * @return the jpanel for painting this box
     */
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void add(RectangleData data) {
        insertRectangle(data);
        panel.repaint();
    }

    @Override
    public int evaluate() {
        return 0;
    }


    /**
     * Calculates a list of all free positions for a given width and height. This method is not optimized!
     *
     * @param width  the width for the new rectangle
     * @param height the height for the new rectangle
     * @return a list of all possible (x|y) points for the new rectangle
     */
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
            for (RectangleData rec : rectangles.values()) {
                if (rectangleData1.intersects(rec)) {
                    intersects = true;
                    break;
                }
            }
            if (!intersects) list.add(new RectangleData(x, y, height, width));
        }
    }

    /**
     * Calculates the first free position for a given width and height. This is semi optimized!
     *
     * @param width  the width for the new rectangle
     * @param height the height for the new rectangle
     * @return a rectangle that does not overlap with any other rectangle in this box, or null if there is no possible position
     */
    public RectangleData getFirstFreePosition(int width, int height) {
        RectangleData rectangleData = new RectangleData(0, 0, width, height);
        RectangleData rectangleData1 = new RectangleData(0, 0, height, width);

        //iterate over all possible starting positions of the new rectangle
        for (int x = 0; x <= length - width; x++) {
            for (int y = 0; y <= length - height; y++) {

                rectangleData.setPosition(x, y);
                boolean intersects = false;

                //if it is free we check if it fits
                // TODO: 03.11.2023 use new box + hashmap (should be faster?)
                for (var rec : rectangles.values()) {
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
                for (var rec : rectangles.values()) {
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

    /**
     * @return the side length of the box
     */
    public int getLength() {
        return length;
    }

    private void calculateFreeRectangles() {
        var freeRectangles = new ArrayList<RectangleData>();
        var recData = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> recDataLine;
        int lastRecID = -1, emptyRecWidth = 0;
        for (int y = 0; y < length; y++) {
            recDataLine = new ArrayList<>();
            var recDataPointer = 0;
            for (int x = 0; x < length; x++) {
                if (box[y][x] == lastRecID) {
                    emptyRecWidth++;
                } else {
                    lastRecID = box[y][x];
                    recDataLine.add(box[y][x]);
                    recDataLine.add(x);
                    if (lastRecID != 0) {
                        recDataLine.add(rectangles.get(lastRecID).getWidth());
                        x += rectangles.get(lastRecID).getWidth();
                    } else {
                        emptyRecWidth = 1;
                    }
                }
            }
            if (lastRecID == 0) {
                recDataLine.add(emptyRecWidth);
            }
            recData.add(recDataLine);
            emptyRecWidth = 0;
            lastRecID = -1;
        }
        recData.forEach(System.out::println);

        /*
        var freeRectangles = new ArrayList<RectangleData>();
        RectangleData lastFreeRec = null;
        for (int y = 0; y < length - 10; y++) {
            for (int x = 0; x < length - 10; x++) {
                int coordData = box[y][x];

                //There is a rectangle at this point in the box
                if (coordData != 0) {
                    x += rectangles.get(coordData).getWidth() - 1;
                }
                //There is no rectangle here, and this is the first empty spot we found
                else if (lastFreeRec == null) {
                    lastFreeRec = new RectangleData(x, y, 1, 1);
                    freeRectangles.add(lastFreeRec);
                }
                //There is no rectangle here and the last empty spot is on the right
                else if (lastFreeRec.x() + lastFreeRec.getWidth() == x) {
                    lastFreeRec.setWidth(lastFreeRec.getWidth() + 1);
                }
                //There is no rectangle here and the last empty spot is not on the right
                else if (lastFreeRec.x() + lastFreeRec.getWidth() != x) {
                    //check if we have a rectangle that is over this one
                    boolean foundRec = false;
                    for (var freeRec : freeRectangles) {
                        if (x >= freeRec.x() && x < freeRec.getWidth() + freeRec.x() && freeRec.y() == y - 1) {
                            lastFreeRec.setHeight(lastFreeRec.getHeight() + 1);
                            foundRec = true;
                            break;
                        }
                    }
                    // we didnt find any rectangle so we create a new one
                    if (!foundRec) {
                        lastFreeRec = new RectangleData(x, y, 1, 1);
                        freeRectangles.add(lastFreeRec);
                    }
                } else {
                    throw new RuntimeException("How did we get here?");
                }
            }
        }
        freeRectangles.removeIf(rec -> rec.getWidth() < 10 || rec.getSize() < 10);
        Collections.sort(freeRectangles);*/


    }

    private static final class RecData {
        private final int id;
        private final int x0;
        private int width;

        private RecData(int id, int x0, int width) {
            this.id = id;
            this.x0 = x0;
            this.width = width;
        }

        public int id() {
            return id;
        }

        public int x0() {
            return x0;
        }

        public int width() {
            return width;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (RecData) obj;
            return this.id == that.id &&
                    this.x0 == that.x0 &&
                    this.width == that.width;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, x0, width);
        }

        @Override
        public String toString() {
            return "RecData[" +
                    "id=" + id + ", " +
                    "x0=" + x0 + ", " +
                    "width=" + width + ']';
        }
    }

}

