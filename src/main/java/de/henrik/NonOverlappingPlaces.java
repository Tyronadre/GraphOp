package de.henrik;

import de.henrik.data.QuadTree;
import de.henrik.data.QuadTree2D;
import de.henrik.data.RectangleData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class NonOverlappingPlaces {

    public static void main(String[] args) throws InterruptedException {
        var q = new QuadTree2D(100);
        q.insertRectangle(new RectangleData(0, 0, 10, 10));
        System.out.println(q);
        q.insertRectangle(new RectangleData(10,0,10,10));
        System.out.println(q);
        q.insertRectangle(new RectangleData(0,10,10,10));
        System.out.println(q);
        q.insertRectangle(new RectangleData(10,10,10,10));


        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                q.paint(g);
            }
        };


        JFrame frame = new JFrame("NonOverlappingPlaces");
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,1000);

        frame.setVisible(true);
        sleep(100);
        System.out.println("done");
    }
}
