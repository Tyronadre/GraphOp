package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.BoxData;
import de.henrik.data.BoxDataList;
import de.henrik.data.DataStructure;
import de.henrik.data.RectangleData;
import de.henrik.gui.Panel_InputData;
import de.henrik.gui.Panel_OutputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Regelbasiert: Anstelle von zulässigen Lösungen, arbeitet die lokale Suche hier auf
 * Permutationen von Rechtecken. Analog zum Greedy-Algorithmus werden die
 * Rechtecke in der Reihenfolge der Permutation in den Boxen platziert. Als Regel
 * Die Nachbarschaft definieren Sie durch kleine Modifikationsschritte auf der Permutation. Auch hier könnte es sinnvoll sein, Rechtecke in relativ leeren Boxen
 * anderswo in der Permutation zu platzieren.
 */
public class NG_RuleBased implements NeighbourGenerator<BoxDataList> {
    private static final int NUMBER_OF_PERMUTATIONS = 1;
    private static final boolean PAINT_EVERY_STEP = false;
    private static final boolean PAINT_NOTHING = true;
    private final Random random;
    private final Panel_InputData panel_inputData;
    private final Panel_OutputData panel_outputData;

    public NG_RuleBased(long seed, Panel_InputData panel_inputData, Panel_OutputData panel_outputData) {
        this.random = new Random(seed);
        this.panel_inputData = panel_inputData;
        this.panel_outputData = panel_outputData;
    }

    List<HashMap<Integer, BoxData>> lastChanges = null;

    @Override
    public void nextNeighbour(BoxDataList data) {


        for (int i = 0; i < NUMBER_OF_PERMUTATIONS; i++) {
            int initialNumberOfBoxes = data.getNumberOfBoxes();

            int box1 = random.nextInt(data.getNumberOfBoxes());
            int box2 = random.nextInt(data.getNumberOfBoxes());
            int index1 = random.nextInt(data.getNumberOfRectangles(box1));
            int index2 = random.nextInt(data.getNumberOfRectangles(box2) + 1);

            lastChanges = data.shift(box1, box2, index1, index2);
//            System.out.print("\r" + i + " " + box1 + " " + box2 + " " + index1 + " " + index2 + " " + lastChanges);

            if (lastChanges == null) continue;
            if (!lastChanges.get(0).keySet().isEmpty()) {
                for (var entry : lastChanges.get(0).entrySet()) {
                    panel_outputData.removeBox(entry.getKey());
                }
            }
            //assuming the overfill will always fit in one box
            if (!lastChanges.get(2).keySet().isEmpty()) {
                for (var entry : lastChanges.get(2).entrySet()) {
                    panel_outputData.addBox(entry.getValue());
                }
            }
            panel_outputData.validate();
            panel_outputData.repaint();
        }
    }

    @Override
    public void revert(BoxDataList data) {
        if (lastChanges == null) return;
        // revert removed boxes
        for (var entry : lastChanges.get(0).entrySet()) {
            panel_outputData.addBox(entry.getValue());
        }
        // revert changed boxes
        for (var entry : lastChanges.get(1).entrySet()) {
            data.getBoxWithID(entry.getKey()).loadData(entry.getValue());
        }
        // revert added boxes
        for (var entry : lastChanges.get(2).entrySet()) {
            panel_outputData.removeBox(entry.getKey());
        }
        lastChanges = null;
    }
}