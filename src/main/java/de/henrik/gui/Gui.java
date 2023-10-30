package de.henrik.gui;

import de.henrik.generator.RectangleDataGenerator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

public class Gui {

    private Panel_InputData panel_InputData;
    private Panel_CreateInstance panel_CreateInstance;
    private Panel_AlgorithmControls panel_AlgorithmControls;
    private Panel_OutputData panel_OutputData;
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
        frame.invalidate();
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
        panel_AlgorithmControls = new Panel_AlgorithmControls(this);
        panel_AlgorithmControls.setBorder(new TitledBorder("Algorithm Controls"));
        return panel_AlgorithmControls;
    }

    private Component createPanel_CreateInstance( ) {
        panel_CreateInstance = new Panel_CreateInstance(this);
        panel_CreateInstance.setBorder(new TitledBorder("Create Instance"));
        return panel_CreateInstance;
    }

    public Panel_InputData getPanel_InputData() {
        return panel_InputData;
    }

    public Component getFrame() {
        return frame;
    }
}

