package de.henrik.data;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class QuadTree2D {
    private int length;
    private int MAX_ITEMS_PER_NODE = 2;
    private boolean allowOverlapping = false;

    private final QuadNode root;

    public QuadTree2D(int length) {
        this.length = length;
        root = new QuadNode(null, 0, 0, length, length, 0);
    }

    public void insertRectangle(RectangleData rectangleData) {
        root.insertRectangle(rectangleData);
    }

    public void setAllowOverlapping(boolean allowOverlapping) {
        this.allowOverlapping = allowOverlapping;
    }

    class QuadNode {
        int x, y, w, h, l;
        QuadNode parent = null;
        QuadNode UL = null, UR = null, LL = null, LR = null;
        List<RectangleData> rectangleData = null;

        QuadNode(QuadNode parent, int x, int y, int w, int h, int l) {
            this.parent = parent;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.l = l;
        }

        public boolean isLeaf() {
            return UL == null && UR == null && LL == null && LR == null;
        }

        public int getLayer() {
            return l;
        }

        @Override
        public String toString() {
            var sb = new StringBuilder();
            sb.append("QuadNode{");
            if (this.rectangleData != null) {
                sb.append(" Data=").append(rectangleData);
            }
            if (!this.isLeaf()) {
                sb.append("\nUL=").append(UL);
                sb.append("\nUR=").append(UR);
                sb.append("\nLL=").append(LL);
                sb.append("\nLR=").append(LR);
            } else {
                sb.append(" Coords=").append("(").append(x).append("|").append(y).append("|").append(w).append("|").append(h);
            }
            sb.append("}");
            return sb.toString();
        }

        public void paint(Graphics g) {
            int scale = 1;
            g.setColor(Color.BLACK);
            g.drawRect(this.x * scale, this.y * scale, this.w * scale, this.h * scale);
            g.setColor(new Color(10, 10, 10, 200 + this.getLayer()));
            g.fillRect((this.x + 1) * scale, (this.y + 1) * scale, (this.w - 1) * scale, (this.h - 1) * scale);

            if (!this.isLeaf()) {
                this.UL.paint(g);
                this.UR.paint(g);
                this.LL.paint(g);
                this.LR.paint(g);
            }
            if (this.isLeaf() && this.rectangleData != null) {
                g.setColor(Color.ORANGE);
                for (var rec : rectangleData) {
                    g.fillRect((rec.x() + 1) * scale, (rec.y() + 1) * scale, (rec.getWidth() - 1) * scale, (rec.getHeight() - 1) * scale);

                }
            }
        }

        public boolean canFit(RectangleData rectangleData) {
            if (allowOverlapping) {
                return true;
            }
            for (RectangleData r : this.rectangleData) {
                if (r.intersects(rectangleData)) {
                    return false;
                }
            }
            return true;
        }

        public void insertRectangle(RectangleData rectangle) {
            if (this.isLeaf()) {
                if (this.rectangleData == null) {
                    this.rectangleData = new ArrayList<>();
                } else if (!this.canFit(rectangle)) {
                    throw new IllegalArgumentException("Rectangle does not fit in QuadTree");
                }
                this.rectangleData.add(rectangle);
                if (this.rectangleData.size() > MAX_ITEMS_PER_NODE) {
                    this.split();
                }
            } else {
                if (rectangle.intersects(UL.rectangleBounds())) {
                    this.UL.insertRectangle(rectangle);
                }
                if (rectangle.intersects(UR.rectangleBounds())) {
                    this.UR.insertRectangle(rectangle);
                }
                if (rectangle.intersects(LL.rectangleBounds())) {
                    this.LL.insertRectangle(rectangle);
                }
                if (rectangle.intersects(LR.rectangleBounds())) {
                    this.LR.insertRectangle(rectangle);
                }
            }
        }

        private void split() {
            //we have now 3 rectangles in this node
            this.UL = new QuadNode(this, x, y, w / 2, h / 2, l + 1);
            this.UR = new QuadNode(this, this.x + w / 2, y, this.w / 2, h / 2, l + 1);
            this.LL = new QuadNode(this, x, this.y + h / 2, w / 2, this.h / 2, l + 1);
            this.LR = new QuadNode(this, this.x + w / 2, this.y + h / 2, this.w / 2, this.h / 2, l + 1);
            for (RectangleData rectangle : this.rectangleData) {
                insertRectangle(rectangle);
            }
        }

        private RectangleData rectangleBounds() {
            return new RectangleData(x, y, w, h);
        }
    }

    public void paint(Graphics g) {
        root.paint(g);
    }

    public String toString() {
        return "QuadTree{" + "length=" + length + ", root=" + root + '}';
    }


    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame();
        var tree = new QuadTree2D(1000);
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                tree.paint(g);
            }
        };
        frame.add(panel);


        frame.setVisible(true);
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        tree.insertRectangle(new RectangleData(500, 500, 100, 100));
        panel.repaint();
        sleep(1000);
        tree.insertRectangle(new RectangleData(0, 0, 100, 100));
        panel.repaint();
        sleep(1000);
        tree.insertRectangle(new RectangleData(400, 500, 100, 100));
        panel.repaint();
        sleep(1000);
        tree.insertRectangle(new RectangleData(20, 500, 3, 3));
        tree.insertRectangle(new RectangleData(25, 500, 3, 3));
        tree.insertRectangle(new RectangleData(28, 520, 3, 3));
        tree.insertRectangle(new RectangleData(35, 510, 3, 3));
        tree.insertRectangle(new RectangleData(40, 500, 3, 3));
        tree.insertRectangle(new RectangleData(45, 510, 3, 3));

        tree.insertRectangle(new RectangleData(324,781,12,80));
        panel.repaint();
        sleep(1000);
        panel.repaint();
        System.out.println(tree);
    }

}
