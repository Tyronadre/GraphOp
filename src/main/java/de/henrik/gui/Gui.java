package de.henrik.gui;

import de.henrik.generator.RectangleDataGenerator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

public class Gui {
    private JButton createInstance;
    private JButton resetData;
    private DefaultValueTextField boxLength;
    private DefaultValueTextField lowerBoundRec;
    private DefaultValueTextField upperBoundRec;
    private DefaultValueTextField numberOfRectangles;
    private DefaultValueTextField seed;

    private Panel_InputData panel_InputData;
    private JFrame frame;

    public Gui() {
        frame = new JFrame("Gui");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;


        c.weightx = 0;
        c.weighty = 0;
        c.gridy = 0;
        c.gridx = 0;
        c.insets = new Insets(3, 3, 3, 3);
        frame.add(createPanel_CreateInstance(), c);
        c.gridx++;
        frame.add(createPanel_AlgorithmControls(), c);
        c.gridy = 1;
        c.gridx = 0;
        c.weighty = 1;
        c.gridwidth = 2;
        frame.add(createPanel_InputData(), c);
        c.gridy = 0;
        c.gridx = 3;
        c.weightx = 1;
        c.weighty = 1;
        c.gridheight = 2;
        frame.add(createPanel_OutputData(frame), c);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private Component createPanel_OutputData(JFrame frame) {
        var panel = new JPanel();
        panel.setBorder(new TitledBorder("Output Data"));
        return panel;
    }

    private Component createPanel_InputData( ) {
        panel_InputData = new Panel_InputData();
        var panel = new JScrollPane(panel_InputData);
        panel.getVerticalScrollBar().setUnitIncrement(16);
        panel.setBorder(new TitledBorder("Input Data"));
        return panel;
    }

    private Component createPanel_AlgorithmControls() {
        var panel = new Panel_AlgorithmControls(this);
        panel.setBorder(new TitledBorder("Algorithm Controls"));
        return panel;
    }

    private Component createPanel_CreateInstance( ) {
        createInstance = new JButton("Create Instance");
        createInstance.addActionListener(e -> {
            panel_InputData.reset();
            try {
                long seed = this.seed.getText().isEmpty() ? new Random().nextLong() : Long.parseLong(this.seed.getText());

                int boxLength = Integer.parseInt(this.boxLength.getTextOrDefault());
                int lowerBoundRec = Integer.parseInt(this.lowerBoundRec.getTextOrDefault());
                int upperBoundRec = Integer.parseInt(this.upperBoundRec.getTextOrDefault());
                int numberOfRectangles = Integer.parseInt(this.numberOfRectangles.getTextOrDefault());


                System.out.println(boxLength + " " + lowerBoundRec + " " + upperBoundRec + " " + numberOfRectangles + " " + seed);

                if (boxLength < upperBoundRec) {
                    int reply = JOptionPane.showConfirmDialog(this.frame, "The upper bound of rectangles is greater than the Box length. If you proceed with this settings there will probably be rectangles that will not fit within a box! Do you want to continue?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (reply == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                if (lowerBoundRec > upperBoundRec) {
                    JOptionPane.showMessageDialog(this.frame, "The lower bound of rectangles is greater than the upper bound. Please change the values.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println("Create Instance, Generating Data");
                panel_InputData.addRectangles(new RectangleDataGenerator(seed, lowerBoundRec, upperBoundRec, numberOfRectangles).generate());
                System.out.println("Data Generated");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        resetData = new JButton("Reset Data");
        resetData.addActionListener(e -> {
            System.out.println("Reset Data");
        });

        var seedText = new JLabel("Seed");
        seed = new DefaultValueTextField("Random", 5);

        var numberOfRectanglesText = new JLabel("Number of Rectangles");
        numberOfRectangles = new DefaultValueTextField("100", 5);

        var boxLengthText = new JLabel("Box Length");
        boxLength = new DefaultValueTextField("50", 5);
        var boundRecText = new JLabel("Lower/Upper Bound");
        lowerBoundRec = new DefaultValueTextField("10", 5);
        upperBoundRec = new DefaultValueTextField("20", 5);

        GridBagConstraints c = new GridBagConstraints();
        JPanel inputPanel = new JPanel(new GridBagLayout());
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(3, 3, 3, 3);

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
        inputPanel.add(numberOfRectanglesText, c);
        c.gridx++;
        inputPanel.add(numberOfRectangles, c);
        c.gridx = 0;
        c.gridy++;
        inputPanel.add(seedText, c);
        c.gridx++;
        inputPanel.add(seed, c);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 3;
        inputPanel.add(createInstance, c);
        inputPanel.setBorder(new TitledBorder("Create Instance"));

        return inputPanel;
    }
}

