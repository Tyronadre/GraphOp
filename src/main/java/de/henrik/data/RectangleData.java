package de.henrik.data;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RectangleData extends AbstractData implements Comparable<RectangleData> {

    private int width;
    private int height;
    private int x;
    private int y;
    private BoxData boxData;



    public RectangleData(int width, int height) {
        this.width = width;
        this.height = height;
        this.x = 0;
        this.y = 0;
    }

    public RectangleData(int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public int hashCode() {
        return (10000 * width + height) + (10000 * x + y);
    }

    @Override
    public String toString() {
        return "Rec{" + "(" + x + "|" + y + "), w=" + width + ", h=" + height + '}';
    }

    public boolean intersects(RectangleData other) {
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(100,100,100,20));
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

    }

    public List<Point> getPoints() {
        var list = new ArrayList<Point>();
        for (int x = this.x; x < this.x + width; x++) {
            for (int y = this.y; y < this.y + height; y++) {
                list.add(new Point(x, y));
            }
        }
        return list;
    }

    public boolean containsPoint(Point point) {
        return point.x >= x && point.x < x + width && point.y >= y && point.y < y + height;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(Point point) {
        setPosition(point.x, point.y);
    }

    public int getSize() {
        return width * height;
    }

    public BoxData getBoxData() {
        return boxData;
    }

    public void setBoxData(BoxData boxData) {
        this.boxData = boxData;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public Dimension getDimension() {
        return new Dimension(width, height);
    }

    public void setDimension(Dimension dimension) {
        this.width = dimension.width;
        this.height = dimension.height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int heigth){
        this.height = heigth;
    }

    @Override
    public int compareTo(RectangleData o) {
        return this.getSize() - o.getSize();
    }

    public RectangleData copy() {
        return new RectangleData(x, y, width, height);
    }

    public void flip() {
        var temp = width;
        width = height;
        height = temp;
    }

    public double intersectionPercentage(RectangleData other) {
        var intersectionWidth = Math.min(x + width, other.x + other.width) - Math.max(x, other.x);
        var intersectionHeight = Math.min(y + height, other.y + other.height) - Math.max(y, other.y);
        var intersectionArea = intersectionWidth * intersectionHeight;
        var maxArea = Math.max(width * height, other.width * other.height);
        return (double) intersectionArea / maxArea;
    }


    /**
     * Moves the rectangle by x and y, if it is possible without going out of bounds of the box
     * @param x x
     * @param y y
     * @return {@code true} if the rectangle was moved, {@code false} otherwise
     */
    public boolean move(int x, int y) {
        int oldX = this.x;
        int oldY = this.y;
        this.x += x;
        this.y += y;
        if (this.x < 0 || this.y < 0 || this.x + width > boxData.getLength() || this.y + height > boxData.getLength()) {
            this.x = oldX;
            this.y = oldY;
            return false;
        }
        return true;
    }

}
