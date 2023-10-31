package de.henrik.gui;

import de.henrik.data.RectangleData;
import de.henrik.generator.Generator;
import de.henrik.generator.RectangleDataGenerator;
import de.henrik.gui.extraComponents.DefaultValueTextField;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

public class Panel_DataControl extends JPanel {

    private final JButton createInstance;
    private final JButton regenerateInstance;
    private final JButton resetData;
    private final DefaultValueTextField boxLength;
    private final DefaultValueTextField lowerBoundRec;
    private final DefaultValueTextField upperBoundRec;
    private final DefaultValueTextField numberOfRectangles;
    private final DefaultValueTextField seed;
    private Generator<RectangleData> generator;
    private Gui gui;

    public Panel_DataControl(Gui gui) {
        this.gui = gui;

        var seedText = new JLabel("Seed");
        seed = new DefaultValueTextField("Random", 5);

        var numberOfRectanglesText = new JLabel("Number of Rectangles");
        numberOfRectangles = new DefaultValueTextField("1000", 5);

        var boxLengthText = new JLabel("Box Length");
        boxLength = new DefaultValueTextField("200", 5);
        var boundRecText = new JLabel("Lower/Upper Bound");
        lowerBoundRec = new DefaultValueTextField("10", 5);
        upperBoundRec = new DefaultValueTextField("100", 5);

        createInstance = new JButton("Create Instance");
        regenerateInstance = new JButton("Regenerate Instance");
        createInstance.addActionListener(e -> {
            gui.getPanel_InputData().reset();
            try {
                long seed = this.seed.getText().isEmpty() ? new Random().nextLong() : Long.parseLong(this.seed.getText());

                int boxLength = Integer.parseInt(this.boxLength.getTextOrDefault());
                int lowerBoundRec = Integer.parseInt(this.lowerBoundRec.getTextOrDefault());
                int upperBoundRec = Integer.parseInt(this.upperBoundRec.getTextOrDefault());
                int numberOfRectangles = Integer.parseInt(this.numberOfRectangles.getTextOrDefault());


                System.out.println(boxLength + " " + lowerBoundRec + " " + upperBoundRec + " " + numberOfRectangles + " " + seed);

                if (boxLength <= 0 || lowerBoundRec <= 0 || upperBoundRec <= 0 || numberOfRectangles <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter positive numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (boxLength < lowerBoundRec) {
                    JOptionPane.showMessageDialog(this, "The lower bound of rectangles is greater than the Box length. Please change the values.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (boxLength < upperBoundRec) {
                    int reply = JOptionPane.showConfirmDialog(this, "The upper bound of rectangles is greater than the Box length. If you proceed with this settings there will probably be rectangles that will not fit within a box! Do you want to continue?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (reply == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                if (lowerBoundRec > upperBoundRec) {
                    JOptionPane.showMessageDialog(this, "The lower bound of rectangles is greater than the upper bound. Please change the values.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println("Create Instance, Generating Data");
                generator = new RectangleDataGenerator(seed, lowerBoundRec, upperBoundRec, numberOfRectangles);
                gui.getPanel_InputData().setGenerator(generator);
                generator.generate();
                gui.getPanel_InputData().setBoxWidth(boxLength);
                System.out.println("Data Generated, Adding Data to Panel");
                gui.getPanel_InputData().addRectangles(generator.getData());
                regenerateInstance.setEnabled(true);
                resetAlgorithmControls();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        regenerateInstance.addActionListener(e -> {
            gui.getPanel_InputData().reset();
            generator.reset();
            gui.getPanel_InputData().setGenerator(generator);
            System.out.println("Create Instance, Generating Data");
            generator.generate();
            System.out.println("Data Generated, Adding Data to Panel");
            gui.getPanel_InputData().addRectangles(generator.getData());
            resetAlgorithmControls();
        });
        regenerateInstance.setEnabled(false);

        resetData = new JButton("Clear Output");
        resetData.addActionListener(e -> System.out.println("Reset Data"));


        GridBagConstraints c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(3, 3, 3, 3);

        c.gridx = 0;
        c.gridy = 0;

        add(boxLengthText, c);
        c.gridx++;
        add(boxLength, c);
        c.gridx = 0;
        c.gridy++;
        add(boundRecText, c);
        c.gridx++;
        add(lowerBoundRec, c);
        c.gridx++;
        add(upperBoundRec, c);
        c.gridx = 0;
        c.gridy++;
        add(numberOfRectanglesText, c);
        c.gridx++;
        add(numberOfRectangles, c);
        c.gridx = 0;
        c.gridy++;
        add(seedText, c);
        c.gridx++;
        add(seed, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 3;
        add(createInstance, c);
        c.gridy++;
        add(regenerateInstance, c);
        setBorder(new TitledBorder("Create Instance"));
    }

    private void resetAlgorithmControls() {
        gui.getPanel_AlgorithmControls().reset();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        createInstance.setEnabled(enabled);
        regenerateInstance.setEnabled(enabled);
    }
}
