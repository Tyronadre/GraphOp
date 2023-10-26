package de.henrik.gui;

import de.henrik.algorithm.AbstractAlgorithm;
import de.henrik.algorithm.LocalSearchAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Panel_AlgorithmControls extends JPanel {
    private final JButton cancelAlgo = new JButton("Force Stop Algorithm");
    private final DefaultValueTextField seed = new DefaultValueTextField("Random", 1);
    private final JLabel seedLabel = new JLabel("Seed:");
    private final JButton algo1 = new JButton("Local Search");
    private final JButton algo2 = new JButton("Greedy Algorithm");
    private final JButton algoPause = new JButton("Pause Algorithm");
    private final JButton verbose = new JButton("Verbose");

    public Panel_AlgorithmControls(Gui frame) {
        //SpeedPanel
        JSlider algoSpeed = new JSlider(0, 1000, 100);
        JButton algoFast = new JButton("Run Fast");
        algoSpeed.addChangeListener(e -> {
            if (algoSpeed.getValue() == 0) {
                algoSpeed.setValue(10);
            }
            AbstractAlgorithm.setSpeed(algoSpeed.getValue());
            algoSpeed.setToolTipText("Waiting Time between Steps: " + algoSpeed.getValue() + " ms");
        });
        algoSpeed.setMajorTickSpacing(250);
        algoSpeed.setMinorTickSpacing(10);
        algoSpeed.setPaintTicks(true);
        algoSpeed.setPaintLabels(true);
        algoSpeed.setSnapToTicks(true);
        algoFast.addActionListener(e -> {
            if (Objects.equals(algoFast.getText(), "Run Fast")) {
                algoFast.setText("Run Slow");
                AbstractAlgorithm.setSlow(false);
            } else {
                algoFast.setText("Run Fast");
                AbstractAlgorithm.setSlow(true);
            }
        });
        algoFast.setPreferredSize(new Dimension(100, 20));
        JPanel speedPanel = new JPanel();
        speedPanel.setLayout(new BoxLayout(speedPanel, BoxLayout.X_AXIS));
        speedPanel.setBorder(BorderFactory.createTitledBorder("SpeedControl"));
        speedPanel.add(algoSpeed);
        speedPanel.add(algoFast);

        //Other Controls
        algoPause.addActionListener(e -> {
            if (Objects.equals(algoPause.getText(), "Pause Algorithm")) {
                algoPause.setText("Resume Algorithm");
                AbstractAlgorithm.pause();
            } else {
                algoPause.setText("Pause Algorithm");
                AbstractAlgorithm.resume();
            }
        });
        JButton algoStep = new JButton("Step Algorithm");
        algoStep.addActionListener(e -> {
            AbstractAlgorithm.step();
            algoPause.setText("Resume Algorithm");
        });
        verbose.addActionListener(e -> {
            if (verbose.getText().equals("Verbose")) {
                verbose.setText("Non-Verbose");
                AbstractAlgorithm.setVerbose(true);
            } else {
                verbose.setText("Verbose");
                AbstractAlgorithm.setVerbose(false);
            }
        });
        cancelAlgo.addActionListener(e -> {
            AbstractAlgorithm.cancel();
            algoPause.setText("Pause Algorithm");
        });

        //Algorithms
        algo1.addActionListener(ActionListener -> {
            try {
                new LocalSearchAlgorithm(this.seed.getText().isEmpty() ? new Random().nextLong() : Long.parseLong(this.seed.getText())).runAlgorithm();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        algo2.addActionListener(ActionListener -> {
            try {
                new LocalSearchAlgorithm(this.seed.getText().isEmpty() ? new Random().nextLong() : Long.parseLong(this.seed.getText())).runAlgorithm();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //Add Components
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(1, 1, 1, 1);

        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
        c.weightx = 1.0;

        this.add(seedLabel, c);
        c.gridx = 1;
        this.add(seed, c);

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        this.add(algoPause, c);
        c.gridy = 2;
        this.add(algoStep, c);
        c.gridy = 3;
        this.add(verbose, c);
        c.gridy = 4;
        this.add(cancelAlgo, c);
        c.gridy = 5;
        this.add(speedPanel, c);

        c.gridwidth = 1;
        c.gridy = 6;
        this.add(algo1, c);
        c.gridx = 1;
        this.add(algo2, c);


    }
}
