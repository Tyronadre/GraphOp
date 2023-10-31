package de.henrik.data;

import java.awt.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuadTree {
    private int length;
    private final QuadNode root;

    public QuadTree(int length) {
        this.length = length;
        root = new QuadNode(null, 0, 0, length, length, 0);
    }

    public void insertRectangle(RectangleData rectangleData) {
        root.insertRectangle(rectangleData);
    }

    @Override
    public String toString() {
        return "QuadTree{" + "length=" + length + ", root=" + root + '}';
    }

    static class QuadNode {
        QuadNode parent = null;
        QuadNode UL = null, UR = null, LL = null, LR = null;
        int x, y, w, h, l;
        public RectangleData rectangleData = null;

        QuadNode(QuadNode parent, int x, int y, int w, int h, int l) {
            this.parent = parent;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.l = l;
        }

        public void insertRectangle(RectangleData rectangleData) {
            if (rectangleData == null) {
                throw new IllegalStateException("rectangleData is null");
            }
            //The given rectangle fill this node fully and this node does not have any data yet
            if (this.x >= rectangleData.x() && this.y >= rectangleData.y() && this.x + this.w <= rectangleData.x() + rectangleData.getWidth() && this.y + this.h <= rectangleData.y() + rectangleData.getHeight()) {
                if (!this.isLeaf() && this.rectangleData == null) {
                    throw new RuntimeException("this node is already occupied");
                } else {
                    this.rectangleData = rectangleData;
                    return;
                }
            }
            //we need to know if the rectangle overlaps on one or more of the quadrants and then adjust each accordingly
            //UpperLeft
            if (this.x >= rectangleData.x() && this.y >= rectangleData.y()) {
                if (this.UL == null)
                    this.UL = new QuadNode(this, x, y,  rectangleData.getWidth() + rectangleData.x() - x, rectangleData.getHeight() + rectangleData.y() - y, l + 1);
                this.UL.insertRectangle(rectangleData);
            }
            //UpperRight
            if (this.x < rectangleData.x() && this.y >= rectangleData.y()) {
                if (this.UR == null)
                    if (x + w - rectangleData.getWidth() - rectangleData.x() < 0)
                        //over the edge
                        split(x, y, rectangleData.getWidth() + rectangleData.x() - x, rectangleData.getHeight() + rectangleData.y() - y);
                    else {
                        //inside of node
                        UR = new QuadNode(this, rectangleData.x(), y, w - rectangleData.getWidth(), rectangleData.getHeight() + rectangleData.y() - y, l + 1);
                        LR = new QuadNode(this, rectangleData.x(), rectangleData.y(), UR.w , h - UR.h, l+1);
                    }
                this.UR.insertRectangle(rectangleData);
            }
            //LowerLeft
            if (this.x >= rectangleData.x() && this.y < rectangleData.y()) {
                if (this.isLeaf())
                    split(x, y, rectangleData.getWidth() + rectangleData.x() - x, h - rectangleData.getHeight());
                this.LL.insertRectangle(rectangleData);
            }
            //LowerRight
            if (this.x < rectangleData.x() && this.y < rectangleData.y()) {
                if (this.isLeaf()) split(x, y, rectangleData.x() - x, h - rectangleData.getHeight());
                this.LR.insertRectangle(rectangleData);
            }


        }

        private void split(int x, int y, int w, int h) {
            this.UL = new QuadNode(this, x, y, w, h, l + 1);
            this.UR = new QuadNode(this, this.x + w, y, this.w - w, h, l + 1);
            this.LL = new QuadNode(this, x, this.w - h, w, this.h - h, l + 1);
            this.LR = new QuadNode(this, this.x + w, this.y + h, this.w - w, this.h - h, l + 1);
        }

        private boolean isLeaf() {
            return UL == null || UR == null || LL == null || LR == null;
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
            int scale = 100;
            g.setColor(Color.BLACK);
            g.drawRect(this.x * scale, this.y * scale, this.w * scale, this.h * scale);
            g.setColor(new Color(50, 50, 50, (20 * this.getLayer())));
            g.fillRect((this.x + 1) * scale, (this.y + 1) * scale, (this.w - 1) * scale, (this.h - 1) * scale);

            if (!this.isLeaf()) {
                this.UL.paint(g);
                this.UR.paint(g);
                this.LL.paint(g);
                this.LR.paint(g);
            }
            if (this.isLeaf() && this.rectangleData != null) {
                g.setColor(Color.ORANGE);
                g.fillRect((this.x + 1) * scale, (this.y + 1) * scale, (this.w - 1) * scale, (this.h - 1) * scale);
            }
        }
    }

    public void paint(Graphics g) {
        root.paint(g);
    }

}