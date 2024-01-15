package de.henrik.data;

import javax.swing.JPanel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Could make this more efficient if we keep track of the used size, and if this size is big enough calculate all remaining spaces and used them for the fitting methods
 */
public class BoxData extends AbstractData {
    private final List<RectangleData> rectangles;
    private final int length;
    private JPanel panel = null;
    private int filledArea = 0;
    private int[][] box;

    private List<RectangleData> freeRectangles;
    private int recMinSize;


    public BoxData(int length, int recMinSize, List<RectangleData> rectangles) {
        this.rectangles = rectangles;
        this.length = length;
        box = new int[length][length];
        this.recMinSize = recMinSize;
    }

    public BoxData(int length, int recMinSize) {
        this(length, recMinSize, new ArrayList<>());
    }

    @Override
    public String toString() {
        return "BoxData{" + "rectangles=" + rectangles + ", length=" + length + '}';
    }

    /**
     * Inserts a rectangle into this box, if it fits
     *
     * @param rectangle the new rectangle
     * @return {@code true} if the rectangle was inserted, {@code false} otherwise, or if the rectangle was null
     */
    private boolean insertRectangle(RectangleData rectangle) {
        if (rectangle == null) return false;
        if (!canFit(rectangle)) {
            System.err.println("Rectangle intersects with other rectangles or is out of bounds");
            return false;
        }
        rectangles.add(rectangle);
        filledArea += rectangle.getSize();
        for (var recPoint : rectangle.getPoints()) {
            box[recPoint.y][recPoint.x] = rectangle.getID();
        }
        rectangle.setBoxData(this);

        //if we only have a certain space left we calculate all rectangles that are left within the box
        //if (filledArea > length * length * 0.9)
        //calculateFreeRectangles();
        //System.out.println(freeRectangles);
        return true;
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
        for (RectangleData rectangle : rectangles) {
            if (rectangle.intersects(newRectangle))
                return false;
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
        if (panel == null){
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
        return panel;
    }

    /**
     * Adds a rectangle to the box if it fits and repaints the panel
     *
     * @param rectangle the rectangle to add
     *             @return {@code true} if the rectangle was inserted, {@code false} otherwise
     */
    public boolean add(RectangleData rectangle) {
        var t = insertRectangle(rectangle);
        if (t && panel != null) panel.repaint();
        return t;
    }

    /**
     * Adds a rectangle to the first free position in this box and repaints the panel
     * @param rectangle the rectangle to add
     * @return {@code true} if the rectangle was inserted, {@code false} otherwise
     */
    public boolean addToFirstFreePosition(RectangleData rectangle) {
        var t = insertRectangle(getFirstFreePosition(rectangle));
        if (t&&panel!=null) panel.repaint();
        return t;
    }

    /**
     * Adds a rectangle to this box, ignoring if it fits. This does not call any intermediate logic of BoxData.
     * @param rectangle the rectangle to add
     */
    public void addForce(RectangleData rectangle) {
        rectangles.add(rectangle);
        rectangle.setBoxData(this);
        if (panel != null) panel.repaint();
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
            for (RectangleData rec : rectangles) {
                if (rectangleData1.intersects(rec)) {
                    intersects = true;
                    break;
                }
            }
            if (!intersects) list.add(new RectangleData(x, y, height, width));
        }
    }

    /**
     * Calculates the first free position for a rectangle. The returned rectangle can be turned by 90°.
     *
     * @param rectangleData  the rectangle to fit
     *
     * @return this given rectangle with a position that does not overlap with any other rectangle in this box, or null if there is no possible position
     */
    public RectangleData getFirstFreePosition(RectangleData rectangleData) {
        int width = rectangleData.getWidth();
        int height = rectangleData.getHeight();
        int[] minXPos = new int[length];
        //iterate over all possible starting positions of the new rectangle
        for (int x = 0; x < length - width; x++) {
            x += minXPos[x];
            for (int y = 0; y < length - height; y++) {
                rectangleData.setPosition(x, y);
                boolean intersects = false;
                for (var rec : rectangles) {
                    if (rectangleData.intersects(rec)) {
                        intersects = true;
                        y += rec.y() + rec.getHeight() - y - 1;
                        minXPos[x] = Math.max(minXPos[y], rec.x() + rec.getWidth());
                        break;
                    }
                }
                if (!intersects) return rectangleData;
            }
        }

        rectangleData.flip();
        minXPos = new int[length];
        for (int x = 0; x < length - height; x++) {
            x += minXPos[x];
            for (int y = 0; y < length - width; y++) {
                rectangleData.setPosition(x, y);
                boolean intersects1 = false;
                for (var rec : rectangles) {
                    if (rectangleData.intersects(rec)) {
                        intersects1 = true;
                        y += rec.y() + rec.getHeight() - y - 1;
                        break;
                    }
                }
                if (!intersects1) return rectangleData;
            }
        }
        return null;
    }

    /**
     * Calculates the first free position for a given width and height. The returned rectangle can be turned by 90°.
     *
     * @param width  the width for the new rectangle
     * @param height the height for the new rectangle
     * @return a rectangle that does not overlap with any other rectangle in this box, or null if there is no possible position
     */
    public RectangleData getFirstFreePosition(int width, int height) {
        RectangleData rectangleData = new RectangleData(0, 0, width, height);
        int[] minXPos = new int[length];
        //iterate over all possible starting positions of the new rectangle
        for (int x = 0; x < length - width; x++) {
            x += minXPos[x];
            for (int y = 0; y < length - height; y++) {
                rectangleData.setPosition(x, y);
                boolean intersects = false;

                //if it is free we check if it fits
                // TODO: 03.11.2023 use new box + hashmap (should be faster?)
                for (var rec : rectangles) {
                    if (rectangleData.intersects(rec)) {
                        intersects = true;
                        y += rec.y() + rec.getHeight() - y - 1;
                        minXPos[x] = Math.max(minXPos[y], rec.x() + rec.getWidth());
                        break;
                    }
                }
                if (!intersects) return rectangleData;
            }
        }

        rectangleData.flip();
        minXPos = new int[length];
        for (int x = 0; x < length - height; x++) {
            x += minXPos[x];
            for (int y = 0; y < length - width; y++) {
                rectangleData.setPosition(x, y);
                boolean intersects1 = false;
                for (var rec : rectangles) {
                    if (rectangleData.intersects(rec)) {
                        intersects1 = true;
                        y += rec.y() + rec.getHeight() - y - 1;
                        break;
                    }
                }
                if (!intersects1) return rectangleData;
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

    public void clear() {
        rectangles.clear();
        filledArea = 0;
        box = new int[length][length];
        if (panel != null) panel.repaint();
    }

    /**
     * @return a deep copy of this box
     */
    public BoxData copy() {
        var copy = new BoxData(length, recMinSize);
        copy.box = new int[length][length];
        copy.rectangles.addAll(rectangles.stream().map(RectangleData::copy).toList());
        copy.filledArea = filledArea;
        return copy;
    }

    /**
     * Recalculates the positions of all rectangles in this box in order of the list
     */
    public void recalculateLayout() {
        var recCopy = new ArrayList<>(rectangles);
        this.rectangles.clear();
        this.box = new int[length][length];
        this.filledArea = 0;
        for (var rec : recCopy) {
            addToFirstFreePosition(rec);
        }
    }

    /**
     * Recalculates the positions of all rectangles in this box in order of the list
     * and returns a list of all rectangles that did not fit into this box. these rectangles will not be in the list of the box anymore
     *
     * @return a list of all rectangles that did not fit into this box
     */
    public List<RectangleData> recalculateLayoutAndReturnOverfill() {
        var recCopy = new ArrayList<>(rectangles);
        var overfill = new ArrayList<RectangleData>();
        this.rectangles.clear();
        this.box = new int[length][length];
        this.filledArea = 0;
        for (var rec : recCopy) {
            var newPosition = getFirstFreePosition(rec);
            if (newPosition == null) {
                overfill.add(rec);
            } else {
                if (!add(rec)) {
                    System.err.println("Could not add rectangle. This should not happen!");
                }
            }
        }
        return overfill;
    }

    /**
     * Removes the rectangle at the given index from this box
     *
     * @param index the index of the rectangle to remove
     * @return the removed rectangle
     */
    public RectangleData remove(int index) {
        var rec = rectangles.remove(index);
        filledArea -= rec.getSize();
        for (var recPoint : rec.getPoints()) {
            box[recPoint.y][recPoint.x] = 0;
        }
        return rec;
    }

    /**
     * Removes the given rectangle from this box
     *
     * @param rec the rectangle to remove
     */
    public void remove(RectangleData rec) {
        rectangles.remove(rec);
        rec.setBoxData(null);
        filledArea -= rec.getSize();
        for (var recPoint : rec.getPoints()) {
            box[recPoint.y][recPoint.x] = 0;
        }
    }

    public List<RectangleData> getRectangles() {
        return rectangles;
    }

    public void loadData(BoxData boxData, boolean repaint) {
        rectangles.clear();
        rectangles.addAll(boxData.getRectangles());
        filledArea = boxData.getFilledArea();
        box = boxData.box;
        if (repaint) panel.repaint();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoxData boxData = (BoxData) o;

        if (length != boxData.length) return false;
        return rectangles.equals(boxData.rectangles);
    }

    @Override
    public int hashCode() {
        int result = rectangles.hashCode();
        result = 31 * result + length;
        return result;
    }

    public double getMaxIntersectionPercentage(RectangleData rec) {
        double maxIntersection = 0;
        for (var rec2 : rectangles) {
            maxIntersection = Math.max(maxIntersection, rec.intersectionPercentage(rec2));
        }
        return maxIntersection;
    }

    public int getRecMinSize() {
        return recMinSize;
    }
}

