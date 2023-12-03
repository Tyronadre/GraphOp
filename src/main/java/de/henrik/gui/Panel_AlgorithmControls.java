package de.henrik.gui;

import de.henrik.algorithm.AbstractAlgorithm;
import de.henrik.algorithm.geedyAlgorithm.*;
import de.henrik.algorithm.localSearchAlgorithm.*;
import de.henrik.data.BoxDataList;
import de.henrik.data.RectangleData;
import de.henrik.gui.extraComponents.DefaultValueTextField;
import de.henrik.gui.extraComponents.ProgressButton;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Panel_AlgorithmControls extends JPanel {
    private final JButton cancelAlgo = new JButton("Force Stop Algorithm");
    private final DefaultValueTextField seed = new DefaultValueTextField("Random", 1);
    private final JLabel seedLabel = new JLabel("Seed:");
    private final ProgressButton algo1 = new ProgressButton("Local Search");
    private final ProgressButton algo2 = new ProgressButton("Greedy Algorithm");
    private final JButton algoPause = new JButton("Pause Algorithm");
    private final JButton verbose = new JButton("Verbose");

    public Panel_AlgorithmControls(Gui frame) {
        //SpeedPanel
        JSlider algoSpeed = new JSlider(0, 1000, 100);
        JButton algoFast = new JButton("Run Slow");
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
        algoFast.setPreferredSize(new Dimension(200, 20));
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
        var stategyAlgo1 = new JComboBox<>(new String[]{"Geometriebasiert", "Regelbasiert", "Überlappungen teilweise zulassen"});
        algo1.addActionListener(ActionListener -> {
            frame.getPanel_OutputData().reset();
            try {
                var initialDataStructur = new BoxDataList(frame.getPanel_InputData().getBoxWidth(), frame.getPanel_InputData().getRecMinSize());
                initialDataStructur.addAll(frame.getPanel_InputData().getData());
                var seed = this.seed.getText().isEmpty() ? new Random().nextLong() : Long.parseLong(this.seed.getText());
                var algo = new LocalSearchAlgorithm<>(seed, initialDataStructur, switch ((String) Objects.requireNonNull(stategyAlgo1.getSelectedItem())) {
                    case "Geometriebasiert" -> new NG_GeometryBased();
                    case "Regelbasiert" -> new NG_RuleBased(seed, frame.getPanel_InputData(), frame.getPanel_OutputData());
                    case "Überlappungen teilweise zulassen" -> new NG_Overlapping();
                    default -> throw new IllegalStateException("Unexpected value: " + stategyAlgo1.getSelectedItem());
                }, new DSE_EmptySpace(frame.getPanel_InputData().getBoxWidth(), frame.getPanel_InputData().getRecMinSize()));
                algo.addProgressListener(e -> algo1.setProgress(e.getProgress()));
                algo2.setEnabled(false);
                frame.getPanel_DataControls().setEnabled(false);
                algo.onFinish(() -> frame.getPanel_DataControls().setEnabled(true));
                algo.onFinish(() -> frame.getPanel_InputData().repaint());

                new Thread(algo).start();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        var strategyAlgo2 = new JComboBox<>(new String[]{"PutInFirst", "TakeBiggest", "PutInFirstSorted", "TakeBiggestSorted"});
        algo2.addActionListener(ActionListener -> {
            try {
                frame.getPanel_OutputData().reset();
                var algo = new GreedyAlgorithm<>(this.seed.getText().isEmpty() ? new Random().nextLong() : Long.parseLong(this.seed.getText()), frame.getPanel_InputData().getData(), switch ((String) Objects.requireNonNull(strategyAlgo2.getSelectedItem())) {
                    case "PutInFirst" -> new DR_PutInFirst(frame.getPanel_InputData(), frame.getPanel_OutputData());
                    case "TakeBiggest" -> new DR_TakeBiggest(frame.getPanel_InputData(), frame.getPanel_OutputData());
                    case "PutInFirstSorted" ->
                            new DR_PutInFirstSorted(frame.getPanel_InputData(), frame.getPanel_OutputData());
                    case "TakeBiggestSorted" ->
                            new DR_TakeBiggestSorted(frame.getPanel_InputData(), frame.getPanel_OutputData());
                    default -> throw new IllegalStateException("Unexpected value: " + strategyAlgo2.getSelectedItem());
                });
                algo.addProgressListener(e -> algo2.setProgress(e.getProgress()));
                algo1.setEnabled(false);
                frame.getPanel_DataControls().setEnabled(false);
                algo.onFinish(() -> frame.getPanel_DataControls().setEnabled(true));
                algo.onFinish(() -> frame.getPanel_InputData().repaint());
                algo.onFinish(() -> JOptionPane.showMessageDialog(this, "Finished: Used Boxes=" + algo.getSolution().stream().map(RectangleData::getBoxData).distinct().count(), "Finished", JOptionPane.INFORMATION_MESSAGE));
                new Thread(algo).start();
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
        this.add(stategyAlgo1, c);
        c.gridy = 7;
        c.gridx = 0;
        this.add(algo2, c);
        c.gridx = 1;
        this.add(strategyAlgo2, c);
    }

    public void reset() {
        algo1.reset();
        algo2.reset();
    }
}
