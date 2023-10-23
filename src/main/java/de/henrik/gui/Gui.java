package de.henrik.gui;

import javax.swing.*;
import java.awt.*;

public class Gui {
    private JPanel contentPanel;
    private JButton createInstance;
    private JTextField boxLength;
    private JTextField lowerBoundRec;
    private JTextField upperBoundRec;

    public Gui() {
        JFrame frame = new JFrame("Gui");
        frame.setContentPane(contentPanel);
        frame.add(createInputPanel(frame));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel createInputPanel(JFrame frame) {
        createInstance = new JButton("Create Instance");
        createInstance.addActionListener(e -> {
            int boxLength = Integer.parseInt(this.boxLength.getText());
            int lowerBoundRec = Integer.parseInt(this.lowerBoundRec.getText());
            int upperBoundRec = Integer.parseInt(this.upperBoundRec.getText());
            if (boxLength < upperBoundRec){
                int reply = JOptionPane.showConfirmDialog(frame, "The upper bound of rectangles is greater than the Box length. If you proceed with this settings there will probably be rectangles that will not fit within a box! Do you want to continue?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (reply == JOptionPane.NO_OPTION){
                    return;
                }
                if (reply == JOptionPane.YES_OPTION) {
                    System.out.println("Create Instance");
                    return;
                }
            }
            if (lowerBoundRec > upperBoundRec){
                JOptionPane.showMessageDialog(frame, "The lower bound of rectangles is greater than the upper bound. Please change the values.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("Create Instance");
        });
        var boxLengthText = new JLabel("Box Length");
        boxLength = new JTextField("Box Length");
        var boundRecText = new JLabel("Lower/Upper Bound");
        lowerBoundRec = new JTextField("Lower Bound");
        upperBoundRec = new JTextField("Upper Bound");

        GridBagConstraints c = new GridBagConstraints();
        JPanel inputPanel = new JPanel(new GridBagLayout());
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 0;
        c.gridy = 0;

        inputPanel.add(boxLengthText, c);
        c.gridx++;
        inputPanel.add(boxLength, c);
        c.gridx = 0;
        c.gridy++;
        inputPanel.add(boundRecText, c);
        c.gridx++;
        inputPanel.add(lowerBoundRec, c);
        c.gridx++;
        inputPanel.add(upperBoundRec, c);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 3;
        inputPanel.add(createInstance, c);

        return inputPanel;
    }
}

