package de.henrik.data;


import java.awt.Graphics;
import java.awt.Color;

public class RectangleData extends AbstractData {

    private final int width;
    private final int height;
    private final int x;
    private final int y;

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

    public int width() {
        return width;
    }

    public int height() {
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
        return ID;
    }

    @Override
    public String toString() {
        return "RectangleData{" + "ID=" + ID + ", width=" + width + ", height=" + height + '}';
    }

    public boolean intersects(RectangleData other) {
        return x < other.x + other.width && x + width > other.x &&
                y < other.y + other.height && y + height > other.y;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(100,100,100,0.2f));
        g.fillRect(x, y, width, height);
    }
}
